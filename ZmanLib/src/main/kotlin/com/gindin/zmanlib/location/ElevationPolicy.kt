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
  public fun elevationMeters(inputs: ElevationInputs): Double
}
