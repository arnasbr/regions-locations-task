package com.traveltime.regionsLocationsTask
import io.circe.generic.extras.JsonKey
import io.circe.generic.extras.semiauto._
import io.circe.generic.extras.{Configuration, ConfiguredJsonCodec}

object Models {
  case class Coordinate(x: Double, y: Double)

  case class Polygon(coordinates: List[Coordinate])

  case class Region(name: String, polygons: List[Polygon])

  case class Location(name: String, coordinates: Coordinate)

  case class RegionWithLocations(region: String, matchedLocations: List[String])

  case class CliArgs(
      locationsFile: String,
      regionsFile: String,
      outputFile: String
  )
}
