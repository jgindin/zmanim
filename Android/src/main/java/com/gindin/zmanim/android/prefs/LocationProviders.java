/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.android.prefs;

/**
 * Lists the possible types of location providers.
 */
public enum LocationProviders {

  /** The name of the category which contains these options. */
  DYNAMIC_LOCATION_CATEGORY,

  /** Indicates the user wants us to find their location every time. */
  DYNAMIC_POSITION,

  /** The user has given us an address (or at least enough data from one) to figure out where they are. */
  STATIC_POSITION,

  /** The user has pinpointed their location. */
  LATITUDE_LONGITUDE_POSITION;


  /** Options specific to the DynamicLocationProvider. */
  public static enum DynamicProviderOptions {

    /** Should we prefer a GPS location, even when other providers are available? */
    PREFER_GPS,
    PREFER_CACHED_LOCATION
  }


  /** Options specific to the LatitudeLongitudeLocationProvider. */
  public static enum LatitudeLongitudeProviderOptions {

    LATITUDE,
    LONGITUDE
  }


  /** Options specific to the StaticLocationProvider. */
  public static enum StaticProviderOptions {
    PLACE
  }


  public static enum TimeZoneOptions {
    OVERRIDE_SYSTEM_TIME_ZONE,
    TIME_ZONE_AREA,
    TIME_ZONE_LOCALE
  }

} // End of LocationProviders enum
