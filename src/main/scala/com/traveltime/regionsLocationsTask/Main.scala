package com.traveltime.regionsLocationsTask

import com.monovore.decline.{CommandApp, Opts}
import io.circe.generic.auto._
import java.nio.file.{Files, Paths}
import java.nio.charset.StandardCharsets
import com.traveltime.regionsLocationsTask.Codecs._
import Models._
import JsonUtils._
import RegionsLocationsUtils._

object Main
    extends CommandApp(
      name = "regions-locations",
      header = "Matches locations to regions",
      main = {
        Cli.command.map { args =>
          val locationsFile = args.locationsFile
          val regionsFile = args.regionsFile
          val outputFile = args.outputFile

          // Parse and decode JSON for locations
          val locationsResult = parseAndDecode[Location](locationsFile)

          // Parse and decode JSON for regions
          val regionsResult = parseAndDecode[Region](regionsFile)

          val regionWithLocations = for {
            regions <- regionsResult
            locations <- locationsResult
          } yield matchLocationsWithRegions(regions, locations)

          val jsonStringEither = regionsToJsonString(regionWithLocations)

          jsonStringEither match {
            case Right(jsonString) =>
              // This is a side effect
              Files.write(
                Paths.get(outputFile),
                jsonString.getBytes(StandardCharsets.UTF_8)
              )
            case Left(error) =>
              println(s"An error occurred: ${error.getMessage}")
          }
        }
      }
    )
