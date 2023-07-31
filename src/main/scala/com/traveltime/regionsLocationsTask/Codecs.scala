package com.traveltime.regionsLocationsTask

import io.circe._
import com.traveltime.regionsLocationsTask.Models._
import io.circe.Decoder
import io.circe.generic.extras.{semiauto, Configuration}

object Codecs {
  implicit val customConfig: Configuration =
    Configuration.default.withSnakeCaseMemberNames

  implicit val decodeCoordinate: Decoder[Coordinate] =
    Decoder[List[Double]].emap {
      case List(x, y) => Right(Coordinate(x, y))
      case _          => Left("Coordinate must be a list of two numbers")
    }

  implicit val decodeLocation: Decoder[Location] =
    Decoder.forProduct2("name", "coordinates")(Location.apply)

  implicit val encodeRegionWithLocations: Encoder[RegionWithLocations] =
    semiauto.deriveConfiguredEncoder[RegionWithLocations]
}
