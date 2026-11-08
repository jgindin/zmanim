/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zmanim.android.location.resolvers

import android.location.Address
import android.location.Geocoder
import android.os.Build
import androidx.annotation.RequiresApi
import com.gindin.zmanlib.location.PlaceResolver
import com.gindin.zmanlib.location.PlaceResult
import com.gindin.zmanlib.location.ZmanimLocation
import java.io.IOException
import kotlin.coroutines.resume
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.suspendCancellableCoroutine

/**
 * Names places through the system [Geocoder], which on most devices sends the query to Google.
 *
 * Both lookups suspend without holding the caller's thread and return as soon as the caller is
 * cancelled, which is what lets [com.gindin.zmanlib.location.ResolverChain]'s time limit move on to
 * the next source. From API 33 the geocoder's listener overloads are used. Before that only the
 * blocking overloads exist, so they run on [blockingDispatcher], and a cancelled caller stops
 * waiting for the answer rather than waiting for the call to finish.
 *
 * A device with no geocoder answers [PlaceResult.NotFound]: there is nothing to retry. A network or
 * service error is [PlaceResult.Failed].
 */
class SystemGeocoder(
  private val geocoder: Geocoder,
  private val blockingDispatcher: CoroutineDispatcher = Dispatchers.IO,
  private val isPresent: () -> Boolean = Geocoder::isPresent,
) : PlaceResolver {

  /**
   * The address's name, with the request's coordinates kept: they are where the user is, and the
   * address the geocoder picks is only near them.
   */
  @Suppress("DEPRECATION") // The blocking overload: deprecated from API 33, only called before it.
  override suspend fun fromCoordinates(request: ZmanimLocation): PlaceResult =
    lookUp(
      blocking = { geocoder.getFromLocation(request.latitude, request.longitude, MAX_RESULTS) },
      listening = { listener ->
        geocoder.getFromLocation(request.latitude, request.longitude, MAX_RESULTS, listener)
      },
    ) { address ->
      ZmanimLocation.Builder()
        .from(request)
        .locationName(LocationResolver.addressToLocationName(address))
        .build()
    }

  /** The address's name and coordinates, in the request's time zone. */
  @Suppress("DEPRECATION") // The blocking overload: deprecated from API 33, only called before it.
  override suspend fun fromName(request: ZmanimLocation): PlaceResult =
    lookUp(
      blocking = { geocoder.getFromLocationName(request.locationName, MAX_RESULTS) },
      listening = { listener ->
        geocoder.getFromLocationName(request.locationName, MAX_RESULTS, listener)
      },
    ) { address ->
      ZmanimLocation.Builder()
        .locationName(LocationResolver.addressToLocationName(address))
        .userName(request.locationName)
        .atLatitude(address.latitude)
        .atLongitude(address.longitude)
        .inTimeZone(request.timeZone)
        .build()
    }

  private suspend fun lookUp(
    blocking: () -> List<Address>?,
    listening: (Geocoder.GeocodeListener) -> Unit,
    toLocation: (Address) -> ZmanimLocation,
  ): PlaceResult {

    if (!isPresent()) {
      return PlaceResult.NotFound
    }

    val addresses =
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        listen(listening)
      } else {
        offload(blocking)
      }

    return addresses.fold(
      onSuccess = { found ->
        found?.firstOrNull()?.let { PlaceResult.Found(toLocation(it)) } ?: PlaceResult.NotFound
      },
      onFailure = { PlaceResult.Failed(it) },
    )
  }

  @RequiresApi(Build.VERSION_CODES.TIRAMISU)
  private suspend fun listen(start: (Geocoder.GeocodeListener) -> Unit): Result<List<Address>?> =
    suspendCancellableCoroutine { continuation ->
      val listener =
        object : Geocoder.GeocodeListener {
          override fun onGeocode(addresses: MutableList<Address>) {
            continuation.resume(Result.success(addresses))
          }

          override fun onError(errorMessage: String?) {
            continuation.resume(Result.failure(IOException(errorMessage)))
          }
        }
      runCatching { start(listener) }.onFailure { continuation.resume(Result.failure(it)) }
    }

  private suspend fun offload(call: () -> List<Address>?): Result<List<Address>?> =
    suspendCancellableCoroutine { continuation ->
      blockingDispatcher.asExecutor().execute { continuation.resume(runCatching { call() }) }
    }

  private companion object {
    /** As many as the geocoder is asked for; the first, its best match, is used. */
    const val MAX_RESULTS = 5
  }
}
