/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zmanim.android.location.acquirers

import com.gindin.zmanim.android.location.LocationError
import com.gindin.zmanlib.location.LocationQuery
import com.gindin.zmanlib.location.LocationSource
import com.gindin.zmanlib.location.LocationState
import com.gindin.zmanlib.location.SettingsResolution
import com.gindin.zmanlib.location.UnavailableReason
import com.gindin.zmanlib.location.ZmanimLocation
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.common.api.Status
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class UT_DeviceLocationBridge {

  private sealed interface Report {
    data class Acquired(val location: ZmanimLocation) : Report

    data class NotAcquired(val error: LocationError, val lastKnown: ZmanimLocation?) : Report
  }

  private val reports = mutableListOf<Report>()
  private val saved = mutableListOf<ZmanimLocation>()
  private val queries = mutableListOf<LocationQuery>()
  private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

  private val sink =
    object : DeviceLocationSink {
      override fun acquired(location: ZmanimLocation) {
        synchronized(reports) { reports += Report.Acquired(location) }
      }

      override fun notAcquired(error: LocationError, lastKnown: ZmanimLocation?) {
        synchronized(reports) { reports += Report.NotAcquired(error, lastKnown) }
      }
    }

  private fun bridge(locations: (LocationQuery) -> Flow<LocationState>) =
    DeviceLocationBridge(
      LocationSource { query ->
        queries += query
        locations(query)
      },
      sink,
      save = { saved += it },
      savedLocation = { SAVED },
      scope = scope,
    )

  /** Runs one request to its end and returns what was reported. */
  private fun run(acceptRecent: Boolean, vararg states: LocationState): List<Report> {
    val bridge = bridge { flowOf(*states) }
    bridge.start(acceptRecent)
    bridge.awaitCompletion()
    return reports.toList()
  }

  @After
  fun stopTheScope() {
    scope.cancel()
  }

  @Test
  fun theQueryAcceptsARecentFixOnlyWhenAskedTo() {
    run(acceptRecent = true, LocationState.Resolving(FIX))
    run(acceptRecent = false, LocationState.Resolving(FIX))

    assertEquals(listOf(LocationQuery(true), LocationQuery(false)), queries)
  }

  @Test
  fun aFixIsSavedAndSentAsAcquired() {
    assertEquals(
      listOf(Report.Acquired(FIX)),
      run(acceptRecent = false, LocationState.Resolving(FIX)),
    )
    assertEquals(listOf(FIX), saved)
  }

  @Test
  fun anApproximateFixIsAcquiredLikeAnyOther() {
    assertEquals(
      listOf(Report.Acquired(FIX)),
      run(acceptRecent = true, LocationState.Resolving(FIX, approximate = true)),
    )
  }

  @Test
  fun aNamedLocationIsSavedAndSentAsAcquired() {
    assertEquals(
      listOf(Report.Acquired(FIX)),
      run(acceptRecent = false, LocationState.Resolved(FIX)),
    )
    assertEquals(listOf(FIX), saved)
  }

  @Test
  fun onlyTheFirstFixIsUsedSoARecentOneEndsTheRequest() {
    var askedForMore = false
    val bridge = bridge {
      flow {
        emit(LocationState.Resolving(FIX))
        askedForMore = true
        emit(LocationState.Resolving(OTHER_FIX))
      }
    }

    bridge.start(acceptRecent = true)
    bridge.awaitCompletion()

    assertEquals(listOf(Report.Acquired(FIX)), reports)
    assertFalse(askedForMore)
  }

  @Test
  fun eachReasonIsReportedWithTheErrorCodeTheScreensKnowAndTheLastKnownLocation() {
    val reasons =
      listOf(
        UnavailableReason.NoPermission to LocationError.Code.NO_PERMISSIONS,
        UnavailableReason.NoPlayServices to LocationError.Code.NO_PLAY_SERVICES,
        UnavailableReason.Timeout to LocationError.Code.CANNOT_DETERMINE_LOCATION,
        UnavailableReason.Failed(IllegalStateException("broken")) to
          LocationError.Code.CANNOT_DETERMINE_LOCATION,
        UnavailableReason.ServicesOff(null) to LocationError.Code.CANNOT_DETERMINE_LOCATION,
        UnavailableReason.ServicesOff(object : SettingsResolution {}) to
          LocationError.Code.CANNOT_DETERMINE_LOCATION,
      )

    for ((reason, code) in reasons) {
      reports.clear()
      val report = run(acceptRecent = true, LocationState.Unavailable(reason, SAVED)).single()

      report as Report.NotAcquired
      assertEquals("$reason", code, report.error.errorCode)
      assertSame(SAVED, report.lastKnown)
    }
    assertTrue(saved.isEmpty())
  }

  @Test
  fun locationTurnedOffCarriesTheWayToTurnItOn() {
    val resolvable = ResolvableApiException(Status(CommonStatusCodes.RESOLUTION_REQUIRED, "off"))

    val report =
      run(
          acceptRecent = false,
          LocationState.Unavailable(
            UnavailableReason.ServicesOff(PlayServicesSettingsResolution(resolvable)),
            null,
          ),
        )
        .single() as Report.NotAcquired

    assertEquals(LocationError.Code.NO_LOCATION_MANAGER_SERVICE, report.error.errorCode)
    assertEquals(CommonStatusCodes.RESOLUTION_REQUIRED, report.error.apiExceptionStatus?.statusCode)
    assertEquals(null, report.lastKnown)
  }

  @Test
  fun aSourceThatFailsOrEndsWithoutAnswerCannotDetermineTheLocation() {
    val failing = bridge { flow { throw IllegalStateException("broken") } }
    failing.start(acceptRecent = true)
    failing.awaitCompletion()

    val silent = bridge { emptyFlow() }
    silent.start(acceptRecent = true)
    silent.awaitCompletion()

    val notAcquired = reports.map { it as Report.NotAcquired }
    // LocationError has no equals, so its code is compared.
    assertEquals(
      List(2) { LocationError.Code.CANNOT_DETERMINE_LOCATION },
      notAcquired.map { it.error.errorCode },
    )
    assertEquals(listOf(SAVED, SAVED), notAcquired.map { it.lastKnown })
  }

  @Test
  fun waitingReturnsOnlyOnceTheRequestHasEnded() {
    val release = CountDownLatch(1)
    val bridge = bridge {
      flow {
        release.await(TEST_TIME_LIMIT_SECONDS, TimeUnit.SECONDS)
        emit(LocationState.Resolving(FIX))
      }
    }

    bridge.start(acceptRecent = false)
    Thread {
      Thread.sleep(SLOW_SOURCE_MILLIS)
      release.countDown()
    }
      .start()
    bridge.awaitCompletion()

    assertEquals(listOf(Report.Acquired(FIX)), reports)
  }

  @Test
  fun cancellingStopsTheRequestWithoutAReport() {
    val started = CountDownLatch(1)
    val stopped = CountDownLatch(1)
    val bridge = bridge {
      flow {
        started.countDown()
        try {
          awaitCancellation()
        } finally {
          stopped.countDown()
        }
      }
    }

    bridge.start(acceptRecent = false)
    assertTrue(started.await(TEST_TIME_LIMIT_SECONDS, TimeUnit.SECONDS))
    bridge.cancel()

    // Waiting on the latch rather than on the request keeps a request that is never stopped from
    // hanging the test.
    assertTrue(stopped.await(TEST_TIME_LIMIT_SECONDS, TimeUnit.SECONDS))
    bridge.awaitCompletion()
    assertTrue(reports.isEmpty())
  }

  @Test
  fun aNewRequestStopsTheOneBeforeIt() {
    val first = CountDownLatch(1)
    val firstStopped = CountDownLatch(1)
    var calls = 0
    val bridge = bridge {
      calls++
      if (calls == 1) {
        flow {
          first.countDown()
          try {
            awaitCancellation()
          } finally {
            firstStopped.countDown()
          }
        }
      } else {
        flowOf(LocationState.Resolving(FIX))
      }
    }

    bridge.start(acceptRecent = false)
    assertTrue(first.await(TEST_TIME_LIMIT_SECONDS, TimeUnit.SECONDS))
    bridge.start(acceptRecent = false)
    bridge.awaitCompletion()

    assertTrue(firstStopped.await(TEST_TIME_LIMIT_SECONDS, TimeUnit.SECONDS))
    assertEquals(listOf(Report.Acquired(FIX)), reports)
  }

  @Test
  fun waitingWithNoRequestReturnsAtOnce() {
    bridge { emptyFlow() }.awaitCompletion()

    assertTrue(reports.isEmpty())
  }

  private companion object {
    val SAVED: ZmanimLocation =
      ZmanimLocation.Builder().locationName("Saved").atLatitude(1.0).atLongitude(2.0).build()
    val FIX: ZmanimLocation = ZmanimLocation.Builder().atLatitude(31.77).atLongitude(35.21).build()
    val OTHER_FIX: ZmanimLocation =
      ZmanimLocation.Builder().atLatitude(40.71).atLongitude(-74.0).build()
    const val TEST_TIME_LIMIT_SECONDS = 10L
    const val SLOW_SOURCE_MILLIS = 200L
  }
}
