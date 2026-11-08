/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zmanim.android.location.resolvers

import android.location.Address
import android.location.Geocoder
import com.gindin.zmanlib.location.PlaceResult
import com.gindin.zmanlib.location.ZmanimLocation
import java.io.IOException
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.withTimeoutOrNull
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyDouble
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.doAnswer
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoInteractions
import org.mockito.Mockito.`when`
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * What [SystemGeocoder] makes of the system geocoder's answers, through the listener overloads (API
 * 33 and later) and the blocking ones (API 24 to 32). The geocoder is a final framework class, so a
 * Mockito mock stands in for it.
 */
// The blocking overloads are deprecated from API 33, and are the ones API 24 to 32 devices have.
@Suppress("DEPRECATION")
@RunWith(RobolectricTestRunner::class)
class UT_SystemGeocoder {

  private lateinit var geocoder: Geocoder

  @Before
  fun setUp() {
    geocoder = mock(Geocoder::class.java)
  }

  /** Fails rather than hangs when a lookup never returns. */
  private fun test(body: suspend CoroutineScope.() -> Unit) = runBlocking {
    withTimeout(TEST_TIME_LIMIT) { body() }
  }

  private fun resolver(present: Boolean = true) = SystemGeocoder(geocoder, isPresent = { present })

  // --- API 33 and later: the listener overloads.

  @Test
  @Config(sdk = [33])
  fun listenerNamesTheFixAndKeepsItsCoordinates() = test {
    answerCoordinates { it.onGeocode(mutableListOf(seattle(), tacoma())) }

    val result = resolver().fromCoordinates(deviceFix())

    val location = (result as PlaceResult.Found).location
    assertEquals("Seattle, Washington", location.locationName)
    assertFixKept(location)
  }

  @Test
  @Config(sdk = [33])
  fun listenerFindsATypedPlace() = test {
    doAnswer { (it.arguments[2] as Geocoder.GeocodeListener).onGeocode(mutableListOf(seattle())) }
      .`when`(geocoder)
      .getFromLocationName(anyString(), anyInt(), any(Geocoder.GeocodeListener::class.java))

    val result = resolver().fromName(typed("seattle"))

    assertTypedSeattle(result)
  }

  @Test
  @Config(sdk = [33])
  fun listenerWithNoAddressesIsNotFound() = test {
    answerCoordinates { it.onGeocode(mutableListOf()) }

    assertEquals(PlaceResult.NotFound, resolver().fromCoordinates(deviceFix()))
  }

  @Test
  @Config(sdk = [33])
  fun listenerErrorIsAFailure() = test {
    answerCoordinates { it.onError("no network") }

    val result = resolver().fromCoordinates(deviceFix())

    val cause = (result as PlaceResult.Failed).cause
    assertTrue(cause is IOException)
    assertEquals("no network", cause.message)
  }

  @Test
  @Config(sdk = [33])
  fun listenerThatNeverAnswersStopsWhenTheCallerGivesUp() = test {
    answerCoordinates {}

    assertNull(withTimeoutOrNull(GIVE_UP) { resolver().fromCoordinates(deviceFix()) })
  }

  // --- API 24 to 32: the blocking overloads, off the caller's thread.

  @Test
  @Config(sdk = [24])
  fun blockingCallNamesTheFixAndKeepsItsCoordinates() = test {
    `when`(geocoder.getFromLocation(anyDouble(), anyDouble(), anyInt()))
      .thenReturn(listOf(seattle(), tacoma()))

    val result = resolver().fromCoordinates(deviceFix())

    val location = (result as PlaceResult.Found).location
    assertEquals("Seattle, Washington", location.locationName)
    assertFixKept(location)
  }

  @Test
  @Config(sdk = [24])
  fun blockingCallFindsATypedPlace() = test {
    `when`(geocoder.getFromLocationName("seattle", 5)).thenReturn(listOf(seattle()))

    assertTypedSeattle(resolver().fromName(typed("seattle")))
  }

  @Test
  @Config(sdk = [24])
  fun blockingCallWithNoAddressesIsNotFound() = test {
    `when`(geocoder.getFromLocationName(anyString(), anyInt())).thenReturn(null)

    assertEquals(PlaceResult.NotFound, resolver().fromName(typed("nowhere")))
  }

  @Test
  @Config(sdk = [24])
  fun blockingIOExceptionIsAFailure() = test {
    `when`(geocoder.getFromLocation(anyDouble(), anyDouble(), anyInt()))
      .thenThrow(IOException("no network"))

    val result = resolver().fromCoordinates(deviceFix())

    assertTrue((result as PlaceResult.Failed).cause is IOException)
  }

  @Test
  @Config(sdk = [24])
  fun blockingCallThatHangsStopsWhenTheCallerGivesUp() {
    val release = CountDownLatch(1)
    `when`(geocoder.getFromLocation(anyDouble(), anyDouble(), anyInt())).thenAnswer {
      release.await(1, TimeUnit.MINUTES)
      listOf(seattle())
    }
    val executor = Executors.newSingleThreadExecutor()
    val geocoderOnItsOwnThread =
      SystemGeocoder(geocoder, executor.asCoroutineDispatcher(), isPresent = { true })

    try {
      val started = System.nanoTime()
      val result = runBlocking {
        withTimeoutOrNull(GIVE_UP) { geocoderOnItsOwnThread.fromCoordinates(deviceFix()) }
      }

      assertNull(result)
      assertTrue(System.nanoTime() - started < TEST_TIME_LIMIT.inWholeNanoseconds)
    } finally {
      release.countDown()
      executor.shutdown()
    }
  }

  // --- No geocoder on the device.

  @Test
  @Config(sdk = [33])
  fun noGeocoderIsNotFoundWithoutAsking() = test {
    assertEquals(PlaceResult.NotFound, resolver(present = false).fromCoordinates(deviceFix()))
    assertEquals(PlaceResult.NotFound, resolver(present = false).fromName(typed("seattle")))
    verifyNoInteractions(geocoder)
  }

  @Test
  @Config(sdk = [24])
  fun noGeocoderIsNotFoundWithoutAskingOnOlderDevices() = test {
    assertEquals(PlaceResult.NotFound, resolver(present = false).fromName(typed("seattle")))
    verify(geocoder, never()).getFromLocationName(anyString(), anyInt())
  }

  private fun answerCoordinates(answer: (Geocoder.GeocodeListener) -> Unit) {
    doAnswer { answer(it.arguments[3] as Geocoder.GeocodeListener) }
      .`when`(geocoder)
      .getFromLocation(
        anyDouble(),
        anyDouble(),
        anyInt(),
        any(Geocoder.GeocodeListener::class.java),
      )
  }

  private fun assertFixKept(location: ZmanimLocation) {
    assertEquals(FIX_LATITUDE, location.latitude, 0.0)
    assertEquals(FIX_LONGITUDE, location.longitude, 0.0)
    assertEquals(FIX_ELEVATION, location.elevation, 0.0)
    assertEquals("fused", location.providerName)
    assertEquals(CHICAGO, location.timeZone)
  }

  private fun assertTypedSeattle(result: PlaceResult) {
    val location = (result as PlaceResult.Found).location
    assertEquals("Seattle, Washington", location.locationName)
    assertEquals("seattle", location.originalUserName)
    assertEquals(SEATTLE_LATITUDE, location.latitude, 0.0)
    assertEquals(SEATTLE_LONGITUDE, location.longitude, 0.0)
    assertEquals(CHICAGO, location.timeZone)
  }

  private companion object {
    val CHICAGO: TimeZone = TimeZone.getTimeZone("America/Chicago")
    val GIVE_UP = 200.milliseconds
    val TEST_TIME_LIMIT = 10.seconds
    const val FIX_LATITUDE = 47.62
    const val FIX_LONGITUDE = -122.33
    const val FIX_ELEVATION = 56.0
    const val SEATTLE_LATITUDE = 47.6062
    const val SEATTLE_LONGITUDE = -122.3321

    fun deviceFix(): ZmanimLocation =
      ZmanimLocation.Builder()
        .fromProvider("fused")
        .atLatitude(FIX_LATITUDE)
        .atLongitude(FIX_LONGITUDE)
        .atElevation(FIX_ELEVATION)
        .inTimeZone(CHICAGO)
        .build()

    fun typed(name: String): ZmanimLocation =
      ZmanimLocation.Builder().locationName(name).inTimeZone(CHICAGO).build()

    fun seattle(): Address =
      Address(Locale.US).apply {
        locality = "Seattle"
        adminArea = "Washington"
        latitude = SEATTLE_LATITUDE
        longitude = SEATTLE_LONGITUDE
      }

    fun tacoma(): Address =
      Address(Locale.US).apply {
        locality = "Tacoma"
        adminArea = "Washington"
        latitude = 47.25
        longitude = -122.44
      }
  }
}
