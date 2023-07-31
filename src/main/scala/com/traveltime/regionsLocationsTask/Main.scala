package com.traveltime.regionsLocationsTask

import com.monovore.decline.{CommandApp, Opts}
import io.circe.Decoder
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax.EncoderOps

import java.nio.file.{Files, Paths}
import scala.util.Using
import scala.io.Source
import java.nio.charset.StandardCharsets
import com.traveltime.regionsLocationsTask.Codecs._
import Models._

import scala.annotation.tailrec

object Main
    extends CommandApp(
      name = "regions-locations",
      header = "Matches locations to regions",
      main = {
        Cli.command.map { args =>
          val locationsFile = args.locationsFile
          val regionsFile = args.regionsFile
          val outputFile = args.outputFile

          def parseAndDecode[T: Decoder](
              jsonFilePath: String
          ): Either[Throwable, List[T]] = {
            val tryJsonString = Using(Source.fromFile(jsonFilePath))(_.mkString)

            tryJsonString.toEither.flatMap { jsonString =>
              parse(jsonString).flatMap(_.as[List[T]])
            }
          }

          def pointInPolygon(
              point: Coordinate,
              polygon: List[Coordinate]
          ): Boolean = {
            val windingNumber: Int = polygon
              .sliding(2)
              .collect {
                case a :: b :: Nil
                    if a.y <= point.y && b.y > point.y && isLeft(
                      a,
                      b,
                      point
                    ) > 0 =>
                  1
                case a :: b :: Nil
                    if a.y > point.y && b.y <= point.y && isLeft(
                      a,
                      b,
                      point
                    ) < 0 =>
                  -1
                case _ => 0
              }
              .sum

            windingNumber != 0
          }

          def isLeft(a: Coordinate, b: Coordinate, c: Coordinate): Double = {
            (b.x - a.x) * (c.y - a.y) - (c.x - a.x) * (b.y - a.y)
          }

          // Parse and decode JSON for locations
          val locationsResult = parseAndDecode[Location](locationsFile)

          // Parse and decode JSON for regions
          val regionsResult = parseAndDecode[Region](regionsFile)

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
              val jsonString =
                regions.asJson.spaces2 // Convert the results to JSON
              Files.write(
                Paths.get(outputFile),
                jsonString.getBytes(StandardCharsets.UTF_8)
              ) // Write to a file
              ()
            case Left(error) =>
              println(s"An error occurred: ${error.getMessage}")
          }
        }
      }
    )
