/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zmanlib.location

/** Answers from the places already looked up, so they are found again quickly and offline. */
public class GeocodeCacheResolver(private val cache: GeocodeCache) : PlaceResolver {

  override suspend fun fromName(request: ZmanimLocation): PlaceResult = find(request)

  override suspend fun fromCoordinates(request: ZmanimLocation): PlaceResult = find(request)

  private suspend fun find(request: ZmanimLocation): PlaceResult =
    cache.find(request)?.let { PlaceResult.Found(it) } ?: PlaceResult.NotFound
}
