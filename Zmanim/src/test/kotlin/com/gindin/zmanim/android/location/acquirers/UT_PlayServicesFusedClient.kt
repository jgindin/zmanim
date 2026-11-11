/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zmanim.android.location.acquirers

import android.Manifest
import android.app.Application
import android.location.Location
import android.os.Looper
import androidx.test.core.app.ApplicationProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.tasks.TaskCompletionSource
import com.google.android.gms.tasks.Tasks
import java.io.IOException
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class UT_PlayServicesFusedClient {

  private val fused = mock(FusedLocationProviderClient::class.java)
  private val settings = mock(SettingsClient::class.java)
  private val client = PlayServicesFusedClient(fused, settings, Looper.getMainLooper())

  private fun test(body: suspend CoroutineScope.() -> Unit) = runBlocking {
    withTimeout(10.seconds) { body() }
  }

  @Test
  fun theGrantIsPreciseApproximateOrNone() {
    val app = ApplicationProvider.getApplicationContext<Application>()
    assertEquals(LocationGrant.NONE, PlayServicesFusedClient.grantOf(app))

    shadowOf(app).grantPermissions(Manifest.permission.ACCESS_COARSE_LOCATION)
    assertEquals(LocationGrant.APPROXIMATE, PlayServicesFusedClient.grantOf(app))

    shadowOf(app).grantPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
    assertEquals(LocationGrant.PRECISE, PlayServicesFusedClient.grantOf(app))
  }

  @Test
  fun theLastLocationIsTheTasksResultOrItsFailure() = test {
    val location = Location("fused")
    `when`(fused.lastLocation).thenReturn(Tasks.forResult(location))
    assertSame(location, client.lastLocation())

    `when`(fused.lastLocation).thenReturn(Tasks.forResult(null))
    assertEquals(null, client.lastLocation())

    val failure = IOException("no fix")
    `when`(fused.lastLocation).thenReturn(Tasks.forException(failure))
    // Coroutines may hand back a copy of the exception, with the original as its cause.
    assertSameFailure(failure, runCatching { client.lastLocation() }.exceptionOrNull())

    `when`(fused.lastLocation).thenReturn(Tasks.forCanceled())
    assertTrue(runCatching { client.lastLocation() }.exceptionOrNull() is CancellationException)

    // A task that completes later resumes the caller then.
    val later = TaskCompletionSource<Location>()
    `when`(fused.lastLocation).thenReturn(later.task)
    val waiting = async { client.lastLocation() }
    later.setResult(location)
    assertSame(location, waiting.await())
  }

  @Test
  fun settingsThatAllowTheRequestReturnAndOthersThrow() = test {
    `when`(settings.checkLocationSettings(any(LocationSettingsRequest::class.java)))
      .thenReturn(Tasks.forResult(mock(LocationSettingsResponse::class.java)))
    client.checkSettings(FusedLocationSource.REQUEST)

    val off = IllegalStateException("location is off")
    `when`(settings.checkLocationSettings(any(LocationSettingsRequest::class.java)))
      .thenReturn(Tasks.forException(off))
    assertSameFailure(
      off,
      runCatching { client.checkSettings(FusedLocationSource.REQUEST) }.exceptionOrNull(),
    )
  }

  @Test
  fun eachFixReachesTheListenerAndStoppingRemovesTheSameCallback() {
    `when`(
        fused.requestLocationUpdates(
          any(LocationRequest::class.java),
          any(LocationCallback::class.java),
          any(Looper::class.java),
        )
      )
      .thenReturn(Tasks.forResult(null))
    val received = mutableListOf<Location>()
    val fix = Location("fused")

    val stop = client.requestUpdates(FusedLocationSource.REQUEST, { received += it }, { throw it })
    val callback = ArgumentCaptor.forClass(LocationCallback::class.java)
    verify(fused)
      .requestLocationUpdates(
        any(LocationRequest::class.java),
        callback.capture(),
        any(Looper::class.java),
      )
    callback.value.onLocationResult(LocationResult.create(listOf(fix)))
    stop()

    assertEquals(listOf(fix), received)
    verify(fused).removeLocationUpdates(callback.value)
  }

  @Test
  fun aRequestThatFailsOrIsRefusedReportsTheError() {
    val errors = mutableListOf<Exception>()
    val refused = SecurityException("no access")
    `when`(
        fused.requestLocationUpdates(
          any(LocationRequest::class.java),
          any(LocationCallback::class.java),
          any(Looper::class.java),
        )
      )
      .thenThrow(refused)
    client.requestUpdates(FusedLocationSource.REQUEST, {}, { errors += it })

    val failed = IOException("cannot start")
    `when`(
        fused.requestLocationUpdates(
          any(LocationRequest::class.java),
          any(LocationCallback::class.java),
          any(Looper::class.java),
        )
      )
      .thenReturn(Tasks.forException(failed))
    client.requestUpdates(FusedLocationSource.REQUEST, {}, { errors += it })

    assertEquals(listOf<Exception>(refused, failed), errors)
  }

  private fun assertSameFailure(expected: Throwable, actual: Throwable?) {
    assertTrue("$actual", actual === expected || actual?.cause === expected)
  }
}
