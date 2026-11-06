/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zmanlib.location

/**
 * One place in the bundled place data.
 *
 * @property elevationMeters height above mean sea level, or null when the data has none.
 * @property timeZoneId an IANA time zone id, such as "America/New_York".
 * @property jewishCommunity true for a place listed for its Jewish community rather than its size.
 */
public data class IndexedPlace(
  public val name: String,
  public val country: String,
  public val latitude: Double,
  public val longitude: Double,
  public val elevationMeters: Double?,
  public val timeZoneId: String,
  public val jewishCommunity: Boolean,
)

/** The bundled place data, which works offline. */
public interface PlaceIndex {

  /** The place nearest to these coordinates by great-circle distance, or null if there are none. */
  public suspend fun nearest(latitude: Double, longitude: Double): IndexedPlace?

  /** The places whose name matches the text, best match first; empty if none does. */
  public suspend fun byName(text: String): List<IndexedPlace>
}
