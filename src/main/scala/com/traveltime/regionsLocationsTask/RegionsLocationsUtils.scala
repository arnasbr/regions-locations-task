package com.traveltime.regionsLocationsTask
import Models._

object RegionsLocationsUtils {
  def matchLocationsWithRegions(
      regions: List[Region],
      locations: List[Location]
  ): List[RegionWithLocations] = {
    regions.map { region =>
      val matchedLocations = locations.filter(location =>
        region.coordinates.exists(polygon =>
          GeographyUtils.pointInPolygon(location.coordinates, polygon)
        )
      )
      RegionWithLocations(region.name, matchedLocations.map(_.name))
    }
  }
}
