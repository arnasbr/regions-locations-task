import io.circe.Decoder
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax.EncoderOps

import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Paths}
import scala.util.Using
import scala.io.Source

import io.circe.Printer

case class Coordinate(x: Double, y: Double)
case class Region(name: String, coordinates: List[List[Coordinate]])
case class Location(name: String, coordinates: Coordinate)

case class RegionWithLocations(region: String, matched_locations: List[String])

object Main extends App {
  // Custom decoders
  implicit val decodeCoordinate: Decoder[Coordinate] =
    Decoder[List[Double]].emap {
      case List(x, y) => Right(Coordinate(x, y))
      case _          => Left("Coordinate must be a list of two numbers")
    }

  implicit val decodeLocation: Decoder[Location] =
    Decoder.forProduct2("name", "coordinates")(Location.apply)

  def parseAndDecode[T: Decoder](
      jsonFilePath: String
  ): Either[Throwable, List[T]] = {
    val tryJsonString = Using(Source.fromFile(jsonFilePath))(_.mkString)

    tryJsonString.toEither.flatMap { jsonString =>
      parse(jsonString).flatMap(_.as[List[T]])
    }
  }

  def pointInPolygon(point: Coordinate, polygon: List[Coordinate]): Boolean = {
    val x = point.x
    val y = point.y
    var isInside = false
    var j = polygon.size - 1

    for (i <- polygon.indices) {
      val xi = polygon(i).x
      val yi = polygon(i).y
      val xj = polygon(j).x
      val yj = polygon(j).y

      val intersect = ((yi > y) != (yj > y)) &&
        (x < (xj - xi) * (y - yi) / (yj - yi) + xi)

      if (intersect) isInside = !isInside
      j = i
    }

    isInside
  }

  // Parse and decode JSON for locations
  val locationsResult = parseAndDecode[Location]("Input/locations.json")

  // Parse and decode JSON for regions
  val regionsResult = parseAndDecode[Region]("Input/regions.json")

  val regionWithLocations = regionsResult.flatMap { regions =>
    locationsResult.map { locations =>
      regions.map { region =>
        val matchedLocations = locations.filter(location =>
          region.coordinates.exists(polygon =>
            pointInPolygon(location.coordinates, polygon)
          )
        )
        RegionWithLocations(region.name, matchedLocations.map(_.name))
      }
    }
  }

  regionWithLocations match {
    case Left(failure) =>
      println(s"Failed to process: ${failure.getMessage}")
    case Right(results) =>
      results.foreach(result =>
        println(
          s"Region: ${result.region}, Matched Locations: ${result.matched_locations}"
        )
      )
  }

  regionWithLocations match {
    case Right(regions) =>
      val jsonString = regions.asJson.spaces2 // Convert the results to JSON
      Files.write(
        Paths.get("Output/results.json"),
        jsonString.getBytes(StandardCharsets.UTF_8)
      ) // Write to a file
    case Left(error) =>
      println(s"An error occurred: ${error.getMessage}")
  }
}
