/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zmanlib.location

import kotlinx.coroutines.flow.Flow

/**
 * What a caller wants from a [LocationSource].
 *
 * @property acceptRecent true to start with a recent fix the device already has, if there is one,
 *   before a fresh one arrives; false to wait for a fresh fix.
 */
public data class LocationQuery(public val acceptRecent: Boolean)

/** Where the device is. */
public fun interface LocationSource {

  /**
   * The device's location as it becomes known. Collecting starts the request, and cancelling the
   * collector stops it.
   */
  public fun locations(query: LocationQuery): Flow<LocationState>
}
