/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zmanlib.location

/** The bundled place data is not in a format this version can read. */
public class PlaceIndexFormatException(message: String) : Exception(message)

/**
 * Reads the bundled place data (`assets/places/places.tsv`, written by the `generatePlaceIndex`
 * task in build-logic, which documents the format).
 *
 * The text is UTF-8, one record per line, fields separated by tabs. The first line names the format
 * and its version. `#` starts a comment line. `C` lines map a country code to its display name, and
 * `P` lines are places: name, country code, latitude, longitude, elevation in meters (empty when
 * unknown), IANA time zone and flags (`1` for a Jewish community).
 */
public object PlaceIndexFormat {

  public const val NAME: String = "zmanim-places"

  public const val VERSION: Int = 1

  private const val COUNTRY_FIELDS = 3
  private const val PLACE_FIELDS = 8
  private const val MAX_LATITUDE = 90.0
  private const val MAX_LONGITUDE = 180.0

  /** How much of an unexpected first line goes into the error message. */
  private const val HEADER_SHOWN = 40

  private const val NAME_FIELD = 1
  private const val COUNTRY_FIELD = 2
  private const val LATITUDE_FIELD = 3
  private const val LONGITUDE_FIELD = 4
  private const val ELEVATION_FIELD = 5
  private const val TIME_ZONE_FIELD = 6
  private const val FLAGS_FIELD = 7

  /** The places in the order the data lists them; fails on anything it does not understand. */
  public fun read(lines: Sequence<String>): List<IndexedPlace> {

    val iterator = lines.iterator()
    val header = if (iterator.hasNext()) iterator.next() else ""
    if (header != "$NAME\t$VERSION") {
      throw PlaceIndexFormatException("Not $NAME version $VERSION: '${header.take(HEADER_SHOWN)}'")
    }

    val countries = mutableMapOf<String, String>()
    val places = mutableListOf<IndexedPlace>()
    var lineNumber = 1
    for (line in iterator) {
      lineNumber++
      if (line.isEmpty() || line.startsWith("#")) continue

      val fields = line.split('\t')
      when (fields[0]) {
        "C" -> {
          expectFields(fields, COUNTRY_FIELDS, lineNumber)
          countries[fields[1]] = fields[2]
        }
        "P" -> places += readPlace(fields, countries, lineNumber)
        else -> throw PlaceIndexFormatException("Line $lineNumber: unknown record '${fields[0]}'")
      }
    }
    return places
  }

  private fun readPlace(
    fields: List<String>,
    countries: Map<String, String>,
    lineNumber: Int,
  ): IndexedPlace {

    expectFields(fields, PLACE_FIELDS, lineNumber)
    val country =
      countries[fields[COUNTRY_FIELD]]
        ?: malformed(
          lineNumber,
          "unknown country '${fields[COUNTRY_FIELD]}'",
        )
    val latitude = number(fields[LATITUDE_FIELD], lineNumber)
    val longitude = number(fields[LONGITUDE_FIELD], lineNumber)
    if (latitude !in -MAX_LATITUDE..MAX_LATITUDE || longitude !in -MAX_LONGITUDE..MAX_LONGITUDE) {
      malformed(lineNumber, "coordinates out of range")
    }

    return IndexedPlace(
      name = fields[NAME_FIELD],
      country = country,
      latitude = latitude,
      longitude = longitude,
      elevationMeters = fields[ELEVATION_FIELD].ifEmpty { null }?.let { number(it, lineNumber) },
      timeZoneId = fields[TIME_ZONE_FIELD],
      jewishCommunity =
        when (fields[FLAGS_FIELD]) {
          "0" -> false
          "1" -> true
          else ->
            malformed(
              lineNumber,
              "unknown flags '${fields[FLAGS_FIELD]}'",
            )
        },
    )
  }

  private fun malformed(lineNumber: Int, problem: String): Nothing =
    throw PlaceIndexFormatException("Line $lineNumber: $problem")

  private fun expectFields(fields: List<String>, count: Int, lineNumber: Int) {
    if (fields.size != count) {
      throw PlaceIndexFormatException(
        "Line $lineNumber: expected $count fields, found ${fields.size}"
      )
    }
  }

  private fun number(text: String, lineNumber: Int): Double =
    text.toDoubleOrNull()?.takeIf { it.isFinite() }
      ?: throw PlaceIndexFormatException("Line $lineNumber: not a number: '$text'")
}
