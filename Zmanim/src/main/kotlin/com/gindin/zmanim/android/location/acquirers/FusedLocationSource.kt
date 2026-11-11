/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zmanim.android.location.acquirers

import android.content.Context
import android.location.Location
import android.os.Build
import androidx.annotation.VisibleForTesting
import com.gindin.zmanim.android.location.ZmanLocationUtil
import com.gindin.zmanim.android.prefs.ZmanimPrefs
import com.gindin.zmanlib.location.BundledGeoidGrid
import com.gindin.zmanlib.location.ElevationInputs
import com.gindin.zmanlib.location.ElevationPolicy
import com.gindin.zmanlib.location.LocationQuery
import com.gindin.zmanlib.location.LocationSource
import com.gindin.zmanlib.location.LocationState
import com.gindin.zmanlib.location.SeaLevelElevationPolicy
import com.gindin.zmanlib.location.SettingsResolution
import com.gindin.zmanlib.location.UnavailableReason
import com.gindin.zmanlib.location.ZmanimLocation
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.Priority
import java.util.TimeZone
import kotlin.coroutines.cancellation.CancellationException
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withTimeoutOrNull

/** How much location access the user has granted the app. */
enum class LocationGrant {
  NONE,

  /** Approximate location only, which can be a couple of kilometers off and has no altitude. */
  APPROXIMATE,
  PRECISE,
}

/** The parts of Play services' fused location client that [FusedLocationSource] uses. */
interface FusedClient {

  /** The device's most recent fix, or null when it has none. */
  suspend fun lastLocation(): Location?

  /**
   * Returns when the device's location settings allow [request]; otherwise throws the
   * [ApiException] Play services gives, a [ResolvableApiException] when the user can be asked to
   * change them.
   */
  suspend fun checkSettings(request: LocationRequest)

  /**
   * Starts [request], calling [onLocation] with each fix and [onError] if it cannot start, and
   * returns what stops it.
   */
  fun requestUpdates(
    request: LocationRequest,
    onLocation: (Location) -> Unit,
    onError: (Exception) -> Unit,
  ): () -> Unit
}

/** Asks the user to turn location on, through the dialog Play services offers. */
class PlayServicesSettingsResolution(val exception: ResolvableApiException) : SettingsResolution

/**
 * The device's location from Play services' fused location provider.
 *
 * Collecting [locations]:
 * 1. With no location access, or without Play services, emits [LocationState.Unavailable] and ends.
 * 2. When the query accepts a recent fix and the device has one from the last [maxRecentAge], emits
 *    it.
 * 3. Asks for one fresh high-accuracy fix and emits it, or [LocationState.Unavailable] when the
 *    location settings do not allow it ([UnavailableReason.ServicesOff], with the dialog to change
 *    them when Play services offers one) or none arrives within [timeout]. After a recent fix has
 *    been emitted, these failures end the flow without an [LocationState.Unavailable], so the
 *    recent fix stands.
 *
 * Every fix is a [LocationState.Resolving]: it has no name yet.
 * [LocationState.Resolving.approximate] is true when the user granted approximate location only,
 * which the app accepts. The elevation comes from [elevationPolicy], from the fix's altitudes
 * alone. Cancelling the collector stops the request.
 */
class FusedLocationSource(
  private val client: FusedClient,
  private val grant: () -> LocationGrant,
  private val playServicesAvailable: () -> Boolean,
  private val elevationPolicy: ElevationPolicy,
  private val timeZone: () -> TimeZone?,
  private val savedLocation: () -> ZmanimLocation?,
  private val now: () -> Long = System::currentTimeMillis,
  private val timeout: Duration = DEFAULT_TIMEOUT,
  private val maxRecentAge: Duration = DEFAULT_MAX_RECENT_AGE,
) : LocationSource {

  /** Why the device's last fix could not be had, the last time it was asked for; null if it was. */
  @Volatile
  var lastLocationFailure: Exception? = null
    private set

  override fun locations(query: LocationQuery): Flow<LocationState> = flow {
    val unavailable = unavailableReason()
    if (unavailable != null) {
      emit(LocationState.Unavailable(unavailable, savedLocation()))
      return@flow
    }

    val approximate = grant() == LocationGrant.APPROXIMATE
    val recent = if (query.acceptRecent) recentFix() else null
    if (recent != null) emit(LocationState.Resolving(toZmanimLocation(recent), approximate))

    when (val fresh = freshFix()) {
      is FreshFix.Arrived -> emit(LocationState.Resolving(toZmanimLocation(fresh.fix), approximate))
      is FreshFix.Missing ->
        if (recent == null) emit(LocationState.Unavailable(fresh.reason, savedLocation()))
    }
  }

  private fun unavailableReason(): UnavailableReason? =
    when {
      grant() == LocationGrant.NONE -> UnavailableReason.NoPermission
      !playServicesAvailable() -> UnavailableReason.NoPlayServices
      else -> null
    }

  /** The device's last fix, if it is recent; a failure to get it is the same as having none. */
  @Suppress("TooGenericExceptionCaught") // Any failure here only means asking for a fresh fix.
  private suspend fun recentFix(): Location? {
    val last =
      try {
        client.lastLocation().also { lastLocationFailure = null }
      } catch (e: CancellationException) {
        throw e
      } catch (e: Exception) {
        lastLocationFailure = e
        null
      }
    return last?.takeIf { now() - it.time < maxRecentAge.inWholeMilliseconds }
  }

  private sealed interface FreshFix {
    data class Arrived(val fix: Location) : FreshFix

    data class Missing(val reason: UnavailableReason) : FreshFix
  }

  private suspend fun freshFix(): FreshFix =
    settingsProblem()?.let { FreshFix.Missing(it) }
      ?: try {
        withTimeoutOrNull(timeout) { updates().first() }?.let { FreshFix.Arrived(it) }
          ?: FreshFix.Missing(UnavailableReason.Timeout)
      } catch (e: RequestFailed) {
        FreshFix.Missing(UnavailableReason.Failed(e.cause))
      }

  /** Why the location settings do not allow the request, or null when they do. */
  private suspend fun settingsProblem(): UnavailableReason? =
    try {
      client.checkSettings(REQUEST)
      null
    } catch (e: ResolvableApiException) {
      UnavailableReason.ServicesOff(PlayServicesSettingsResolution(e))
    } catch (e: ApiException) {
      UnavailableReason.Failed(e)
    }

  private fun updates(): Flow<Location> = callbackFlow {
    val stop =
      client.requestUpdates(
        REQUEST,
        onLocation = { trySend(it) },
        onError = { close(RequestFailed(it)) },
      )
    awaitClose(stop)
  }

  private suspend fun toZmanimLocation(location: Location): ZmanimLocation {
    val msl =
      if (
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE && location.hasMslAltitude()
      )
        location.mslAltitudeMeters
      else null
    val ellipsoid = if (location.hasAltitude()) location.altitude else null
    val elevation =
      elevationPolicy.elevationMeters(
        ElevationInputs(location.latitude, location.longitude, msl, ellipsoid, null)
      )

    val builder =
      ZmanimLocation.Builder()
        .fromProvider(location.provider)
        .at(location.time)
        .atLatitude(location.latitude)
        .atLongitude(location.longitude)
        .atElevation(elevation)
    if (location.hasAccuracy()) builder.withAccuracy(location.accuracy)
    timeZone()?.let { builder.inTimeZone(it) }
    return builder.build()
  }

  /** The fresh-fix request could not start; [cause] is what Play services said. */
  private class RequestFailed(cause: Exception) : Exception(cause)

  companion object {

    /** The bundled geoid grid that corrects GPS altitudes to sea level. */
    const val GEOID_ASSET: String = "geoid/geoid.bin"

    // One per process: the grid is read once and kept in memory.
    @Volatile private var geoid: BundledGeoidGrid? = null

    private fun geoid(context: Context): BundledGeoidGrid =
      geoid
        ?: synchronized(this) {
          geoid
            ?: context.applicationContext
              .let { app -> BundledGeoidGrid({ app.assets.open(GEOID_ASSET) }) }
              .also { geoid = it }
        }

    /**
     * The app's fused location source: the user's time zone override from the location preferences,
     * and the location saved by the last run.
     */
    @JvmStatic
    fun create(context: Context): FusedLocationSource {
      val app = context.applicationContext
      val locationPrefs =
        app.getSharedPreferences(ZmanimPrefs.LOCATION_MANAGEMENT.name, Context.MODE_PRIVATE)
      return FusedLocationSource(
        client = PlayServicesFusedClient(app),
        grant = { PlayServicesFusedClient.grantOf(app) },
        playServicesAvailable = { PlayServicesFusedClient.playServicesAvailable(app) },
        elevationPolicy = SeaLevelElevationPolicy(geoid(app)),
        timeZone = { LocationAcquirer.getTimeZoneFromPrefs(locationPrefs) },
        savedLocation = { ZmanLocationUtil.load(app) },
      )
    }

    /** Forgets the shared geoid grid, so each test starts afresh. */
    @VisibleForTesting
    internal fun forgetShared() {
      synchronized(this) { geoid = null }
    }

    /** How long to wait for a fresh fix. */
    val DEFAULT_TIMEOUT: Duration = 30.seconds

    /** How old the device's last fix can be and still be used before a fresh one arrives. */
    val DEFAULT_MAX_RECENT_AGE: Duration = 30.minutes

    /** One high-accuracy fix, no older than a minute. */
    val REQUEST: LocationRequest =
      LocationRequest.Builder(5.seconds.inWholeMilliseconds)
        .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
        .setMaxUpdates(1)
        .setMaxUpdateAgeMillis(1.minutes.inWholeMilliseconds)
        .build()
  }
}
