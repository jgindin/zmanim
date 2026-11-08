/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zmanlib.location

import com.gindin.zmanlib.location.PlaceData.place
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class UT_PlaceIndexFormat {

  private val countries = mapOf("XA" to "Atlantis", "XB" to "Lemuria")

  private fun read(text: String) = PlaceIndexFormat.read(text.lineSequence())

  @Test
  fun aPlaceIsReadWithItsCountryNameElevationTimeZoneAndFlag() {
    val places =
      read(
        PlaceData.text(
          countries,
          place("Poseidonis", 31.5, -35.25, country = "XB", elevation = "786", flags = "1"),
        )
      )

    assertEquals(
      listOf(IndexedPlace("Poseidonis", "Lemuria", 31.5, -35.25, 786.0, "UTC", true)),
      places,
    )
  }

  @Test
  fun anEmptyElevationIsUnknown() {
    val places = read(PlaceData.text(countries, place("A", 1.0, 2.0)))

    assertEquals(null, places.single().elevationMeters)
  }

  @Test
  fun placesKeepTheDataOrder() {
    val places =
      read(PlaceData.text(countries, place("Second", 1.0, 1.0), place("First", 0.0, 0.0)))

    assertEquals(listOf("Second", "First"), places.map { it.name })
  }

  @Test
  fun anotherFormatOrVersionIsRefused() {
    assertThrows<PlaceIndexFormatException> {
      read(PlaceData.text(countries).replace(PlaceData.HEADER, "zmanim-places\t2"))
    }
  }

  @Test
  fun emptyDataIsRefused() {
    assertThrows<PlaceIndexFormatException> { read("") }
  }

  @Test
  fun aPlaceInAnUnlistedCountryIsRefused() {
    assertThrows<PlaceIndexFormatException> {
      read(PlaceData.text(countries, place("A", 1.0, 2.0, country = "ZZ")))
    }
  }

  @Test
  fun aPlaceWithMissingFieldsIsRefused() {
    assertThrows<PlaceIndexFormatException> {
      read(PlaceData.text(countries, "P\tA\tXA\t1.0\t2.0"))
    }
  }

  @Test
  fun coordinatesThatAreNotNumbersAreRefused() {
    assertThrows<PlaceIndexFormatException> {
      read(PlaceData.text(countries, "P\tA\tXA\tnorth\t2.0\t\tUTC\t0"))
    }
  }

  @Test
  fun coordinatesOutOfRangeAreRefused() {
    assertThrows<PlaceIndexFormatException> {
      read(PlaceData.text(countries, place("A", 91.0, 2.0)))
    }
    assertThrows<PlaceIndexFormatException> {
      read(PlaceData.text(countries, place("A", 1.0, 180.5)))
    }
  }

  @Test
  fun anUnknownRecordTypeIsRefused() {
    assertThrows<PlaceIndexFormatException> { read(PlaceData.text(countries, "R\tsomething")) }
  }

  @Test
  fun unknownFlagsAreRefused() {
    assertThrows<PlaceIndexFormatException> {
      read(PlaceData.text(countries, place("A", 1.0, 2.0, flags = "yes")))
    }
  }
}
