/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zmanlib.location

import java.util.TimeZone
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

/**
 * [GeocodeCacheResolver] and [PlaceIndexResolver], which put the cache and the data in the chain.
 */
class UT_PlaceSourceAdapters {

  private val newYork = TimeZone.getTimeZone("America/New_York")

  private val albany =
    ZmanimLocation.Builder()
      .locationName("Albany, New York")
      .atLatitude(42.6526)
      .atLongitude(-73.7562)
      .inTimeZone(newYork)
      .build()

  private val typedAlbany =
    ZmanimLocation.Builder().locationName("albany").inTimeZone(newYork).build()

  private fun fix(latitude: Double, longitude: Double) =
    ZmanimLocation.Builder()
      .fromProvider("fused")
      .atLatitude(latitude)
      .atLongitude(longitude)
      .atElevation(FIX_ELEVATION)
      .inTimeZone(newYork)
      .build()

  @Nested
  inner class FromTheCache {

    private val stored = mutableListOf<ZmanimLocation>()

    private val cache =
      object : GeocodeCache {
        override suspend fun find(request: ZmanimLocation): ZmanimLocation? = stored.firstOrNull()

        override suspend fun store(location: ZmanimLocation) {
          stored += location
        }
      }

    @Test
    fun aCachedPlaceIsFoundByNameAndByCoordinates() = runTest {
      stored += albany

      assertSame(
        albany,
        (GeocodeCacheResolver(cache).fromName(typedAlbany) as PlaceResult.Found).location,
      )
      assertSame(
        albany,
        (GeocodeCacheResolver(cache).fromCoordinates(fix(42.65, -73.75)) as PlaceResult.Found)
          .location,
      )
    }

    @Test
    fun aPlaceNotCachedIsNotFound() = runTest {
      assertEquals(PlaceResult.NotFound, GeocodeCacheResolver(cache).fromName(typedAlbany))
      assertEquals(PlaceResult.NotFound, GeocodeCacheResolver(cache).fromCoordinates(fix(1.0, 1.0)))
    }
  }

  @Nested
  inner class FromThePlaceData {

    private val index =
      InMemoryPlaceIndex({
        PlaceData.text(
            mapOf("US" to "United States"),
            PlaceData.place("Albany", 42.6526, -73.7562, country = "US", elevation = "45"),
            PlaceData.place("Albany", 31.5785, -84.1557, country = "US"),
          )
          .byteInputStream()
      })

    private val resolver = PlaceIndexResolver(index)

    @Test
    fun aTypedNameIsTheBestMatchAtItsCoordinatesInTheRequestedTimeZone() = runTest {
      val chicago = TimeZone.getTimeZone("America/Chicago")
      val typed = ZmanimLocation.Builder().locationName("albany").inTimeZone(chicago).build()

      val found = (resolver.fromName(typed) as PlaceResult.Found).location

      assertEquals("Albany, United States", found.locationName)
      assertEquals("albany", found.originalUserName)
      assertEquals(42.6526, found.latitude)
      assertEquals(-73.7562, found.longitude)
      assertEquals(chicago, found.timeZone)
    }

    @Test
    fun anUnknownNameIsNotFound() = runTest {
      val typed = ZmanimLocation.Builder().locationName("Atlantis").inTimeZone(newYork).build()

      assertEquals(PlaceResult.NotFound, resolver.fromName(typed))
    }

    @Test
    fun aFixNearAPlaceIsNamedAfterItAndKeepsEverythingElse() = runTest {
      // About 20 km west of Albany.
      val fix = fix(42.6526, -73.9998)

      val found = (resolver.fromCoordinates(fix) as PlaceResult.Found).location

      assertEquals("Albany, United States", found.locationName)
      assertEquals(fix.latitude, found.latitude)
      assertEquals(fix.longitude, found.longitude)
      assertEquals(FIX_ELEVATION, found.elevation)
      assertEquals("fused", found.providerName)
      assertEquals(newYork, found.timeZone)
    }

    @Test
    fun aFixFarFromEveryPlaceIsNotFound() = runTest {
      // About 30 km west of Albany.
      assertEquals(PlaceResult.NotFound, resolver.fromCoordinates(fix(42.6526, -74.1229)))
    }

    @Test
    fun noPlaceDataMeansNotFound() = runTest {
      val empty =
        PlaceIndexResolver(InMemoryPlaceIndex({ PlaceData.text(emptyMap()).byteInputStream() }))

      assertEquals(PlaceResult.NotFound, empty.fromCoordinates(fix(42.6526, -73.7562)))
      assertEquals(PlaceResult.NotFound, empty.fromName(typedAlbany))
    }
  }

  private companion object {
    const val FIX_ELEVATION = 120.0
  }
}
