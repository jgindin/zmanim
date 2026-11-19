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
 *
 * [nearbyPlaceName] answers for a fix that no source could name: the nearest place within
 * [nearbyMeters], so that the app can say the fix is near it.
 */
public class PlaceIndexResolver(
  private val index: PlaceIndex,
  private val nearEnoughMeters: Double = DEFAULT_NEAR_ENOUGH_METERS,
  private val nearbyMeters: Double = DEFAULT_NEARBY_METERS,
) : PlaceResolver {

  /**
   * The best match for the typed name, at that place's coordinates and elevation (the data's value
   * for the town; 0, meaning unknown, when the data has none).
   */
  override suspend fun fromName(request: ZmanimLocation): PlaceResult {

    val place = index.byName(request.locationName).firstOrNull() ?: return PlaceResult.NotFound

    return PlaceResult.Found(
      ZmanimLocation.Builder()
        .locationName(nameOf(place))
        .userName(request.locationName)
        .atLatitude(place.latitude)
        .atLongitude(place.longitude)
        .atElevation(place.elevationMeters ?: 0.0)
        .inTimeZone(request.timeZone)
        .build()
    )
  }

  override suspend fun fromCoordinates(request: ZmanimLocation): PlaceResult {

    val place = nearestWithin(request, nearEnoughMeters)

    return place?.let {
      PlaceResult.Found(ZmanimLocation.Builder().from(request).locationName(nameOf(it)).build())
    } ?: PlaceResult.NotFound
  }

  /**
   * The name ("name, country") of the place nearest the request's coordinates when it is within
   * [nearbyMeters] of them; null when no place is that near.
   */
  public suspend fun nearbyPlaceName(request: ZmanimLocation): String? =
    nearestWithin(request, nearbyMeters)?.let(::nameOf)

  private suspend fun nearestWithin(request: ZmanimLocation, meters: Double): IndexedPlace? {

    val place = index.nearest(request.latitude, request.longitude) ?: return null
    val atPlace =
      ZmanimLocation.Builder().atLatitude(place.latitude).atLongitude(place.longitude).build()

    return place.takeIf { request.getGeodesicDistance(atPlace) <= meters }
  }

  private fun nameOf(place: IndexedPlace): String = "${place.name}, ${place.country}"

  public companion object {

    /**
     * Far enough to name a fix in a town's suburbs after the town, near enough that the name is
     * still where the user is.
     */
    public const val DEFAULT_NEAR_ENOUGH_METERS: Double = 25_000.0

    /**
     * How far a fix can be from a place and still be called near it: 20 miles, near enough that the
     * name still says where the user is. Beyond it, the fix keeps a name made of its coordinates.
     */
    public const val DEFAULT_NEARBY_METERS: Double = 20 * 1_609.344
  }
}
