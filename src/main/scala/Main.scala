import io.circe.Decoder
import io.circe.generic.auto._
import io.circe.parser._

import scala.util.Using
import scala.io.Source

case class Coordinate(x: Double, y: Double)
case class Region(name: String, coordinates: List[List[Coordinate]])
case class Location(name: String, coordinates: Coordinate)

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

  // Parse and decode JSON for locations
  val locationsResult = parseAndDecode[Location]("Input/locations.json")

  // Parse and decode JSON for regions
  val regionsResult = parseAndDecode[Region]("Input/regions.json")
}
