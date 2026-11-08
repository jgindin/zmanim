/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.buildlogic.places

import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class UT_PlaceIndexData {

  private val countries =
    mapOf("IL" to "Israel", "PS" to "Palestinian Territory", "US" to "United States")

  private fun city(
    id: Long,
    name: String,
    country: String = "US",
    population: Long = 100_000,
    elevation: String = "",
    dem: String = "10",
    timeZone: String = "America/New_York",
  ) =
    GeoNamesCity(
      id = id,
      name = name,
      countryCode = country,
      regionCode = "01",
      latitude = "1.5",
      longitude = "-2.25",
      population = population,
      elevation = elevation,
      dem = dem,
      timeZone = timeZone,
    )

  private fun override(
    id: Long,
    sourceName: String,
    country: String? = null,
    name: String? = null,
    timeZone: String? = null,
  ) = PlaceOverride(id, sourceName, country, name, timeZone)

  private fun placeLines(output: PlaceIndexOutput) = output.lines.filter { it.startsWith("P\t") }

  @Test
  fun theAssetStartsWithItsFormatAndVersionThenTheAttribution() {
    val lines = PlaceIndexData.build(listOf(city(1, "Albany")), countries, emptyList()).lines

    assertEquals("zmanim-places\t1", lines[0])
    assertEquals("# ${PlaceIndexData.ATTRIBUTION}", lines[1])
  }

  @Test
  fun aPlaceIsWrittenWithItsCountryCoordinatesElevationAndTimeZone() {
    val output = PlaceIndexData.build(listOf(city(1, "Albany", dem = "45")), countries, emptyList())

    assertEquals(
      listOf("P\tAlbany\tUS\t1.5\t-2.25\t45\tAmerica/New_York\t0"),
      placeLines(output),
    )
  }

  @Test
  fun onlyTheCountriesThePlacesUseAreListed() {
    val output =
      PlaceIndexData.build(
        listOf(city(1, "Haifa", country = "IL"), city(2, "Albany")),
        countries,
        emptyList(),
      )

    assertEquals(
      listOf("C\tIL\tIsrael", "C\tUS\tUnited States"),
      output.lines.filter { it.startsWith("C\t") },
    )
  }

  @Test
  fun aCountryWithNoNameFailsTheGenerator() {
    assertFailsWith<PlaceDataException> {
      PlaceIndexData.build(listOf(city(1, "Somewhere", country = "ZZ")), countries, emptyList())
    }
  }

  @Test
  fun placesAreInDescendingOrderOfPopulationThenById() {
    val output =
      PlaceIndexData.build(
        listOf(
          city(3, "Small", population = 20_000),
          city(2, "Big B", population = 900_000),
          city(1, "Big A", population = 900_000),
        ),
        countries,
        emptyList(),
      )

    assertEquals(listOf("Big A", "Big B", "Small"), placeLines(output).map { it.split('\t')[1] })
  }

  @Test
  fun theElevationColumnIsPreferredOverTheDem() {
    assertEquals("12", PlaceIndexData.elevationOf(city(1, "A", elevation = "12", dem = "40")))
  }

  @Test
  fun theDemIsUsedWhenThereIsNoElevation() {
    assertEquals("40", PlaceIndexData.elevationOf(city(1, "A", dem = "40")))
  }

  @Test
  fun theDemsNoDataValueMeansTheElevationIsUnknown() {
    assertEquals("", PlaceIndexData.elevationOf(city(1, "A", dem = "-9999")))
  }

  @Test
  fun theCountsSayWhereEachElevationCameFrom() {
    val counts =
      PlaceIndexData.build(
          listOf(
            city(1, "A", elevation = "5"),
            city(2, "B", dem = "7"),
            city(3, "C", dem = "-9999"),
            city(4, "D", dem = ""),
          ),
          countries,
          emptyList(),
        )
        .elevationCounts

    assertEquals(ElevationCounts(fromElevation = 1, fromDem = 1, demNoData = 1, none = 1), counts)
  }

  @Test
  fun anOverrideReplacesOnlyTheFieldsItGives() {
    val output =
      PlaceIndexData.build(
        listOf(
          city(1, "Hebron", country = "PS", dem = "903", timeZone = "Asia/Hebron"),
          city(2, "Battir", country = "PS", dem = "675", timeZone = "Asia/Jerusalem"),
        ),
        countries,
        listOf(
          override(1, "Hebron", name = "Hevron", timeZone = "Asia/Jerusalem"),
          override(2, "Battir", country = "IL", name = "Beitar Illit"),
        ),
      )

    assertEquals(
      listOf(
        "P\tHevron\tPS\t1.5\t-2.25\t903\tAsia/Jerusalem\t0",
        "P\tBeitar Illit\tIL\t1.5\t-2.25\t675\tAsia/Jerusalem\t0",
      ),
      placeLines(output),
    )
  }

  @Test
  fun anOverrideThatWouldStillShowPalestineFailsTheGenerator() {
    val failure =
      assertFailsWith<PlaceDataException> {
        PlaceIndexData.build(
          listOf(city(1, "Old City", country = "IL")),
          countries,
          listOf(override(1, "Old City", country = "PS")),
        )
      }

    assertContains(failure.message.orEmpty(), "Palestinian Territory")
  }

  /** Guards against a GeoNames id coming to mean another place. */
  @Test
  fun anOverrideWhoseRecordHasAnotherNameFailsTheGenerator() {
    assertFailsWith<PlaceDataException> {
      PlaceIndexData.build(
        listOf(city(1, "Somewhere Else")),
        countries,
        listOf(override(1, "Old City", country = "IL")),
      )
    }
  }

  @Test
  fun anOverrideOfAMissingRecordFailsTheGenerator() {
    assertFailsWith<PlaceDataException> {
      PlaceIndexData.build(listOf(city(1, "A")), countries, listOf(override(2, "B", name = "C")))
    }
  }

  @Test
  fun anOverrideWithAnUnknownCountryFailsTheGenerator() {
    assertFailsWith<PlaceDataException> {
      PlaceIndexData.build(
        listOf(city(1, "A")),
        countries,
        listOf(override(1, "A", country = "ZZ")),
      )
    }
  }

  @Test
  fun anOverrideWithAnUnknownTimeZoneFailsTheGenerator() {
    assertFailsWith<PlaceDataException> {
      PlaceIndexData.build(
        listOf(city(1, "A")),
        countries,
        listOf(override(1, "A", timeZone = "Asia/Nowhere")),
      )
    }
  }

  @Test
  fun aPlaceOverriddenTwiceFailsTheGenerator() {
    assertFailsWith<PlaceDataException> {
      PlaceIndexData.build(
        listOf(city(1, "A")),
        countries,
        listOf(override(1, "A", name = "B"), override(1, "A", name = "C")),
      )
    }
  }

  @Test
  fun theReviewListsEveryPsRecordAndWhatItBecomes() {
    val output =
      PlaceIndexData.build(
        listOf(
          city(1, "Ramallah", country = "PS", timeZone = "Asia/Hebron"),
          city(2, "Old City", country = "PS", timeZone = "Asia/Hebron"),
          city(3, "Albany"),
        ),
        countries,
        listOf(override(2, "Old City", country = "IL", timeZone = "Asia/Jerusalem")),
      )

    assertEquals(
      listOf(
        "2\tOld City\tPS.01\toverridden: Old City, Israel (Asia/Jerusalem)",
        "1\tRamallah\tPS.01\tunchanged: Ramallah, Palestinian Territory (Asia/Hebron)",
      ),
      output.reviewOfPsRecords,
    )
  }

  @Test
  fun citiesAreParsedFromGeoNamesColumns() {
    val line =
      listOf(
          "293397",
          "Tel Aviv",
          "Tel Aviv",
          "TLV,Tel Aviv-Yafo",
          "32.08088",
          "34.78057",
          "P",
          "PPLA",
          "IL",
          "",
          "05",
          "",
          "",
          "",
          "432892",
          "",
          "15",
          "Asia/Jerusalem",
          "2023-01-01",
        )
        .joinToString("\t")

    assertEquals(
      listOf(
        GeoNamesCity(
          id = 293397,
          name = "Tel Aviv",
          countryCode = "IL",
          regionCode = "05",
          latitude = "32.08088",
          longitude = "34.78057",
          population = 432892,
          elevation = "",
          dem = "15",
          timeZone = "Asia/Jerusalem",
        )
      ),
      PlaceIndexData.parseCities(sequenceOf(line)),
    )
  }

  @Test
  fun aCitiesLineWithTheWrongNumberOfColumnsFailsTheGenerator() {
    assertFailsWith<PlaceDataException> { PlaceIndexData.parseCities(sequenceOf("1\tShort")) }
  }

  @Test
  fun countriesAreParsedAndCommentsSkipped() {
    assertEquals(
      mapOf("IL" to "Israel"),
      PlaceIndexData.parseCountries(
        sequenceOf("#ISO\tISO3\tISO-Numeric\tfips\tCountry", "IL\tISR\t376\tIS\tIsrael\tJerusalem")
      ),
    )
  }

  @Test
  fun overridesAreParsedWithEmptyFieldsKeepingTheSource() {
    assertEquals(
      listOf(override(285066, "Hebron", name = "Hevron", timeZone = "Asia/Jerusalem")),
      PlaceIndexData.parseOverrides(
        sequenceOf("# comment", "", "285066\tHebron\t\tHevron\tAsia/Jerusalem\tJewish name")
      ),
    )
  }
}
