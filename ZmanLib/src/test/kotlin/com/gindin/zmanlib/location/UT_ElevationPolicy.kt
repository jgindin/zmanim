/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zmanlib.location

import com.gindin.zmanlib.calendar.HebrewCalendar
import com.gindin.zmanlib.zman.Zmanim
import com.gindin.zmanlib.zman.ZmanimFactory
import java.util.Calendar
import java.util.TimeZone
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test

class UT_ElevationPolicy {

  /** A geoid 20 m above the ellipsoid everywhere, as it is around Jerusalem. */
  private val policy = SeaLevelElevationPolicy { _, _ -> GEOID_HEIGHT }

  private fun inputs(
    msl: Double? = null,
    ellipsoid: Double? = null,
    place: Double? = null,
  ) = ElevationInputs(JERUSALEM_LATITUDE, JERUSALEM_LONGITUDE, msl, ellipsoid, place)

  @AfterEach
  fun restoreDefaultElevationSetting() {
    ZmanimFactory.useElevation(false)
  }

  @Test
  fun theSeaLevelAltitudeIsUsedWhenTheFixHasOne() = runTest {
    assertEquals(
      780.0,
      policy.elevationMeters(inputs(msl = 780.0, ellipsoid = 900.0, place = 754.0)),
    )
  }

  @Test
  fun otherwiseTheEllipsoidAltitudeIsCorrectedByTheGeoidHeight() = runTest {
    assertEquals(780.0, policy.elevationMeters(inputs(ellipsoid = 800.0, place = 754.0)))
  }

  @Test
  fun anEllipsoidAltitudeWithNoGeoidHeightThereFallsBackToThePlace() = runTest {
    val noGeoid = SeaLevelElevationPolicy { _, _ -> null }

    assertEquals(754.0, noGeoid.elevationMeters(inputs(ellipsoid = 800.0, place = 754.0)))
  }

  @Test
  fun aFixWithNoAltitudeTakesThePlacesElevation() = runTest {
    assertEquals(754.0, policy.elevationMeters(inputs(place = 754.0)))
  }

  @Test fun nothingKnownIsZero() = runTest { assertEquals(0.0, policy.elevationMeters(inputs())) }

  @Test
  fun anInputThatIsNotANumberIsSkipped() = runTest {
    assertEquals(780.0, policy.elevationMeters(inputs(msl = Double.NaN, ellipsoid = 800.0)))
    assertEquals(
      754.0,
      policy.elevationMeters(inputs(ellipsoid = Double.POSITIVE_INFINITY, place = 754.0)),
    )
    assertEquals(0.0, policy.elevationMeters(inputs(place = Double.NaN)))
    val nanGeoid = SeaLevelElevationPolicy { _, _ -> Double.NaN }
    assertEquals(754.0, nanGeoid.elevationMeters(inputs(ellipsoid = 800.0, place = 754.0)))
  }

  @Test
  fun belowSeaLevelIsZeroAndDoesNotFallBackToThePlace() = runTest {
    // A fix by the Dead Sea, and one at the shore whose GPS altitude is a little low.
    assertEquals(0.0, policy.elevationMeters(inputs(msl = -430.0, place = 754.0)))
    assertEquals(0.0, policy.elevationMeters(inputs(ellipsoid = 17.0, place = 754.0)))
  }

  @Test
  fun theElevationIsIgnoredUnlessTheUserTurnedElevationOn() = runTest {
    val elevation = policy.elevationMeters(inputs(ellipsoid = 800.0))
    val atElevation = jerusalem(elevation)
    val atSeaLevel = jerusalem(0.0)

    ZmanimFactory.useElevation(false)
    assertEquals(shema(atSeaLevel), shema(atElevation))

    ZmanimFactory.useElevation(true)
    assertNotEquals(shema(atSeaLevel), shema(atElevation))
  }

  private fun jerusalem(elevation: Double) =
    ZmanimLocation.Builder()
      .locationName("Jerusalem")
      .atLatitude(JERUSALEM_LATITUDE)
      .atLongitude(JERUSALEM_LONGITUDE)
      .atElevation(elevation)
      .inTimeZone(TimeZone.getTimeZone("Asia/Jerusalem"))
      .build()

  /** The GR'A's latest time for the Shema, which moves with an elevation that is applied. */
  private fun shema(location: ZmanimLocation): Long? =
    ZmanimFactory.get(
        location,
        HebrewCalendar.getForDate(2026, Calendar.JUNE, 15),
        Zmanim.Type.SofZmanShema,
      )
      .asList()
      .first { it.name == "GR'A" && !it.advanced }
      .getTime()
      ?.timeInMillis

  private companion object {
    const val GEOID_HEIGHT = 20.0
    const val JERUSALEM_LATITUDE = 31.7683
    const val JERUSALEM_LONGITUDE = 35.2137
  }
}
