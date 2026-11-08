/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zmanim.android.location.resolvers

import android.content.Context
import android.location.Address
import android.location.Geocoder
import androidx.test.core.app.ApplicationProvider
import com.gindin.zmanlib.location.CoordinatesResult
import com.gindin.zmanlib.location.PlaceResult
import com.gindin.zmanlib.location.ZmanimLocation
import java.util.Locale
import java.util.TimeZone
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowGeocoder

/**
 * The production chain from [PlaceResolution.create]: its sources and their order, with
 * Robolectric's geocoder and the app's real bundled place data.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33])
class UT_PlaceResolution {

  private val context: Context = ApplicationProvider.getApplicationContext()

  @Before
  fun setUp() {
    PlaceResolution.forgetShared()
  }

  @After
  fun tearDown() {
    PlaceResolution.forgetShared()
    ShadowGeocoder.reset()
  }

  private fun test(body: suspend CoroutineScope.() -> Unit) = runBlocking {
    withTimeout(30.seconds) { body() }
  }

  /** The chain with Robolectric's geocoder, answering every lookup from [addresses]. */
  private fun resolution(vararg addresses: Address): PlaceResolution {
    val geocoder = Geocoder(context)
    shadowOf(geocoder).setFromLocation(addresses.toList())
    return PlaceResolution.create(context, geocoder)
  }

  @Test
  fun withoutAnAnswerFromTheGeocoderTheBundledDataNamesATypedPlace() = test {
    val result = resolution().chain.fromName(typed("Jerusalem"))

    val found = (result as PlaceResult.Found).location
    assertEquals("Jerusalem, Israel", found.locationName)
    assertEquals(NEW_YORK, found.timeZone)
  }

  @Test
  fun withoutAGeocoderTheBundledDataStillNamesAFix() = test {
    ShadowGeocoder.setIsPresent(false)

    val result = PlaceResolution.create(context).chain.fromCoordinates(fix(31.7767, 35.2345))

    assertEquals(
      "Old City, Jerusalem, Israel",
      (result as CoordinatesResult.Named).location.locationName,
    )
  }

  @Test
  fun theGeocoderIsAskedBeforeTheBundledData() = test {
    val resolution = resolution(address("Yerushalayim", "Jerusalem District", 31.78, 35.22))

    val result = resolution.chain.fromName(typed("Jerusalem"))

    assertEquals(
      "Yerushalayim, Jerusalem District",
      (result as PlaceResult.Found).location.locationName,
    )
  }

  @Test
  fun theCacheIsAskedFirst() = test {
    val resolution = resolution(address("Seattle", "Washington", 47.6062, -122.3321))
    val albany =
      ZmanimLocation.Builder()
        .locationName("Albany, New York")
        .atLatitude(42.6526)
        .atLongitude(-73.7562)
        .inTimeZone(NEW_YORK)
        .build()
    resolution.cache.store(albany)

    val result = resolution.chain.fromCoordinates(fix(42.6530, -73.7562))

    assertEquals("Albany, New York", (result as CoordinatesResult.Named).location.locationName)
  }

  @Test
  fun everyChainSharesOneCache() {
    assertSame(PlaceResolution.create(context).cache, PlaceResolution.create(context).cache)
  }

  private companion object {
    val NEW_YORK: TimeZone = TimeZone.getTimeZone("America/New_York")

    fun typed(name: String): ZmanimLocation =
      ZmanimLocation.Builder().locationName(name).inTimeZone(NEW_YORK).build()

    fun fix(latitude: Double, longitude: Double): ZmanimLocation =
      ZmanimLocation.Builder()
        .atLatitude(latitude)
        .atLongitude(longitude)
        .inTimeZone(NEW_YORK)
        .build()

    fun address(locality: String, adminArea: String, latitude: Double, longitude: Double) =
      Address(Locale.US).apply {
        this.locality = locality
        this.adminArea = adminArea
        this.latitude = latitude
        this.longitude = longitude
      }
  }
}
