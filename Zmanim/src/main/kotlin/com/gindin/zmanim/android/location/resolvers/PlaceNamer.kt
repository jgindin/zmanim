/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zmanim.android.location.resolvers

import com.gindin.zmanlib.location.CoordinatesResult
import com.gindin.zmanlib.location.PlaceIndexResolver
import com.gindin.zmanlib.location.PlaceResult
import com.gindin.zmanlib.location.ZmanimLocation
import kotlinx.coroutines.runBlocking

/**
 * Names places through [resolution]'s chain for [LocationToAddressConverter], whose work runs on a
 * background thread that may block. Only a place a source found is cached.
 *
 * [describeNear] turns the name of a town into the name of a place near it, such as "Near Dimona,
 * Israel", in the app's language.
 */
class PlaceNamer(
  private val resolution: PlaceResolution,
  private val describeNear: (placeName: String) -> String,
) {

  /**
   * The place the user typed, or null when no source found it: none knows it, or none could look.
   */
  fun placeForName(request: ZmanimLocation): ZmanimLocation? = runBlocking {
    (resolution.chain.fromName(request) as? PlaceResult.Found)?.location?.also {
      resolution.cache.store(it)
    }
  }

  /**
   * The place at the request's coordinates. When no source can name it, it is named after the town
   * it is near ([PlaceIndexResolver.nearbyPlaceName]) or, when no town is near enough, comes back
   * as it was given, named by its coordinates, so the zmanim can still be shown. Either way only
   * the name changes, and the place is not cached, so a source that can name it later is asked
   * again.
   */
  fun placeForCoordinates(request: ZmanimLocation): ZmanimLocation = runBlocking {
    when (val result = resolution.chain.fromCoordinates(request)) {
      is CoordinatesResult.Named -> result.location.also { resolution.cache.store(it) }
      is CoordinatesResult.Unnamed -> nearATown(request) ?: request
    }
  }

  private suspend fun nearATown(request: ZmanimLocation): ZmanimLocation? =
    resolution.placeData.nearbyPlaceName(request)?.let { town ->
      ZmanimLocation.Builder().from(request).locationName(describeNear(town)).build()
    }

  /** Forgets every cached place, for the upgrades that need a fresh cache. */
  fun forgetCachedPlaces() {
    runBlocking { resolution.cache.clear() }
  }
}
