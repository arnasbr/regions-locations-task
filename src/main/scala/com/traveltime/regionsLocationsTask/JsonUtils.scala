package com.traveltime.regionsLocationsTask

import io.circe.Decoder
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax.EncoderOps
import scala.util.Using
import scala.io.Source
import Models._
import Codecs._

object JsonUtils {
  def parseAndDecode[T: Decoder](
      jsonFilePath: String
  ): Either[Throwable, List[T]] = {
    for {
      jsonString <- Using(Source.fromFile(jsonFilePath))(_.mkString).toEither
      json <- parse(jsonString)
      list <- json.as[List[T]]
    } yield list
  }

  def regionsToJsonString(
      regionWithLocations: Either[Throwable, List[RegionWithLocations]]
  ): Either[Throwable, String] = {
    regionWithLocations.map(_.asJson.spaces2)
  }
}
