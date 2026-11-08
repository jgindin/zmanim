/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zmanlib.location

import com.gindin.zmanlib.location.PlaceData.place
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStream
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class UT_InMemoryPlaceIndex {

  private val countries = mapOf("XA" to "Atlantis")

  private fun TestScope.indexOf(vararg places: String) = indexReading {
    PlaceData.text(countries, *places).byteInputStream()
  }

  private fun TestScope.indexReading(open: () -> InputStream) =
    InMemoryPlaceIndex(open, StandardTestDispatcher(testScheduler))

  @Test
  fun theNearestPlaceIsFound() = runTest {
    val index = indexOf(place("Far", 10.0, 10.0), place("Near", 1.0, 1.0), place("Mid", 5.0, 5.0))

    assertEquals("Near", index.nearest(0.5, 0.5)?.name)
  }

  /** Distance is along the earth, not in degrees: a degree of longitude is short near the poles. */
  @Test
  fun nearnessIsByGreatCircleNotDegrees() = runTest {
    val index =
      indexOf(
        place("Two degrees of latitude away", 62.0, 0.0),
        place("Five degrees of longitude away", 60.0, 5.0),
      )

    // At 60 degrees north, 5 degrees of longitude is about 278 km; 2 degrees of latitude is 222 km.
    //  Counting degrees would pick the other one.
    assertEquals("Two degrees of latitude away", index.nearest(60.0, 0.0)?.name)

    // From 60.0, 1.2 the second is about 211 km away and the first about 231 km, although the
    //  second is further in degrees (3.8 against 2.3).
    assertEquals("Five degrees of longitude away", index.nearest(60.0, 1.2)?.name)
  }

  @Test
  fun theNearestPlaceIsFoundAcrossTheAntimeridian() = runTest {
    val index = indexOf(place("Fiji side", -17.0, 179.5), place("Same side", -17.0, -175.0))

    assertEquals("Fiji side", index.nearest(-17.0, -179.8)?.name)
  }

  @Test
  fun theNearestPlaceIsFoundNearAPole() = runTest {
    val index = indexOf(place("Across the pole", 89.0, 180.0), place("Same meridian", 85.0, 0.0))

    // Two degrees from the first over the pole; four from the second along the meridian.
    assertEquals("Across the pole", index.nearest(89.0, 0.0)?.name)
  }

  @Test
  fun ofTwoEquallyNearPlacesTheOneListedFirstWins() = runTest {
    val index = indexOf(place("Listed first", 0.0, 1.0), place("Listed second", 0.0, -1.0))

    assertEquals("Listed first", index.nearest(0.0, 0.0)?.name)
  }

  @Test
  fun anEmptyIndexHasNoNearestPlaceAndNoNames() = runTest {
    val index = indexOf()

    assertNull(index.nearest(0.0, 0.0))
    assertEquals(emptyList<IndexedPlace>(), index.byName("Anything"))
  }

  @Test
  fun exactNameMatchesComeBeforePrefixMatches() = runTest {
    val index =
      indexOf(
        place("Springfield Gardens", 1.0, 1.0),
        place("Springfield", 2.0, 2.0),
        place("Springfield", 3.0, 3.0),
        place("Spring", 4.0, 4.0),
        place("Albany", 5.0, 5.0),
      )

    assertEquals(
      listOf(2.0, 3.0, 1.0),
      index.byName("Springfield").map { it.latitude },
    )
  }

  @Test
  fun nameMatchingIgnoresCaseAccentsAndApostrophes() = runTest {
    val index = indexOf(place("Ma‘ale Adummim", 1.0, 1.0), place("Ḩalḩūl", 2.0, 2.0))

    assertEquals(listOf("Ma‘ale Adummim"), index.byName("maale adummim").map { it.name })
    assertEquals(listOf("Ma‘ale Adummim"), index.byName("Ma'ale").map { it.name })
    assertEquals(listOf("Ḩalḩūl"), index.byName("HALHUL").map { it.name })
  }

  @Test
  fun hyphensAndExtraSpacesMatchASingleSpace() = runTest {
    val index = indexOf(place("Tel Aviv", 1.0, 1.0))

    assertEquals(listOf("Tel Aviv"), index.byName("  tel-aviv ").map { it.name })
  }

  @Test
  fun blankTextMatchesNothing() = runTest {
    val index = indexOf(place("Albany", 1.0, 1.0))

    assertEquals(emptyList<IndexedPlace>(), index.byName("  "))
  }

  @Test
  fun theDataIsReadOnceAndKept() = runTest {
    var opened = 0
    val index = indexReading {
      opened++
      PlaceData.text(countries, place("Albany", 1.0, 1.0)).byteInputStream()
    }

    index.nearest(0.0, 0.0)
    index.byName("Albany")
    index.nearest(1.0, 1.0)

    assertEquals(1, opened)
  }

  @Test
  fun malformedDataLeavesTheIndexEmptyAndSaysWhy() = runTest {
    val index = indexReading { ByteArrayInputStream("not place data\n".toByteArray()) }

    assertNull(index.nearest(0.0, 0.0))
    assertEquals(emptyList<IndexedPlace>(), index.byName("Albany"))
    assertInstanceOf(PlaceIndexFormatException::class.java, index.loadFailure)
  }

  @Test
  fun dataThatCannotBeReadLeavesTheIndexEmptyAndSaysWhy() = runTest {
    val index = indexReading { throw IOException("no such asset") }

    assertNull(index.nearest(0.0, 0.0))
    assertInstanceOf(IOException::class.java, index.loadFailure)
  }
}
