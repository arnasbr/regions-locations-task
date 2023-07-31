package com.traveltime

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers._
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks
import com.traveltime.regionsLocationsTask.Models._
import com.traveltime.regionsLocationsTask.GeographyUtils._
import com.traveltime.regionsLocationsTask.RegionsLocationsUtils._

class GeneralSpec extends AnyFlatSpec with ScalaCheckDrivenPropertyChecks {
  "pointInPolygon" should "return true if the point is inside the polygon" in {
    val polygon = List(
      Coordinate(0, 0),
      Coordinate(0, 10),
      Coordinate(10, 10),
      Coordinate(10, 0)
    )

    val point = Coordinate(5, 5)

    pointInPolygon(point, polygon) shouldEqual true
  }

  it should "return false if the point is outside the polygon" in {
    val polygon = List(
      Coordinate(0, 0),
      Coordinate(0, 10),
      Coordinate(10, 10),
      Coordinate(10, 0)
    )

    val point = Coordinate(15, 15)

    pointInPolygon(point, polygon) shouldEqual false
  }

  it should "return false if the point is on the border of the polygon" in {
    val polygon = List(
      Coordinate(0, 0),
      Coordinate(0, 10),
      Coordinate(10, 10),
      Coordinate(10, 0)
    )

    val point = Coordinate(10, 10)

    pointInPolygon(point, polygon) shouldEqual false
  }

  "matchLocationsWithRegions" should "match locations correctly to regions" in {
    val locations = List(
      Location("Location 1", Coordinate(1, 1)),
      Location("Location 2", Coordinate(3, 3)),
      Location("Location 3", Coordinate(5, 5))
    )

    val regions = List(
      Region(
        "Region 1",
        List(
          Polygon(
            List(
              Coordinate(0, 0),
              Coordinate(0, 2),
              Coordinate(2, 2),
              Coordinate(2, 0)
            )
          )
        )
      ),
      Region(
        "Region 2",
        List(
          Polygon(
            List(
              Coordinate(2, 2),
              Coordinate(2, 4),
              Coordinate(4, 4),
              Coordinate(4, 2)
            )
          )
        )
      )
    )

    val expectedMatches = List(
      RegionWithLocations("Region 1", List("Location 1")),
      RegionWithLocations("Region 2", List("Location 2"))
    )

    matchLocationsWithRegions(regions, locations) shouldEqual expectedMatches
  }

  it should "return empty list when there are no matching locations for a region" in {
    val locations = List(
      Location("Location 1", Coordinate(1, 1)),
      Location("Location 2", Coordinate(3, 3)),
      Location("Location 3", Coordinate(5, 5))
    )

    val regions = List(
      Region(
        "Region 1",
        List(
          Polygon(
            List(
              Coordinate(6, 6),
              Coordinate(6, 8),
              Coordinate(8, 8),
              Coordinate(8, 6)
            )
          )
        )
      )
    )

    val expectedMatches = List(
      RegionWithLocations("Region 1", List())
    )

    matchLocationsWithRegions(regions, locations) shouldEqual expectedMatches
  }

  it should "match multiple locations to a region when they are inside the region" in {
    val locations = List(
      Location("Location 1", Coordinate(1, 1)),
      Location("Location 2", Coordinate(2, 2)),
      Location("Location 3", Coordinate(3, 3))
    )

    val regions = List(
      Region(
        "Region 1",
        List(
          Polygon(
            List(
              Coordinate(0, 0),
              Coordinate(0, 4),
              Coordinate(4, 4),
              Coordinate(4, 0)
            )
          )
        )
      )
    )

    val expectedMatches = List(
      RegionWithLocations(
        "Region 1",
        List("Location 1", "Location 2", "Location 3")
      )
    )

    matchLocationsWithRegions(regions, locations) shouldEqual expectedMatches
  }
}
