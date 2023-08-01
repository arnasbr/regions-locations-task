package com.traveltime.regionsLocationsTask
import Models._

object GeographyUtils {
  // This method determines whether a given point is inside a polygon using the winding number algorithm
  def pointInPolygon(point: Coordinate, polygon: List[Coordinate]): Boolean = {
    // The first step is to create pairs of consecutive points from the polygon.
    // The sliding function takes the polygon (which is a list of coordinates) and
    // generates a new list of lists, where each inner list contains two consecutive points.
    val pointPairs = polygon.sliding(2).toList

    // The winding number is calculated by traversing the polygon in a counterclockwise direction
    // and summing up the number of times the polygon winds around the point.
    // If this number is nonzero, it means that the point is inside the polygon.
    val windingNumber = pointPairs.map {
      // If the point lies between the y-coordinates of a and b, and the point lies to the
      // left of the line segment ab (which is determined by the isLeft function),
      // then we increment the winding number
      case List(a, b)
          if a.y <= point.y && b.y > point.y && isLeft(a, b, point) > 0 =>
        1
      // If the point lies between the y-coordinates of a and b, and the point lies to the
      // right of the line segment ab (which is determined by the isLeft function),
      // then we decrement the winding number
      case List(a, b)
          if a.y > point.y && b.y <= point.y && isLeft(a, b, point) < 0 =>
        -1
      // Otherwise, the winding number does not change
      case _ => 0
    }.sum

    // If the winding number is nonzero, the point is inside the polygon
    windingNumber != 0
  }

  // This helper function calculates the cross product of the vectors ab and ac.
  // If the result is positive, c lies to the left of the line segment ab.
  // If it's negative, c lies to the right. If it's 0, a, b, and c are collinear.
  private def isLeft(a: Coordinate, b: Coordinate, c: Coordinate): Double = {
    (b.x - a.x) * (c.y - a.y) - (c.x - a.x) * (b.y - a.y)
  }
}
