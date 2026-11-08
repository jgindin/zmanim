/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.buildlogic.places

import java.time.DateTimeException
import java.time.ZoneId

/** One row of GeoNames' `cities15000.txt`, reduced to the columns the place index uses. */
data class GeoNamesCity(
  val id: Long,
  val name: String,
  val countryCode: String,
  val regionCode: String,
  val latitude: String,
  val longitude: String,
  val population: Long,
  val elevation: String,
  val dem: String,
  val timeZone: String,
)

/**
 * One row of `place-overrides.tsv`. [sourceName] is the record's name in GeoNames, so a GeoNames id
 * that comes to mean a different place fails the generator instead of being overridden silently. A
 * null field keeps the source's value.
 */
data class PlaceOverride(
  val id: Long,
  val sourceName: String,
  val countryCode: String?,
  val name: String?,
  val timeZone: String?,
)

/** A problem with the source data or the overrides; the generator writes nothing. */
class PlaceDataException(message: String) : Exception(message)

/** What [PlaceIndexData.build] produces: the asset's lines and what the maintainer reviews. */
data class PlaceIndexOutput(
  val lines: List<String>,
  val reviewOfPsRecords: List<String>,
  val elevationCounts: ElevationCounts,
)

/** How many places got their elevation from which GeoNames column. */
data class ElevationCounts(
  val fromElevation: Int,
  val fromDem: Int,
  val demNoData: Int,
  val none: Int,
)

/**
 * Turns the GeoNames files into the place index asset.
 *
 * The asset is UTF-8 text, one record per line, fields separated by tabs:
 * - `zmanim-places<TAB>1`: the first line, the format name and version.
 * - `#...`: a comment (the attribution).
 * - `C<TAB>code<TAB>name`: a country's display name, for every country code the places use.
 * - `P<TAB>name<TAB>country code<TAB>latitude<TAB>longitude<TAB>elevation<TAB>time zone<TAB>flags`:
 *   a place. Elevation is whole meters above sea level, or empty when unknown. Flags is `1` for a
 *   place marked as a Jewish community and `0` otherwise.
 *
 * Places are in descending order of population (then by GeoNames id), so a reader that keeps the
 * file's order lists the larger of two same-named places first.
 */
object PlaceIndexData {

  const val FORMAT_NAME: String = "zmanim-places"

  const val FORMAT_VERSION: Int = 1

  const val ATTRIBUTION: String =
    "Place data from GeoNames (geonames.org), licensed under CC BY 4.0 " +
      "(creativecommons.org/licenses/by/4.0). Filtered and converted for this app."

  /** GeoNames' `dem` value for "no data" (a cell over the sea, for example). */
  private const val DEM_NO_DATA = "-9999"

  private const val PALESTINE_PATTERN = "palestin"

  private const val CITY_COLUMNS = 19
  private const val COL_ID = 0
  private const val COL_NAME = 1
  private const val COL_LATITUDE = 4
  private const val COL_LONGITUDE = 5
  private const val COL_COUNTRY = 8
  private const val COL_REGION = 10
  private const val COL_POPULATION = 14
  private const val COL_ELEVATION = 15
  private const val COL_DEM = 16
  private const val COL_TIME_ZONE = 17

  private const val COUNTRY_COL_CODE = 0
  private const val COUNTRY_COL_NAME = 4

  private const val OVERRIDE_COLUMNS = 6
  private const val OVERRIDE_COL_ID = 0
  private const val OVERRIDE_COL_SOURCE_NAME = 1
  private const val OVERRIDE_COL_COUNTRY = 2
  private const val OVERRIDE_COL_NAME = 3
  private const val OVERRIDE_COL_TIME_ZONE = 4

  /** Parses `cities15000.txt`. */
  fun parseCities(lines: Sequence<String>): List<GeoNamesCity> =
    lines
      .withIndex()
      .filter { it.value.isNotEmpty() }
      .map { (index, line) ->
        val fields = line.split('\t')
        if (fields.size != CITY_COLUMNS) {
          throw PlaceDataException(
            "cities line ${index + 1}: expected $CITY_COLUMNS columns, found ${fields.size}"
          )
        }
        GeoNamesCity(
          id = fields[COL_ID].toLongOrFail("cities line ${index + 1} id"),
          name = fields[COL_NAME],
          countryCode = fields[COL_COUNTRY],
          regionCode = fields[COL_REGION],
          latitude = fields[COL_LATITUDE],
          longitude = fields[COL_LONGITUDE],
          population = fields[COL_POPULATION].toLongOrFail("cities line ${index + 1} population"),
          elevation = fields[COL_ELEVATION],
          dem = fields[COL_DEM],
          timeZone = fields[COL_TIME_ZONE],
        )
      }
      .toList()

  /** Parses `countryInfo.txt` into country code to display name. */
  fun parseCountries(lines: Sequence<String>): Map<String, String> =
    lines
      .filter { it.isNotEmpty() && !it.startsWith("#") }
      .map { it.split('\t') }
      .associate { it[COUNTRY_COL_CODE] to it[COUNTRY_COL_NAME] }

  /**
   * Parses `place-overrides.tsv`: `#` starts a comment line; each other line is GeoNames id, the
   * GeoNames name, country code, name, time zone and the reason, with an empty field meaning "keep
   * the source's value".
   */
  fun parseOverrides(lines: Sequence<String>): List<PlaceOverride> =
    lines
      .withIndex()
      .filter { it.value.isNotBlank() && !it.value.startsWith("#") }
      .map { (index, line) ->
        val fields = line.split('\t')
        if (fields.size != OVERRIDE_COLUMNS) {
          throw PlaceDataException(
            "overrides line ${index + 1}: expected $OVERRIDE_COLUMNS columns, found ${fields.size}"
          )
        }
        PlaceOverride(
          id = fields[OVERRIDE_COL_ID].toLongOrFail("overrides line ${index + 1} id"),
          sourceName = fields[OVERRIDE_COL_SOURCE_NAME],
          countryCode = fields[OVERRIDE_COL_COUNTRY].ifEmpty { null },
          name = fields[OVERRIDE_COL_NAME].ifEmpty { null },
          timeZone = fields[OVERRIDE_COL_TIME_ZONE].ifEmpty { null },
        )
      }
      .toList()

  /**
   * Applies [overrides] to [cities] and writes the asset's lines. Fails, listing every problem,
   * when an override names a record that does not exist or no longer has the expected name, uses an
   * unknown country or time zone, or moves a place into a country displayed as Palestine.
   */
  fun build(
    cities: List<GeoNamesCity>,
    countries: Map<String, String>,
    overrides: List<PlaceOverride>,
  ): PlaceIndexOutput {

    val citiesById = cities.associateBy { it.id }
    val overridesById = checkOverrides(overrides, citiesById, countries)

    val places =
      cities.sortedWith(compareByDescending<GeoNamesCity> { it.population }.thenBy { it.id }).map {
        city ->
        applyOverride(city, overridesById[city.id])
      }

    val missingCountries = places.map { it.countryCode }.filter { it !in countries }.toSortedSet()
    if (missingCountries.isNotEmpty()) {
      throw PlaceDataException("Country codes with no name in countryInfo: $missingCountries")
    }

    val lines = buildList {
      add("$FORMAT_NAME\t$FORMAT_VERSION")
      add("# $ATTRIBUTION")
      places
        .map { it.countryCode }
        .toSortedSet()
        .forEach { code -> add("C\t$code\t${countries.getValue(code)}") }
      places.forEach { place ->
        add(
          listOf(
              "P",
              place.name,
              place.countryCode,
              place.latitude,
              place.longitude,
              elevationOf(place),
              place.timeZone,
              "0",
            )
            .joinToString("\t")
        )
      }
    }

    return PlaceIndexOutput(
      lines = lines,
      reviewOfPsRecords = reviewOfPsRecords(cities, overridesById, countries),
      elevationCounts = countElevations(cities),
    )
  }

  /** The elevation the asset carries: GeoNames' `elevation`, else its `dem`, else unknown. */
  fun elevationOf(city: GeoNamesCity): String =
    when {
      city.elevation.isNotEmpty() -> city.elevation
      city.dem.isNotEmpty() && city.dem != DEM_NO_DATA -> city.dem
      else -> ""
    }

  private fun checkOverrides(
    overrides: List<PlaceOverride>,
    citiesById: Map<Long, GeoNamesCity>,
    countries: Map<String, String>,
  ): Map<Long, PlaceOverride> {

    val problems = mutableListOf<String>()
    overrides
      .groupBy { it.id }
      .filterValues { it.size > 1 }
      .keys
      .forEach { problems += "GeoNames id $it is overridden more than once" }

    for (override in overrides) {
      val city = citiesById[override.id]
      if (city == null) {
        problems += "GeoNames id ${override.id} (${override.sourceName}) is not in the source"
        continue
      }
      if (city.name != override.sourceName) {
        problems +=
          "GeoNames id ${override.id} is now named '${city.name}', not '${override.sourceName}'"
      }
      override.countryCode?.let { code ->
        val country = countries[code]
        when {
          country == null -> problems += "GeoNames id ${override.id}: unknown country $code"
          country.contains(PALESTINE_PATTERN, ignoreCase = true) ->
            problems += "GeoNames id ${override.id} would still be shown as in $country"
        }
      }
      override.timeZone?.let { zone ->
        if (!isTimeZone(zone)) {
          problems += "GeoNames id ${override.id}: unknown time zone $zone"
        }
      }
    }

    if (problems.isNotEmpty()) {
      throw PlaceDataException(problems.joinToString(separator = "\n"))
    }
    return overrides.associateBy { it.id }
  }

  private fun isTimeZone(zone: String): Boolean =
    try {
      ZoneId.of(zone)
      true
    } catch (_: DateTimeException) {
      false
    }

  private fun applyOverride(city: GeoNamesCity, override: PlaceOverride?): GeoNamesCity =
    if (override == null) {
      city
    } else {
      city.copy(
        name = override.name ?: city.name,
        countryCode = override.countryCode ?: city.countryCode,
        timeZone = override.timeZone ?: city.timeZone,
      )
    }

  private fun reviewOfPsRecords(
    cities: List<GeoNamesCity>,
    overridesById: Map<Long, PlaceOverride>,
    countries: Map<String, String>,
  ): List<String> =
    cities
      .filter { it.countryCode == "PS" }
      .sortedBy { it.name }
      .map { city ->
        val result = applyOverride(city, overridesById[city.id])
        val shown = "${result.name}, ${countries[result.countryCode]} (${result.timeZone})"
        val status = if (city.id in overridesById) "overridden: $shown" else "unchanged: $shown"
        "${city.id}\t${city.name}\tPS.${city.regionCode}\t$status"
      }

  private fun countElevations(cities: List<GeoNamesCity>): ElevationCounts {
    val withoutElevation = cities.filter { it.elevation.isEmpty() }
    return ElevationCounts(
      fromElevation = cities.size - withoutElevation.size,
      fromDem = withoutElevation.count { it.dem.isNotEmpty() && it.dem != DEM_NO_DATA },
      demNoData = withoutElevation.count { it.dem == DEM_NO_DATA },
      none = withoutElevation.count { it.dem.isEmpty() },
    )
  }

  private fun String.toLongOrFail(what: String): Long =
    toLongOrNull() ?: throw PlaceDataException("$what is not a number: '$this'")
}
