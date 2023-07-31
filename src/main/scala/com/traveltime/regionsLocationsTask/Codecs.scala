package com.traveltime.regionsLocationsTask

import com.traveltime.regionsLocationsTask.Models._
import io.circe.Decoder

object Codecs {
  implicit val decodeCoordinate: Decoder[Coordinate] =
    Decoder[List[Double]].emap {
      case List(x, y) => Right(Coordinate(x, y))
      case _          => Left("Coordinate must be a list of two numbers")
    }

  implicit val decodeLocation: Decoder[Location] =
    Decoder.forProduct2("name", "coordinates")(Location.apply)
}
