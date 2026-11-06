/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zmanlib.location

/** The places the app has already looked up, kept so they are found again offline and quickly. */
public interface GeocodeCache {

  /** The cached place that answers this request, or null if there is none. */
  public suspend fun find(request: ZmanimLocation): ZmanimLocation?

  /** Keeps a place that has both a name and coordinates; any other location is ignored. */
  public suspend fun store(location: ZmanimLocation)
}
