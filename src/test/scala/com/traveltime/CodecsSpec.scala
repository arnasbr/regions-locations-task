package com.traveltime

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers._
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks
import com.traveltime.regionsLocationsTask.Models._
import com.traveltime.regionsLocationsTask.GeographyUtils._
import com.traveltime.regionsLocationsTask.RegionsLocationsUtils._
import com.traveltime.regionsLocationsTask.Codecs._
import io.circe.parser._
import io.circe.{Decoder, DecodingFailure}

class CodecsSpec extends AnyFlatSpec with ScalaCheckDrivenPropertyChecks {
  "decodeCoordinate" should "decode a JSON array to Coordinate" in {
    val json = parse("[1.0, 2.0]").getOrElse(fail("Parsing failed"))

    val result = Decoder[Coordinate].decodeJson(json)

    result shouldBe Right(Coordinate(1.0, 2.0))
  }

  it should "return an error if the JSON array is not of length 2" in {
    val json = parse("[1.0, 2.0, 3.0]").getOrElse(fail("Parsing failed"))

    val result = Decoder[Coordinate].decodeJson(json)

    result shouldBe a[Left[_, _]]
    result.swap.toOption.get.message should include(
      "Coordinate must be a list of two numbers"
    )
  }

  "decodeLocation" should "decode a JSON object to Location" in {
    val json = parse("""{ "name": "Location 1", "coordinates": [1.0, 2.0] }""")
      .getOrElse(fail("Parsing failed"))

    val result = Decoder[Location].decodeJson(json)

    result shouldBe Right(Location("Location 1", Coordinate(1.0, 2.0)))
  }

  it should "return an error if the JSON object does not have the right fields" in {
    val json = parse("""{ "name": "Location 1" }""")
      .getOrElse(fail("Parsing failed"))

    val result = Decoder[Location].decodeJson(json)

    result shouldBe a[Left[_, _]]
  }

  "decodePolygon" should "successfully decode valid json" in {
    val json = """[[1.0, 2.0], [3.0, 4.0], [5.0, 6.0]]"""
    val expected =
      Right(Polygon(List(Coordinate(1, 2), Coordinate(3, 4), Coordinate(5, 6))))

    decode[Polygon](json) shouldEqual expected
  }

  it should "fail to decode json when coordinates are not in pairs" in {
    val json = """[[1.0, 2.0], [3.0, 4.0, 5.0], [5.0, 6.0]]"""
    val expected =
      Left(DecodingFailure("Coordinates must be a list of two numbers", List()))

    decode[Polygon](json) shouldEqual expected
  }

  it should "fail to decode json when it's not a list" in {
    val json = """{"coordinates": [[1.0, 2.0], [3.0, 4.0], [5.0, 6.0]]}"""

    decode[Polygon](json).isLeft shouldBe true
  }

  "decodeRegion" should "decode a JSON object to Region" in {
    val json = parse(
      """
        |{
        |  "name": "region1",
        |  "coordinates": [
        |    [
        |      [
        |        25.13573603154873,
        |        54.67922829209249
        |      ],
        |      [
        |        25.156131289233258,
        |        54.58478594629585
        |      ],
        |      [
        |        25.286660938416787,
        |        54.5942400514071
        |      ]
        |    ]
        |  ]
        |}
      """.stripMargin
    ).getOrElse(fail("Parsing failed"))

    val result = Decoder[Region].decodeJson(json)

    result shouldBe Right(
      Region(
        "region1",
        List(
          Polygon(
            List(
              Coordinate(25.13573603154873, 54.67922829209249),
              Coordinate(25.156131289233258, 54.58478594629585),
              Coordinate(25.286660938416787, 54.5942400514071)
            )
          )
        )
      )
    )
  }
}
