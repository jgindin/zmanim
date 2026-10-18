/*
 * Copyright (c) 2022. Jay R. Gindin
 */

package com.gindin.zmanlib.location

import org.json.JSONObject
import org.json.JSONTokener
import java.util.TimeZone

/**
 * Handles the translation of a ZmanimLocation to/from JSON.
 */
public object ZmanLocationJSON {

  private const val LATITUDE = "lat"
  private const val LONGITUDE = "lon"
  private const val ELEVATION = "el"
  private const val TIME_ZONE = "tz"
  private const val PROVIDER = "pv"
  private const val ACCURACY = "ac"
  private const val NAME = "name"
  private const val USER_NAME = "userName"

  private const val NO_NAME = " - - "


  @JvmStatic
  public fun toJSON(location: ZmanimLocation): String {

    val jsonLocation = JSONObject()
    jsonLocation.put(LATITUDE, location.latitude)
    jsonLocation.put(LONGITUDE, location.longitude)
    jsonLocation.put(ELEVATION, location.elevation)
    jsonLocation.put(TIME_ZONE, location.timeZone.id)
    jsonLocation.put(PROVIDER, location.providerName)

    // The accuracy is widened to a double so that it is stored the same way as the other numbers.
    jsonLocation.put(ACCURACY, location.accuracy.toDouble())

    if (location.hasName()) {
      jsonLocation.put(NAME, location.getLocationName())
    }
    else {
      jsonLocation.put(NAME, NO_NAME)
    }

    jsonLocation.put(USER_NAME, location.originalUserName)

    return jsonLocation.toString(2)
  }


  @JvmStatic
  public fun fromJSON(json: String): ZmanimLocation {

    val tokener = JSONTokener(json)
    val root = tokener.nextValue() as JSONObject

    val latitude = root.getDouble(LATITUDE)
    val longitude = root.getDouble(LONGITUDE)
    val elevation = root.getDouble(ELEVATION)
    val accuracy = root.getDouble(ACCURACY).toFloat()
    val provider = root.getString(PROVIDER)
    val tz = root.getString(TIME_ZONE)
    val timeZone = TimeZone.getTimeZone(tz)
    var name: String? = root.getString(NAME)
    if (name == NO_NAME) {
      name = null
    }
    val userName = root.optString(USER_NAME)

    return ZmanimLocation.Builder()
      .atLatitude(latitude)
      .atLongitude(longitude)
      .atElevation(elevation)
      .withAccuracy(accuracy)
      .fromProvider(provider)
      .inTimeZone(timeZone)
      .locationName(name)
      .userName(userName)
      .build()
  }

}
