/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zmanim.android.location.acquirers

import com.gindin.zmanim.android.location.LocationError
import com.gindin.zmanlib.location.LocationQuery
import com.gindin.zmanlib.location.LocationSource
import com.gindin.zmanlib.location.LocationState
import com.gindin.zmanlib.location.UnavailableReason
import com.gindin.zmanlib.location.ZmanimLocation
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/** Where [DeviceLocationBridge] reports, in the terms the app's screens already use. */
interface DeviceLocationSink {

  /** The device's location, not yet named. */
  fun acquired(location: ZmanimLocation)

  /** The device's location could not be had; [lastKnown] is the one saved by an earlier run. */
  fun notAcquired(error: LocationError, lastKnown: ZmanimLocation?)
}

/**
 * Asks a [LocationSource] for the device's location and reports the first answer to [sink], as
 * [LocationError] codes for the screens.
 *
 * Only the first answer is used: when a recent fix is accepted and the device has one, it is the
 * answer and no fresh fix is asked for. Without a recent one, the source asks for a fresh fix. A
 * fix is saved with [save] before it is reported, so the next run can fall back on it.
 */
class DeviceLocationBridge(
  private val source: LocationSource,
  private val sink: DeviceLocationSink,
  private val save: (ZmanimLocation) -> Unit,
  private val savedLocation: () -> ZmanimLocation?,
  private val scope: CoroutineScope,
) {

  // Volatile because the request is started and awaited on the service's worker thread, and
  //  cancelled on the main thread when the service is destroyed.
  @Volatile private var request: Job? = null

  /** Starts a request, stopping any that is still running. */
  fun start(acceptRecent: Boolean) {
    cancel()
    request = scope.launch { report(firstAnswer(acceptRecent)) }
  }

  /**
   * Blocks until the current request has ended, so that the service doing the work stays alive
   * while the device looks for a fix. It must not be called on the main thread, which delivers the
   * fixes.
   */
  fun awaitCompletion() {
    val running = request ?: return
    runBlocking { running.join() }
  }

  /** Stops the current request, if any, without reporting it. */
  fun cancel() {
    request?.cancel()
  }

  // A source is code this bridge does not control (Play services, the geoid grid), and an exception
  //  escaping this coroutine would end the app, so any failure is the location not being had.
  @Suppress("TooGenericExceptionCaught")
  private suspend fun firstAnswer(acceptRecent: Boolean): LocationState =
    try {
      source.locations(LocationQuery(acceptRecent)).firstOrNull()
        ?: LocationState.Unavailable(UnavailableReason.Failed(null), savedLocation())
    } catch (e: CancellationException) {
      throw e
    } catch (e: Exception) {
      LocationState.Unavailable(UnavailableReason.Failed(e), savedLocation())
    }

  private fun report(state: LocationState) {
    when (state) {
      is LocationState.Resolving -> acquired(state.fix)
      is LocationState.Resolved -> acquired(state.location)
      is LocationState.Unavailable -> sink.notAcquired(errorFor(state.reason), state.lastKnown)
    }
  }

  private fun acquired(location: ZmanimLocation) {
    save(location)
    sink.acquired(location)
  }

  private fun errorFor(reason: UnavailableReason): LocationError =
    when (reason) {
      UnavailableReason.NoPermission -> LocationError(LocationError.Code.NO_PERMISSIONS)
      UnavailableReason.NoPlayServices -> LocationError(LocationError.Code.NO_PLAY_SERVICES)
      is UnavailableReason.ServicesOff ->
        (reason.resolution as? PlayServicesSettingsResolution)?.let {
          LocationError(LocationError.Code.NO_LOCATION_MANAGER_SERVICE, it.exception)
        } ?: LocationError(LocationError.Code.CANNOT_DETERMINE_LOCATION)
      UnavailableReason.Timeout,
      is UnavailableReason.Failed -> LocationError(LocationError.Code.CANNOT_DETERMINE_LOCATION)
    }
}
