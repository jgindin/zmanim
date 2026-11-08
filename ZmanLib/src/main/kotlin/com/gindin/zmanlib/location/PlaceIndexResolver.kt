/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zmanlib.location

/**
 * Answers from the bundled place data, which works offline. A place it finds is named "name,
 * country" and keeps the request's time zone, which is the user's choice or the device's, as for
 * the other sources.
 *
 * The data holds towns of more than 15,000 people, so the place nearest some coordinates can be far
 * from them. A fix is named after the nearest place only when that place is within
 * [nearEnoughMeters]; farther than that, the place is not found. The fix keeps its own coordinates
 * and elevation: the data is used only to name it.
 */
public class PlaceIndexResolver(
  private val index: PlaceIndex,
  private val nearEnoughMeters: Double = DEFAULT_NEAR_ENOUGH_METERS,
) : PlaceResolver {

  /** The best match for the typed name, at that place's coordinates. */
  override suspend fun fromName(request: ZmanimLocation): PlaceResult {

    val place = index.byName(request.locationName).firstOrNull() ?: return PlaceResult.NotFound

    return PlaceResult.Found(
      ZmanimLocation.Builder()
        .locationName(nameOf(place))
        .userName(request.locationName)
        .atLatitude(place.latitude)
        .atLongitude(place.longitude)
        .inTimeZone(request.timeZone)
        .build()
    )
  }

  override suspend fun fromCoordinates(request: ZmanimLocation): PlaceResult {

    val place =
      index.nearest(request.latitude, request.longitude)?.takeIf { isNearEnough(request, it) }

    return place?.let {
      PlaceResult.Found(ZmanimLocation.Builder().from(request).locationName(nameOf(it)).build())
    } ?: PlaceResult.NotFound
  }

  private fun isNearEnough(request: ZmanimLocation, place: IndexedPlace): Boolean {
    val atPlace =
      ZmanimLocation.Builder().atLatitude(place.latitude).atLongitude(place.longitude).build()
    return request.getGeodesicDistance(atPlace) <= nearEnoughMeters
  }

  private fun nameOf(place: IndexedPlace): String = "${place.name}, ${place.country}"

  public companion object {

    /**
     * Far enough to name a fix in a town's suburbs after the town, near enough that the name is
     * still where the user is.
     */
    public const val DEFAULT_NEAR_ENOUGH_METERS: Double = 25_000.0
  }
}
