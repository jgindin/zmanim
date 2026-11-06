/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zmanlib.location

/**
 * One source of place names and coordinates: the cache, the system geocoder or the offline place
 * index. [ResolverChain] asks them in order.
 */
public interface PlaceResolver {

  /**
   * Finds the place the user typed. The request's location name is the text as typed, and its time
   * zone is the one the result should use.
   */
  public suspend fun fromName(request: ZmanimLocation): PlaceResult

  /** Names the place at the request's coordinates. */
  public suspend fun fromCoordinates(request: ZmanimLocation): PlaceResult
}

/** What a [PlaceResolver] answered. */
public sealed interface PlaceResult {

  /** The source knows the place. */
  public data class Found(public val location: ZmanimLocation) : PlaceResult

  /** The source looked and does not know the place. */
  public data object NotFound : PlaceResult

  /** The source could not look, for example because there is no network. */
  public data class Failed(public val cause: Throwable) : PlaceResult
}
