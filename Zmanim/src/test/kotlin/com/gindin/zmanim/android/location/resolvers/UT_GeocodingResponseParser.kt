/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zmanim.android.location.resolvers

import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class UT_GeocodingResponseParser {

  private val reverseGeocode = """
    {
      "place_id": 240109189,
      "lat": "47.6038321",
      "lon": "-122.3300624",
      "display_name": "Seattle, King County, Washington, United States",
      "address": {
        "city": "Seattle",
        "state": "Washington",
        "country_code": "us"
      },
      "boundingbox": ["47.4919119", "47.7341388", "-122.4596959", "-122.2244331"]
    }
  """.trimIndent()

  private val searchResults = """
    [
      {"lat": "31.7788242", "lon": "35.2257626", "display_name": "Jerusalem, Israel"},
      {"lat": "39.0", "lon": "-91.0", "display_name": "Jerusalem, Missouri"}
    ]
  """.trimIndent()


  @Test
  fun anObjectIsReturnedAsIs() {

    val result = GeocodingResponseParser.parse(reverseGeocode)

    assertNotNull(result)
    assertEquals("47.6038321", result!!["lat"]!!.jsonPrimitive.content)
    assertEquals("Seattle", result["address"]!!.jsonObject["city"]!!.jsonPrimitive.content)
  }


  @Test
  fun anArrayYieldsItsFirstObject() {

    val result = GeocodingResponseParser.parse(searchResults)

    assertEquals("Jerusalem, Israel", result!!["display_name"]!!.jsonPrimitive.content)
  }


  @Test
  fun anEmptyArrayMeansNoMatch() {

    assertNull(GeocodingResponseParser.parse("[]"))
    assertNull(GeocodingResponseParser.parse("  [ ]  "))
  }


  @Test
  fun aResponseThatIsNeitherObjectNorArrayMeansNoMatch() {

    assertNull(GeocodingResponseParser.parse("null"))
    assertNull(GeocodingResponseParser.parse("42"))
    assertNull(GeocodingResponseParser.parse("true"))
    assertNull(GeocodingResponseParser.parse("\"no results\""))
  }


  @Test
  fun anEmptyObjectIsStillAnObject() {

    assertEquals(JsonObject(emptyMap()), GeocodingResponseParser.parse("{}"))
  }


  @Test
  fun unquotedValuesAreAccepted() {

    val result = GeocodingResponseParser.parse("{status: OK, \"lat\": 1.5}")

    assertEquals("OK", result!!["status"]!!.jsonPrimitive.content)
    assertEquals("1.5", result["lat"]!!.jsonPrimitive.content)
  }


  @Test
  fun anEmptyResponseFailsToParse() {

    assertThrows(SerializationException::class.java) { GeocodingResponseParser.parse("") }
    assertThrows(SerializationException::class.java) { GeocodingResponseParser.parse("   ") }
  }


  @Test
  fun aTruncatedResponseFailsToParse() {

    assertThrows(SerializationException::class.java) {
      GeocodingResponseParser.parse(reverseGeocode.substring(0, reverseGeocode.length / 2))
    }
    assertThrows(SerializationException::class.java) {
      GeocodingResponseParser.parse(searchResults.substring(0, searchResults.length - 5))
    }
  }


  @Test
  fun anHtmlErrorPageFailsToParse() {

    assertThrows(SerializationException::class.java) {
      GeocodingResponseParser.parse("<html><body>502 Bad Gateway</body></html>")
    }
  }


  @Test
  fun anArrayWhoseFirstElementIsNotAnObjectFailsToParse() {

    assertThrows(SerializationException::class.java) { GeocodingResponseParser.parse("[1, 2]") }
    assertThrows(SerializationException::class.java) { GeocodingResponseParser.parse("[null]") }
    assertThrows(SerializationException::class.java) { GeocodingResponseParser.parse("[[{}]]") }
  }

}
