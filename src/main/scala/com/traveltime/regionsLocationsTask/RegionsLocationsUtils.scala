package com.traveltime.regionsLocationsTask
import Models._

object RegionsLocationsUtils {
  def matchLocationsWithRegions(
      regions: List[Region],
      locations: List[Location]
  ): List[RegionWithLocations] = {
    regions.map { region =>
      val matchedLocations = locations.filter(location =>
        region.polygons.exists(polygon =>
          GeographyUtils.pointInPolygon(
            location.coordinates,
            polygon.coordinates
          )
        )
      )
      RegionWithLocations(region.name, matchedLocations)
    }
  }
}
