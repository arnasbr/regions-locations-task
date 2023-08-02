package com.traveltime.regionsLocationsTask

import io.circe._
import com.traveltime.regionsLocationsTask.Models._
import io.circe.Decoder
import io.circe.generic.extras.{Configuration, semiauto}
import cats.syntax.traverse._
import cats.instances.list._
import cats.instances.either._
import io.circe.generic.semiauto.deriveDecoder

object Codecs {
  implicit val customConfig: Configuration =
    Configuration.default.withSnakeCaseMemberNames

  implicit val encodeCoordinate: Encoder[Coordinate] =
    Encoder.forProduct2("x", "y")(c => (c.x, c.y))

  implicit val decodeCoordinate: Decoder[Coordinate] =
    Decoder[List[Double]].emap {
      case List(x, y) => Right(Coordinate(x, y))
      case _          => Left("Coordinate must be a list of two numbers")
    }

  implicit val encodePolygon: Encoder[Polygon] =
    Encoder.forProduct1("coordinates")(p => p.coordinates)

  implicit val decodePolygon: Decoder[Polygon] =
    Decoder[List[List[Double]]].emap { list =>
      val coordList = list.map {
        case List(x, y) => Right(Coordinate(x, y))
        case _          => Left("Coordinates must be a list of two numbers")
      }

      coordList.sequence match {
        case Right(coordinates) => Right(Polygon(coordinates))
        case Left(err)          => Left(err)
      }
    }

  implicit val decodeRegion: Decoder[Region] =
    Decoder.forProduct2("name", "coordinates")(Region.apply)

  implicit val encodeRegion: Encoder[Region] =
    Encoder.forProduct2("name", "polygons")(r => (r.name, r.polygons))

  implicit val encodeLocation: Encoder[Location] =
    Encoder.forProduct2("name", "coordinates")(l => (l.name, l.coordinates))

  implicit val decodeLocation: Decoder[Location] =
    Decoder.forProduct2("name", "coordinates")(Location.apply)

  implicit val encodeRegionWithLocations: Encoder[RegionWithLocations] =
    Encoder.forProduct2("region", "matched_locations") { r =>
      (r.region, r.matchedLocations.map(_.name))
    }

  implicit val decodeRegionWithLocations: Decoder[RegionWithLocations] =
    deriveDecoder[RegionWithLocations]
}
