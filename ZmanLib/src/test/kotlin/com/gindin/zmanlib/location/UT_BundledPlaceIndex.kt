/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zmanlib.location

import java.io.File
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

/** Checks the place data the app bundles (decision D2: Israeli places are shown in Israel). */
class UT_BundledPlaceIndex {

  private val asset = File(System.getProperty("zmanim.placesAsset"))

  private fun TestScope.bundledIndex() =
    InMemoryPlaceIndex({ asset.inputStream() }, StandardTestDispatcher(testScheduler))

  @Test
  fun theBundledDataReads() = runTest {
    val index = bundledIndex()

    index.nearest(0.0, 0.0)

    assertNull(index.loadFailure)
  }

  @Test
  fun wellKnownIsraeliCitiesAreInIsrael() = runTest {
    val index = bundledIndex()

    for (city in listOf("Jerusalem", "Tel Aviv", "Haifa", "Beersheba", "Eilat")) {
      assertEquals("Israel", index.byName(city).first().country, city)
    }
  }

  @Test
  fun theLargestPlaceOfAGivenNameComesFirst() = runTest {
    assertEquals(31.76904, bundledIndex().byName("Jerusalem").first().latitude)
  }

  /** GeoNames assigns the Old City to PS; the override file shows it in Jerusalem, Israel. */
  @Test
  fun theWesternWallIsInJerusalemIsrael() = runTest {
    val place = bundledIndex().nearest(31.7767, 35.2345)

    assertEquals("Old City, Jerusalem", place?.name)
    assertEquals("Israel", place?.country)
    assertEquals("Asia/Jerusalem", place?.timeZoneId)
  }

  @Test
  fun citiesNamedByTheOverridesKeepTheirNewNames() = runTest {
    val index = bundledIndex()

    for (name in listOf("Beitar Illit", "Hevron", "Shechem", "Beit Lechem")) {
      assertEquals(name, index.byName(name).firstOrNull()?.name, name)
      assertEquals("Asia/Jerusalem", index.byName(name).first().timeZoneId, name)
    }
  }
}
