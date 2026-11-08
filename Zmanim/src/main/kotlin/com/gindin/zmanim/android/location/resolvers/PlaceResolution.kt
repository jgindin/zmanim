/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zmanim.android.location.resolvers

import android.content.Context
import android.location.Geocoder
import androidx.annotation.VisibleForTesting
import com.gindin.zmanlib.location.GeocodeCacheResolver
import com.gindin.zmanlib.location.InMemoryPlaceIndex
import com.gindin.zmanlib.location.PlaceIndex
import com.gindin.zmanlib.location.PlaceIndexResolver
import com.gindin.zmanlib.location.ResolverChain

/**
 * The app's place lookup: the cache, then the system geocoder (the only one that goes online, and
 * left out on a device that has none), then the bundled place data. Each source has
 * [ResolverChain.DEFAULT_TIMEOUT_PER_SOURCE]. The caller stores what the chain finds in [cache].
 */
class PlaceResolution(val cache: FileGeocodeCache, val chain: ResolverChain) {

  companion object {

    /**
     * The bundled place data, in the format [com.gindin.zmanlib.location.PlaceIndexFormat] reads.
     */
    const val PLACES_ASSET: String = "places/places.tsv"

    // One of each per process, shared by every chain: the place data is read once, and the cache
    //  keeps its entries in memory, so two caches over the same file would undo each other's work.
    @Volatile private var shared: Pair<FileGeocodeCache, PlaceIndex>? = null

    private fun shared(context: Context): Pair<FileGeocodeCache, PlaceIndex> =
      shared
        ?: synchronized(this) {
          shared
            ?: context.applicationContext
              .let { app ->
                FileGeocodeCache(app.filesDir) to
                  InMemoryPlaceIndex({ app.assets.open(PLACES_ASSET) })
              }
              .also { shared = it }
        }

    @JvmStatic
    fun create(context: Context): PlaceResolution =
      create(context, if (Geocoder.isPresent()) Geocoder(context.applicationContext) else null)

    /** With [geocoder] as the system geocoder, or none if it is null. */
    @VisibleForTesting
    internal fun create(context: Context, geocoder: Geocoder?): PlaceResolution {

      val (cache, places) = shared(context)
      val sources = buildList {
        add(GeocodeCacheResolver(cache))
        geocoder?.let { add(SystemGeocoder(it)) }
        add(PlaceIndexResolver(places))
      }

      return PlaceResolution(cache, ResolverChain(sources))
    }

    /** Forgets the shared cache and place data, so each test starts from its own files. */
    @VisibleForTesting
    internal fun forgetShared() {
      synchronized(this) { shared = null }
    }
  }
}
