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

object Main
    extends CommandApp(
      name = "regions-locations",
      header = "Matches locations to regions",
      main = {
        Cli.command.map { case (locationsFile, regionsFile, outputFile) =>
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
            val x = point.x
            val y = point.y
            var isInside = false
            var j = polygon.size - 1

            for (i <- polygon.indices) {
              val xi = polygon(i).x
              val yi = polygon(i).y
              val xj = polygon(j).x
              val yj = polygon(j).y

              val intersect = ((yi > y) != (yj > y)) &&
                (x < (xj - xi) * (y - yi) / (yj - yi) + xi)

              if (intersect) isInside = !isInside
              j = i
            }

            isInside
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
