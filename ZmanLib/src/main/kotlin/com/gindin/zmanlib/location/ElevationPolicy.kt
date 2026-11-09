/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zmanlib.location

/**
 * What is known about a location's elevation. Each value is null when it is not known.
 *
 * @property mslAltitudeMeters the fix's altitude above mean sea level, which newer devices report.
 * @property ellipsoidAltitudeMeters the fix's altitude above the WGS 84 ellipsoid, which can differ
 *   from height above sea level by tens of meters.
 * @property placeElevationMeters the elevation the bundled place data gives for the matched place.
 */
public data class ElevationInputs(
  public val latitude: Double,
  public val longitude: Double,
  public val mslAltitudeMeters: Double?,
  public val ellipsoidAltitudeMeters: Double?,
  public val placeElevationMeters: Double?,
)

/** Decides the elevation stored on a location, in meters above mean sea level. */
public fun interface ElevationPolicy {

  /** The elevation to use, or 0 when none of the inputs gives one. */
  public suspend fun elevationMeters(inputs: ElevationInputs): Double
}

/**
 * The elevation in meters above mean sea level, from the first input that gives one:
 * 1. the fix's altitude above mean sea level (Android reports it from API 34);
 * 2. the fix's altitude above the ellipsoid, less the geoid height there from [geoid];
 * 3. the bundled place data's elevation for the matched place, which is a value for the town, not
 *    for the user's street;
 * 4. otherwise 0, which means unknown, as it always has.
 *
 * An input that is NaN or infinite is skipped. An elevation below sea level is 0, as
 * [ZmanimLocation.Builder.atElevation] makes it: the zmanim engine does not take negative
 * elevations. Whether the elevation is applied at all is the user's choice, made in
 * [com.gindin.zmanlib.zman.ZmanimFactory.useElevation], not here.
 */
public class SeaLevelElevationPolicy(private val geoid: GeoidModel) : ElevationPolicy {

  override suspend fun elevationMeters(inputs: ElevationInputs): Double {
    val meters =
      inputs.mslAltitudeMeters.known()
        ?: corrected(inputs)
        ?: inputs.placeElevationMeters.known()
        ?: 0.0
    return meters.coerceAtLeast(0.0)
  }

  private suspend fun corrected(inputs: ElevationInputs): Double? {
    val ellipsoid = inputs.ellipsoidAltitudeMeters.known() ?: return null
    return geoid.heightMeters(inputs.latitude, inputs.longitude).known()?.let { ellipsoid - it }
  }

  private fun Double?.known(): Double? = this?.takeIf { it.isFinite() }
}
