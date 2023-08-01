package com.traveltime.regionsLocationsTask
import Models._

object GeographyUtils {
  def pointInPolygon(point: Coordinate, polygon: List[Coordinate]): Boolean = {
    // Create pairs of consecutive points from the polygon
    val pointPairs = polygon.sliding(2).toList

    // Calculate winding numbers
    val windingNumber = pointPairs.map {
      case List(a, b)
          if a.y <= point.y && b.y > point.y && isLeft(a, b, point) > 0 =>
        1
      case List(a, b)
          if a.y > point.y && b.y <= point.y && isLeft(a, b, point) < 0 =>
        -1
      case _ => 0
    }.sum

    windingNumber != 0
  }

  private def isLeft(a: Coordinate, b: Coordinate, c: Coordinate): Double = {
    (b.x - a.x) * (c.y - a.y) - (c.x - a.x) * (b.y - a.y)
  }
}
