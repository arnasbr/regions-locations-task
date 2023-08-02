package com.traveltime.regionsLocationsTask

import com.monovore.decline.{CommandApp, Opts}
import io.circe.generic.auto._

import java.nio.file.{Files, Paths}
import java.nio.charset.StandardCharsets
import com.traveltime.regionsLocationsTask.Codecs._
import Models._
import JsonUtils._
import RegionsLocationsUtils._
import io.circe.syntax.EncoderOps

object Main
    extends CommandApp(
      name = "regions-locations",
      header = "Matches locations to regions",
      main = {
        Cli.command.map { args =>
          val locationsFile = args.locationsFile
          val regionsFile = args.regionsFile
          val outputFile = args.outputFile

          (
            for {
              locations <- parseAndDecode[Location](locationsFile.toString())
              regions <- parseAndDecode[Region](regionsFile.toString())
            } yield {
              val matched = matchLocationsWithRegions(regions, locations)
              val sortedMatched = matched.map { regionWithLocations =>
                val sortedLocations =
                  regionWithLocations.matchedLocations.sortBy(_.name)
                regionWithLocations.copy(matchedLocations = sortedLocations)
              }
              sortedMatched.sortBy(_.region)
            }
          ) match {
            case Left(error) =>
              println(s"An error occurred: ${error.toString}")

            case Right(regionsWithLocations) =>
              Files.write(
                Paths.get(outputFile.toString()),
                regionsWithLocations.asJson.spaces2.getBytes(
                  StandardCharsets.UTF_8
                )
              )
          }
        }
      }
    )
