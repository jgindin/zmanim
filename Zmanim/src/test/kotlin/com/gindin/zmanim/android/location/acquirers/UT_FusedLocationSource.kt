/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zmanim.android.location.acquirers

import android.location.Location
import com.gindin.zmanlib.location.LocationQuery
import com.gindin.zmanlib.location.LocationState
import com.gindin.zmanlib.location.SeaLevelElevationPolicy
import com.gindin.zmanlib.location.UnavailableReason
import com.gindin.zmanlib.location.ZmanimLocation
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.LocationRequest
import java.util.TimeZone
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class UT_FusedLocationSource {

  private class FakeFusedClient : FusedClient {
    var last: Location? = null
    var lastFails = false
    var lastAsked = 0

    /** When set, asking for the last fix waits until the caller goes away. */
    var lastHangs = false
    val askingForLast = CompletableDeferred<Unit>()
    var settingsFailure: Exception? = null
    var startFailure: Exception? = null

    /** Delivered as soon as a request starts; null delivers nothing. */
    var fresh: Location? = null
    var started = 0
    var stopped = 0
    val running = CompletableDeferred<Unit>()

    override suspend fun lastLocation(): Location? {
      lastAsked++
      askingForLast.complete(Unit)
      if (lastHangs) kotlinx.coroutines.awaitCancellation()
      if (lastFails) throw IllegalStateException("no last location")
      return last
    }

    override suspend fun checkSettings(request: LocationRequest) {
      settingsFailure?.let { throw it }
    }

    override fun requestUpdates(
      request: LocationRequest,
      onLocation: (Location) -> Unit,
      onError: (Exception) -> Unit,
    ): () -> Unit {
      started++
      running.complete(Unit)
      startFailure?.let(onError) ?: fresh?.let(onLocation)
      return { stopped++ }
    }
  }

  private val client = FakeFusedClient()
  private var grant = LocationGrant.PRECISE
  private var playServices = true
  private var timeZone: TimeZone? = null
  private val saved =
    ZmanimLocation.Builder().locationName("Saved").atLatitude(1.0).atLongitude(2.0).build()

  private fun source(timeout: kotlin.time.Duration = 5.seconds) =
    FusedLocationSource(
      client = client,
      grant = { grant },
      playServicesAvailable = { playServices },
      // A geoid 20 m above the ellipsoid everywhere.
      elevationPolicy = SeaLevelElevationPolicy { _, _ -> GEOID_HEIGHT },
      timeZone = { timeZone },
      savedLocation = { saved },
      now = { NOW },
      timeout = timeout,
    )

  private fun fix(ageMillis: Long = 0, latitude: Double = 31.7683) =
    Location("fused").apply {
      this.latitude = latitude
      longitude = 35.2137
      time = NOW - ageMillis
    }

  private suspend fun collect(
    acceptRecent: Boolean = true,
    timeout: kotlin.time.Duration = 5.seconds,
  ) = source(timeout).locations(LocationQuery(acceptRecent)).toList()

  private fun fixes(states: List<LocationState>) = states.map {
    (it as LocationState.Resolving).fix.latitude
  }

  private fun test(body: suspend CoroutineScope.() -> Unit) = runBlocking {
    withTimeout(TEST_TIME_LIMIT) { body() }
  }

  @Test
  fun withoutLocationAccessTheSavedLocationIsOfferedAndNothingIsAsked() = test {
    grant = LocationGrant.NONE

    assertEquals(
      listOf(LocationState.Unavailable(UnavailableReason.NoPermission, saved)),
      collect(),
    )
    assertEquals(0, client.lastAsked + client.started)
  }

  @Test
  fun withoutPlayServicesTheSavedLocationIsOffered() = test {
    playServices = false

    assertEquals(
      listOf(LocationState.Unavailable(UnavailableReason.NoPlayServices, saved)),
      collect(),
    )
    assertEquals(0, client.started)
  }

  @Test
  fun aRecentLastFixComesFirstThenTheFreshOne() = test {
    client.last = fix(ageMillis = 29.minutes.inWholeMilliseconds, latitude = 10.0)
    client.fresh = fix(latitude = 20.0)

    assertEquals(listOf(10.0, 20.0), fixes(collect()))
  }

  @Test
  fun aStaleLastFixIsNotUsed() = test {
    client.last = fix(ageMillis = 31.minutes.inWholeMilliseconds, latitude = 10.0)
    client.fresh = fix(latitude = 20.0)

    assertEquals(listOf(20.0), fixes(collect()))
  }

  @Test
  fun noLastFixOrAFailureToGetItMeansWaitingForTheFreshOne() = test {
    client.fresh = fix(latitude = 20.0)
    assertEquals(listOf(20.0), fixes(collect()))

    client.lastFails = true
    assertEquals(listOf(20.0), fixes(collect()))
  }

  @Test
  fun aFailureToGetTheLastFixIsKeptForDiagnostics() = test {
    val source = source()
    client.fresh = fix(latitude = 20.0)
    client.lastFails = true
    source.locations(LocationQuery(acceptRecent = true)).toList()
    assertEquals("no last location", source.lastLocationFailure?.message)

    client.lastFails = false
    source.locations(LocationQuery(acceptRecent = true)).toList()
    assertEquals(null, source.lastLocationFailure)
  }

  @Test
  fun aCollectorThatGoesAwayWhileTheLastFixIsAskedForStopsThere() = test {
    client.lastHangs = true
    val collector = launch { source().locations(LocationQuery(acceptRecent = true)).toList() }
    client.askingForLast.await()

    collector.cancel()
    collector.join()

    assertEquals(0, client.started)
  }

  @Test
  fun aQueryThatWantsAFreshFixDoesNotAskForTheLastOne() = test {
    client.last = fix(latitude = 10.0)
    client.fresh = fix(latitude = 20.0)

    assertEquals(listOf(20.0), fixes(collect(acceptRecent = false)))
    assertEquals(0, client.lastAsked)
  }

  @Test
  fun locationTurnedOffOffersTheDialogToTurnItOn() = test {
    val resolvable = ResolvableApiException(Status(CommonStatusCodes.RESOLUTION_REQUIRED))
    client.settingsFailure = resolvable

    val states = collect()

    val unavailable = states.single() as LocationState.Unavailable
    val reason = unavailable.reason as UnavailableReason.ServicesOff
    assertSame(resolvable, (reason.resolution as PlayServicesSettingsResolution).exception)
    assertSame(saved, unavailable.lastKnown)
    assertEquals(0, client.started)
  }

  @Test
  fun settingsThatCannotBeChangedAndARequestThatCannotStartAreFailures() = test {
    val unchangeable = ApiException(Status(CommonStatusCodes.ERROR))
    client.settingsFailure = unchangeable
    assertEquals(
      listOf(LocationState.Unavailable(UnavailableReason.Failed(unchangeable), saved)),
      collect(),
    )

    client.settingsFailure = null
    val refused = SecurityException("no access")
    client.startFailure = refused
    assertEquals(
      listOf(LocationState.Unavailable(UnavailableReason.Failed(refused), saved)),
      collect(),
    )
  }

  @Test
  fun noFreshFixInTimeIsATimeoutAndStopsTheRequest() = test {
    assertEquals(
      listOf(LocationState.Unavailable(UnavailableReason.Timeout, saved)),
      collect(timeout = 50.milliseconds),
    )
    assertEquals(1, client.started)
    assertEquals(1, client.stopped)
  }

  @Test
  fun afterARecentFixAFailureForTheFreshOneLeavesTheRecentFixStanding() = test {
    client.last = fix(latitude = 10.0)
    client.settingsFailure = ResolvableApiException(Status(CommonStatusCodes.RESOLUTION_REQUIRED))
    assertEquals(listOf(10.0), fixes(collect()))

    client.settingsFailure = null
    assertEquals(listOf(10.0), fixes(collect(timeout = 50.milliseconds)))
  }

  @Test
  fun aCollectorThatGoesAwayStopsTheRequest() = test {
    val collector = launch { source().locations(LocationQuery(acceptRecent = false)).toList() }
    client.running.await()

    collector.cancel()
    collector.join()

    assertEquals(1, client.started)
    assertEquals(1, client.stopped)
  }

  @Test
  fun aFixUnderAnApproximateGrantIsMarkedApproximate() = test {
    grant = LocationGrant.APPROXIMATE
    client.last = fix(latitude = 10.0)
    client.fresh = fix(latitude = 20.0)

    val states = collect().map { it as LocationState.Resolving }

    assertEquals(listOf(true, true), states.map { it.approximate })
    grant = LocationGrant.PRECISE
    assertEquals(
      listOf(false, false),
      collect().map { (it as LocationState.Resolving).approximate },
    )
  }

  @Test
  fun theFixKeepsItsCoordinatesAccuracyProviderAndTimeInTheUsersTimeZone() = test {
    timeZone = TimeZone.getTimeZone("Asia/Jerusalem")
    client.fresh = fix(ageMillis = 2_000).apply { accuracy = 8f }

    val location = (collect(acceptRecent = false).single() as LocationState.Resolving).fix

    assertEquals(31.7683, location.latitude, 0.0)
    assertEquals(35.2137, location.longitude, 0.0)
    assertEquals(8f, location.accuracy)
    assertEquals("fused", location.providerName)
    assertEquals(NOW - 2_000, location.locationTime)
    assertEquals(timeZone, location.timeZone)
    assertTrue(location.originalUserName.isNullOrEmpty())
  }

  @Test
  fun theElevationIsTheSeaLevelAltitudeOrTheCorrectedGpsAltitude() = test {
    client.fresh =
      fix().apply {
        altitude = 900.0
        mslAltitudeMeters = 780.0
      }
    assertEquals(780.0, elevationOfFreshFix(), 0.0)

    client.fresh = fix().apply { altitude = 800.0 }
    assertEquals(780.0, elevationOfFreshFix(), 0.0)

    client.fresh = fix()
    assertEquals(0.0, elevationOfFreshFix(), 0.0)
  }

  @Test
  @Config(sdk = [33])
  fun beforeApi34TheGpsAltitudeIsCorrected() = test {
    client.fresh = fix().apply { altitude = 800.0 }

    assertEquals(780.0, elevationOfFreshFix(), 0.0)
  }

  private suspend fun elevationOfFreshFix() =
    (collect(acceptRecent = false).single() as LocationState.Resolving).fix.elevation

  private companion object {
    const val NOW = 1_800_000_000_000L
    const val GEOID_HEIGHT = 20.0
    val TEST_TIME_LIMIT = 10.seconds
  }
}
