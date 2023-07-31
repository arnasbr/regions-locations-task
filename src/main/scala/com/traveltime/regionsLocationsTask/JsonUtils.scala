package com.traveltime.regionsLocationsTask

import io.circe.Decoder
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax.EncoderOps
import scala.util.Using
import scala.io.Source
import Models._

object JsonUtils {
  def parseAndDecode[T: Decoder](
      jsonFilePath: String
  ): Either[Throwable, List[T]] = {
    val tryJsonString = Using(Source.fromFile(jsonFilePath))(_.mkString)
    tryJsonString.toEither.flatMap { jsonString =>
      parse(jsonString).flatMap(_.as[List[T]])
    }
  }

  def regionsToJsonString(
      regionWithLocations: Either[Throwable, List[RegionWithLocations]]
  ): Either[Throwable, String] = {
    regionWithLocations.map(_.asJson.spaces2)
  }
}
