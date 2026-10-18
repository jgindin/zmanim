/*
 * Copyright (c) 2022. Jay R. Gindin
 */

package com.gindin.zmanlib.location

import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.doubleOrNull
import java.util.TimeZone

/**
 * Handles the translation of a ZmanimLocation to/from JSON.
 *
 * The text is persisted on users' devices (see [ZmanimLocation.save]), so its shape is a compatibility contract: the
 *  keys below, numbers without a fractional part written as integers, and two-space indentation. Reading is lenient
 *  about how a value is spelled (numbers may be quoted, strings may be bare numbers) and ignores keys it does not know.
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

  private val writer = Json {
    prettyPrint = true
    prettyPrintIndent = "  "
  }

  private val reader = Json {
    isLenient = true
  }


  @JvmStatic
  public fun toJSON(location: ZmanimLocation): String {

    // A LinkedHashMap keeps the keys in the order they are put, which is the order they are written.
    val fields = LinkedHashMap<String, JsonElement>()
    fields[LATITUDE] = number(location.latitude)
    fields[LONGITUDE] = number(location.longitude)
    fields[ELEVATION] = number(location.elevation)
    fields[TIME_ZONE] = JsonPrimitive(location.timeZone.id)

    // A missing value is left out of the document rather than written as null.
    location.providerName?.let { fields[PROVIDER] = JsonPrimitive(it) }

    // The accuracy is widened to a double so that it is written the same way as the other numbers.
    fields[ACCURACY] = number(location.accuracy.toDouble())

    fields[NAME] = JsonPrimitive(if (location.hasName()) location.getLocationName() else NO_NAME)

    location.originalUserName?.let { fields[USER_NAME] = JsonPrimitive(it) }

    return writer.encodeToString(JsonObject.serializer(), JsonObject(fields))
  }


  /**
   * @throws SerializationException if the text is not a JSON object, or a required value is missing or has the wrong
   *  type.
   */
  @JvmStatic
  public fun fromJSON(json: String): ZmanimLocation {

    val root = reader.parseToJsonElement(json) as? JsonObject
      ?: throw SerializationException("A location must be a JSON object")

    val latitude = root.requireDouble(LATITUDE)
    val longitude = root.requireDouble(LONGITUDE)
    val elevation = root.requireDouble(ELEVATION)
    val accuracy = root.requireDouble(ACCURACY).toFloat()
    val provider = root.requireString(PROVIDER)
    val tz = root.requireString(TIME_ZONE)
    val timeZone = TimeZone.getTimeZone(tz)
    var name: String? = root.requireString(NAME)
    if (name == NO_NAME) {
      name = null
    }
    val userName = root.optionalString(USER_NAME)

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


  /**
   * A whole number is written without a fractional part ("754", not "754.0"), matching how the location has always
   *  been persisted.
   */
  private fun number(value: Double): JsonPrimitive {

    if (value.isNaN() || value.isInfinite()) {
      throw SerializationException("JSON does not allow non-finite numbers: $value")
    }

    val whole = value.toLong()
    return if (whole.toDouble() == value) JsonPrimitive(whole) else JsonPrimitive(value)
  }


  private fun JsonObject.requireDouble(key: String): Double {
    return (this[key] as? JsonPrimitive)?.doubleOrNull
      ?: throw SerializationException("Value for \"$key\" is missing or is not a number")
  }


  private fun JsonObject.requireString(key: String): String {
    val value = this[key]
    if (value !is JsonPrimitive || value is JsonNull) {
      throw SerializationException("Value for \"$key\" is missing or is not a string")
    }
    return value.content
  }


  private fun JsonObject.optionalString(key: String): String {
    val value = this[key]
    return if (value is JsonPrimitive && value !is JsonNull) value.content else ""
  }

}
