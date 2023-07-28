import io.circe.generic.auto._, io.circe.parser._

import scala.util.{Try, Using}
import scala.io.Source

case class Region(name: String, coordinates: List[List[List[Double]]])
case class Location(name: String, coordinates: List[Double])

object Main extends App {

  def parseAndDecode[T: io.circe.Decoder](
      jsonFilePath: String
  ): Either[Throwable, List[T]] = {
    val tryJsonString = Using(Source.fromFile(jsonFilePath))(_.mkString)

    tryJsonString.toEither.flatMap { jsonString =>
      parse(jsonString).flatMap(_.as[List[T]])
    }
  }

  // Parse and decode JSON for locations
  val locationsResult =
    parseAndDecode[Location]("Input/locations.json")

  locationsResult match {
    case Left(failure) =>
      println(s"Failed to parse/decode Locations: ${failure.getMessage}")
    case Right(locations) =>
      locations.foreach(location =>
        println(s"Successfully decoded location: $location")
      )
  }

  // Parse and decode JSON for regions
  val regionsResult = parseAndDecode[Region]("Input/regions.json")

  regionsResult match {
    case Left(failure) =>
      println(s"Failed to parse/decode Regions: ${failure.getMessage}")
    case Right(regions) =>
      regions.foreach(region =>
        println(s"Successfully decoded region: $region")
      )
  }
}
