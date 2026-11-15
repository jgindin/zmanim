/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zmanim.android.location.resolvers

import com.gindin.zmanlib.location.CoordinatesResult
import com.gindin.zmanlib.location.PlaceResult
import com.gindin.zmanlib.location.ZmanimLocation
import kotlinx.coroutines.runBlocking

/**
 * Names places through [resolution]'s chain for [LocationToAddressConverter], whose work runs on a
 * background thread that may block. Only a place a source found is cached.
 */
class PlaceNamer(private val resolution: PlaceResolution) {

  /**
   * The place the user typed, or null when no source found it: none knows it, or none could look.
   */
  fun placeForName(request: ZmanimLocation): ZmanimLocation? = runBlocking {
    (resolution.chain.fromName(request) as? PlaceResult.Found)?.location?.also {
      resolution.cache.store(it)
    }
  }

  /**
   * The place at the request's coordinates. When no source can name it, the request comes back as
   * it was given: its name is then its coordinates, so the zmanim can still be shown, and it is not
   * cached, so a source that can name the place later is asked again.
   */
  fun placeForCoordinates(request: ZmanimLocation): ZmanimLocation = runBlocking {
    when (val result = resolution.chain.fromCoordinates(request)) {
      is CoordinatesResult.Named -> result.location.also { resolution.cache.store(it) }
      is CoordinatesResult.Unnamed -> request
    }
  }

  /** Forgets every cached place, for the upgrades that need a fresh cache. */
  fun forgetCachedPlaces() {
    runBlocking { resolution.cache.clear() }
  }
}
