/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zmanlib.location

/**
 * A [PlaceResolver] that answers both kinds of lookup with [answer], and records what it was asked.
 */
class FakePlaceResolver(private val answer: suspend (ZmanimLocation) -> PlaceResult) :
  PlaceResolver {

  val nameRequests: MutableList<ZmanimLocation> = mutableListOf()

  val coordinateRequests: MutableList<ZmanimLocation> = mutableListOf()

  val wasAsked: Boolean
    get() = nameRequests.isNotEmpty() || coordinateRequests.isNotEmpty()

  override suspend fun fromName(request: ZmanimLocation): PlaceResult {
    nameRequests += request
    return answer(request)
  }

  override suspend fun fromCoordinates(request: ZmanimLocation): PlaceResult {
    coordinateRequests += request
    return answer(request)
  }
}
