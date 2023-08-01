package com.traveltime.regionsLocationsTask

import io.circe.Decoder
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax.EncoderOps
import scala.util.Using
import scala.io.Source
import Models._
import Codecs._
import CustomErrors._

object JsonUtils {
  def parseAndDecode[T: Decoder](
      jsonFilePath: String
  ): Either[CustomErrors, List[T]] = {
    for {
      jsonString <- Using(Source.fromFile(jsonFilePath))(
        _.mkString
      ).toEither.left.map(e => FileReadError(e.toString))
      json <- parse(jsonString).left.map(e => ParseError(e.toString))
      list <- json.as[List[T]].left.map(e => DecodeError(e.toString()))
    } yield list
  }

  def regionsToJsonString(
      regionWithLocations: Either[CustomErrors, List[RegionWithLocations]]
  ): Either[CustomErrors, String] = {
    regionWithLocations.left
      .map(e => ConversionError(e.toString))
      .map(_.asJson.spaces2)
  }
}
