/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zmanlib.location

import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.io.File
import java.io.IOException
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class UT_BundledGeoidGrid {

  /**
   * A grid in the bundled format: [rows] rows from 90° N, [columns] columns from 0° E, holding
   * [decimeters] row by row.
   */
  private fun grid(
    rows: Int,
    columns: Int,
    decimeters: List<Int>,
    header: String = "zmanim-geoid\t1\t$rows\t$columns",
    source: String = "# A synthetic grid",
  ): ByteArray {
    val out = ByteArrayOutputStream()
    out.write("$header\n$source\n".toByteArray(Charsets.UTF_8))
    DataOutputStream(out).use { data -> decimeters.forEach { data.writeShort(it) } }
    return out.toByteArray()
  }

  private fun TestScope.geoid(bytes: ByteArray) =
    BundledGeoidGrid({ bytes.inputStream() }, StandardTestDispatcher(testScheduler))

  // Three rows (90 N, 0, 90 S) of four columns (0, 90 E, 180, 90 W), in decimeters.
  private val small = grid(3, 4, listOf(10, 10, 10, 10, 0, 100, 200, 300, -50, -50, -50, -50))

  @Nested
  inner class Heights {

    @Test
    fun aNodeHasItsOwnHeight() = runTest {
      val geoid = geoid(small)

      assertEquals(10.0, geoid.heightMeters(0.0, 90.0)!!, DELTA)
      assertEquals(20.0, geoid.heightMeters(0.0, 180.0)!!, DELTA)
      assertEquals(-5.0, geoid.heightMeters(-90.0, 0.0)!!, DELTA)
      assertEquals(1.0, geoid.heightMeters(90.0, 270.0)!!, DELTA)
    }

    @Test
    fun betweenNodesTheHeightIsInterpolatedInBothDirections() = runTest {
      val geoid = geoid(small)

      // Halfway between 90 E (10 m) and 180 (20 m) on the equator.
      assertEquals(15.0, geoid.heightMeters(0.0, 135.0)!!, DELTA)
      // A quarter of the way from the equator (10 m) to 90 N (1 m) at 90 E.
      assertEquals(7.75, geoid.heightMeters(22.5, 90.0)!!, DELTA)
      // Halfway in both: the mean of 1, 1, 10 and 20 m.
      assertEquals(8.0, geoid.heightMeters(45.0, 135.0)!!, DELTA)
    }

    @Test
    fun westernLongitudesAndTheMeridianWrapAround() = runTest {
      val geoid = geoid(small)

      // 90 W is the fourth column (30 m); halfway from it to 0 (0 m) is 45 W.
      assertEquals(30.0, geoid.heightMeters(0.0, -90.0)!!, DELTA)
      assertEquals(15.0, geoid.heightMeters(0.0, -45.0)!!, DELTA)
      assertEquals(15.0, geoid.heightMeters(0.0, 315.0)!!, DELTA)
      assertEquals(geoid.heightMeters(0.0, 0.0), geoid.heightMeters(0.0, 360.0))
      // 180 W is 180 E.
      assertEquals(20.0, geoid.heightMeters(0.0, -180.0)!!, DELTA)
    }

    @Test
    fun coordinatesThatAreNotOnEarthHaveNoHeight() = runTest {
      val geoid = geoid(small)

      assertNull(geoid.heightMeters(Double.NaN, 0.0))
      assertNull(geoid.heightMeters(0.0, Double.POSITIVE_INFINITY))
      assertNull(geoid.heightMeters(90.5, 0.0))
      assertNull(geoid.heightMeters(-91.0, 0.0))
    }
  }

  @Nested
  inner class UnreadableGrids {

    private suspend fun TestScope.assertUnreadable(bytes: ByteArray) {
      val geoid = geoid(bytes)
      assertNull(geoid.heightMeters(0.0, 0.0))
      assertNotNull(geoid.loadFailure)
    }

    @Test
    fun theWrongFormatOrVersionIsRefused() = runTest {
      assertUnreadable(grid(3, 4, List(12) { 0 }, header = "zmanim-places\t1\t3\t4"))
      assertUnreadable(grid(3, 4, List(12) { 0 }, header = "zmanim-geoid\t2\t3\t4"))
      // One row, or no columns, cannot be interpolated, however many heights follow.
      assertUnreadable(grid(1, 4, List(4) { 0 }))
      assertUnreadable(grid(3, 0, emptyList()))
      assertUnreadable(grid(3, 4, List(12) { 0 }, source = "no source line"))
    }

    @Test
    fun aGridCutShortOrTooLongIsRefused() = runTest {
      assertUnreadable(grid(3, 4, List(11) { 0 }))
      assertUnreadable(grid(3, 4, List(13) { 0 }))
      assertUnreadable("zmanim-geoid\t1\t3".toByteArray())
    }

    @Test
    fun aGridThatCannotBeOpenedHasNoHeightsAndIsNotOpenedAgain() = runTest {
      var opens = 0
      val geoid =
        BundledGeoidGrid(
          {
            opens++
            throw IOException("no asset")
          },
          StandardTestDispatcher(testScheduler),
        )

      assertNull(geoid.heightMeters(0.0, 0.0))
      assertNull(geoid.heightMeters(10.0, 10.0))
      assertEquals(1, opens)
      assertEquals("no asset", geoid.loadFailure?.message)
    }
  }

  @Nested
  inner class TheBundledGrid {

    private val asset = File(System.getProperty("zmanim.geoidAsset"))

    /**
     * The heights interpolated from the whole-degree nodes of GeographicLib's 5-minute EGM2008
     * grid. They are within 2 m of the full model's (Jerusalem 19.76, London 46.09, New York
     * -32.74, La Paz 45.64, Melbourne 4.63); the bundled grid rounds each node to a decimeter.
     */
    @Test
    fun wellKnownPlacesHaveTheirGeoidHeight() = runTest {
      val geoid = BundledGeoidGrid({ asset.inputStream() }, StandardTestDispatcher(testScheduler))

      assertEquals(19.83, geoid.heightMeters(31.7683, 35.2137)!!, ROUNDING) // Jerusalem
      assertEquals(46.18, geoid.heightMeters(51.5074, -0.1278)!!, ROUNDING) // London
      assertEquals(-32.94, geoid.heightMeters(40.7128, -74.0060)!!, ROUNDING) // New York
      assertEquals(43.63, geoid.heightMeters(-16.4897, -68.1193)!!, ROUNDING) // La Paz
      assertEquals(4.40, geoid.heightMeters(-37.8136, 144.9631)!!, ROUNDING) // Melbourne
      assertNull(geoid.loadFailure)
    }
  }

  private companion object {
    const val DELTA = 1e-9
    const val ROUNDING = 0.06
  }
}
