package com.traveltime.regionsLocationsTask

object Models {
  case class Coordinate(x: Double, y: Double)

  case class Region(name: String, coordinates: List[List[Coordinate]])

  case class Location(name: String, coordinates: Coordinate)

  case class RegionWithLocations(
      region: String,
      matched_locations: List[String]
  )
}
