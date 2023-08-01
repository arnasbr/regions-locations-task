package com.traveltime.regionsLocationsTask

import io.circe.Decoder
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax.EncoderOps
import scala.util.Using
import scala.io.Source
import Models._
import Codecs._
import CustomError._

object JsonUtils {
  def parseAndDecode[T: Decoder](
      jsonFilePath: String
  ): Either[CustomError, List[T]] =
    for {
      data <- Using(Source.fromFile(jsonFilePath))(_.mkString).toEither.left
        .map(e => FileReadError(e.toString))
      circeJson <- parse(data).left.map(e => ParseError(e.toString))
      decodedList <- circeJson
        .as[List[T]]
        .left
        .map(e => DecodeError(e.toString()))
    } yield decodedList

  def regionsToJsonString(
      regionWithLocations: Either[CustomError, List[RegionWithLocations]]
  ): Either[CustomError, String] = {
    regionWithLocations.left
      .map(e => ConversionError(e.toString))
      .map(_.asJson.spaces2)
  }
}
