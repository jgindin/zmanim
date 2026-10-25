/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zmanim.android.location.resolvers

import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject

/** Picks the address object out of the JSON a geocoding web service answers with. */
public object GeocodingResponseParser {

  private val json = Json { isLenient = true }

  /**
   * Services answer with either a single object or an array of candidate matches, best first.
   *
   * @return the object, or the first object of the array; null if the service had no match (an
   *   empty array) or answered with something that is neither an object nor an array.
   * @throws SerializationException if the text is not valid JSON, or if the first element of an
   *   array is not an object. Callers treat that as a failed request and may ask again.
   */
  @JvmStatic
  public fun parse(response: String): JsonObject? {

    return when (val root = json.parseToJsonElement(response)) {
      is JsonObject -> root
      is JsonArray -> {
        if (root.isEmpty()) {
          null
        } else {
          root[0] as? JsonObject
            ?: throw SerializationException("The first match is not a JSON object")
        }
      }
      else -> null
    }
  }
}
