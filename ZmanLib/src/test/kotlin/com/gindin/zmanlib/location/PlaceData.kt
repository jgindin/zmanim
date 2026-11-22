/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zmanlib.location

/** Builds synthetic place data in the bundled format, for the place index tests. */
object PlaceData {

  const val HEADER = "zmanim-places\t2"

  /** The data with the usual header, the given countries (code to name) and place lines. */
  fun text(countries: Map<String, String>, vararg places: String): String =
    (listOf(HEADER, "# synthetic") + countries.map { (code, name) -> "C\t$code\t$name" } + places)
      .joinToString("\n", postfix = "\n")

  fun place(
    name: String,
    latitude: Double,
    longitude: Double,
    country: String = "XA",
    elevation: String = "",
    timeZone: String = "UTC",
    flags: String = "0",
    alternateNames: List<String> = emptyList(),
  ): String =
    (listOf("P", name, country, "$latitude", "$longitude", elevation, timeZone, flags) +
        alternateNames)
      .joinToString("\t")
}
