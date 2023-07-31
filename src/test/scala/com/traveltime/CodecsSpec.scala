package com.traveltime

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers._
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks
import com.traveltime.regionsLocationsTask.Models._
import com.traveltime.regionsLocationsTask.GeographyUtils._
import com.traveltime.regionsLocationsTask.RegionsLocationsUtils._
import com.traveltime.regionsLocationsTask.Codecs._
import io.circe.parser._
import io.circe.Decoder

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
}
