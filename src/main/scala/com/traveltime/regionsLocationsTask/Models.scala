package com.traveltime.regionsLocationsTask
import io.circe.generic.extras.JsonKey
import io.circe.generic.extras.semiauto._
import io.circe.generic.extras.Configuration

object Models {
  case class Coordinate(x: Double, y: Double)

  case class Region(name: String, coordinates: List[List[Coordinate]])

  case class Location(name: String, coordinates: Coordinate)

  case class RegionWithLocations(
      region: String,
      @JsonKey("matched_locations") MatchedLocations: List[String]
  )

  case class CliArgs(
      locationsFile: String,
      regionsFile: String,
      outputFile: String
  )
}
