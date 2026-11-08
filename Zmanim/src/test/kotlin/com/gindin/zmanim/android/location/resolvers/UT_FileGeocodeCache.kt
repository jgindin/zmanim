/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zmanim.android.location.resolvers

import com.gindin.zmanlib.location.ZmanimLocation
import java.io.DataOutputStream
import java.io.File
import java.util.TimeZone
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

/**
 * [FileGeocodeCache]: the cache file the released app wrote, the file it writes now, and the rule
 * for which cached place answers a request.
 */
class UT_FileGeocodeCache {

  @get:Rule val folder = TemporaryFolder()

  private lateinit var directory: File

  @Before
  fun setUp() {
    directory = folder.newFolder("files")
  }

  private fun cache() = FileGeocodeCache(directory)

  private fun test(body: suspend CoroutineScope.() -> Unit) = runBlocking {
    withTimeout(10.seconds) { body() }
  }

  private val current
    get() = File(directory, FileGeocodeCache.FILE_NAME)

  private val legacy
    get() = File(directory, FileGeocodeCache.LEGACY_FILE_NAME)

  // --- The file the released app wrote.

  @Test
  fun readsTheCacheTheReleasedAppWrote() = test {
    installReleasedCache()

    val cache = cache()

    assertEquals("Albany, New York", cache.find(nearAlbany())?.locationName)
    val saratoga = cache.find(typed("Saratoga Springs, NY", NEW_YORK))
    assertEquals("Saratoga Springs, New York", saratoga?.locationName)
    assertEquals(SARATOGA_LATITUDE, saratoga!!.latitude, 0.0)
    assertEquals("Jerusalem, Israel", cache.find(nearJerusalem())?.locationName)
  }

  @Test
  fun movesTheReleasedCacheToTheNewFileOnTheFirstStore() = test {
    installReleasedCache()

    cache().store(place("Boston, Massachusetts", BOSTON_LATITUDE, BOSTON_LONGITUDE))

    assertFalse(legacy.exists())
    assertTrue(current.readLines().first().startsWith("zmanim-geocode-cache\t1\t5"))
    val reopened = cache()
    assertEquals("Albany, New York", reopened.find(nearAlbany())?.locationName)
    assertEquals("Boston, Massachusetts", reopened.find(nearBoston())?.locationName)
  }

  @Test
  fun aTruncatedReleasedCacheIsDiscarded() = test {
    installReleasedCache()
    legacy.writeBytes(legacy.readBytes().copyOf(legacy.length().toInt() / 2))

    assertNull(cache().find(nearAlbany()))
  }

  @Test
  fun aReleasedCacheWithANegativeCountIsDiscarded() = test {
    DataOutputStream(legacy.outputStream()).use { it.writeInt(-1) }

    assertNull(cache().find(nearAlbany()))
  }

  @Test
  fun theNewFileWinsOverAReleasedCacheLeftBehind() = test {
    cache().store(place("Boston, Massachusetts", BOSTON_LATITUDE, BOSTON_LONGITUDE))
    installReleasedCache()

    val cache = cache()

    assertNull(cache.find(nearAlbany()))
    assertNotNull(cache.find(nearBoston()))
  }

  // --- The file it writes.

  @Test
  fun aPlaceSurvivesARoundTrip() = test {
    val boston =
      ZmanimLocation.Builder()
        .fromProvider("fused")
        .locationName("Boston, Massachusetts")
        .userName("boston")
        .atLatitude(BOSTON_LATITUDE)
        .atLongitude(BOSTON_LONGITUDE)
        .atElevation(BOSTON_ELEVATION)
        .withAccuracy(BOSTON_ACCURACY)
        .inTimeZone(NEW_YORK)
        .build()
    cache().store(boston)

    val found = cache().find(typed("boston", NEW_YORK))!!

    assertEquals("Boston, Massachusetts", found.locationName)
    assertEquals("boston", found.originalUserName)
    assertEquals(BOSTON_LATITUDE, found.latitude, 0.0)
    assertEquals(BOSTON_LONGITUDE, found.longitude, 0.0)
    assertEquals(BOSTON_ELEVATION, found.elevation, 0.0)
    assertEquals(BOSTON_ACCURACY, found.accuracy, 0f)
    assertEquals("fused", found.providerName)
    assertEquals(NEW_YORK, found.timeZone)
    assertFalse(File(directory, "${FileGeocodeCache.FILE_NAME}.tmp").exists())
  }

  @Test
  fun aFileCutShortAtALineIsDiscarded() = test {
    val cache = cache()
    cache.store(place("Boston, Massachusetts", BOSTON_LATITUDE, BOSTON_LONGITUDE))
    cache.store(place("Albany, New York", ALBANY_LATITUDE, ALBANY_LONGITUDE))
    current.writeText(current.readLines().dropLast(1).joinToString("\n", postfix = "\n"))

    assertNull(cache().find(nearBoston()))
  }

  @Test
  fun aFileCutShortInsideALineIsDiscarded() = test {
    cache().store(place("Boston, Massachusetts", BOSTON_LATITUDE, BOSTON_LONGITUDE))
    current.writeText(current.readText().dropLast(10))

    assertNull(cache().find(nearBoston()))
  }

  @Test
  fun aFileOfAnotherVersionIsDiscarded() = test {
    cache().store(place("Boston, Massachusetts", BOSTON_LATITUDE, BOSTON_LONGITUDE))
    current.writeText(current.readText().replaceFirst("\t1\t", "\t2\t"))

    assertNull(cache().find(nearBoston()))
  }

  @Test
  fun aDiscardedFileIsReplacedByTheNextStore() = test {
    current.writeText("not a cache\n")
    val cache = cache()

    cache.store(place("Boston, Massachusetts", BOSTON_LATITUDE, BOSTON_LONGITUDE))

    assertNotNull(cache().find(nearBoston()))
  }

  @Test
  fun aFailedSaveIsReportedAndTheNextOneTriesAgain() = test {
    val cache = cache()
    directory.setWritable(false)
    try {
      cache.store(place("Boston, Massachusetts", BOSTON_LATITUDE, BOSTON_LONGITUDE))
      assertNotNull(cache.lastWriteFailure)
    } finally {
      directory.setWritable(true)
    }

    cache.store(place("Albany, New York", ALBANY_LATITUDE, ALBANY_LONGITUDE))

    assertNull(cache.lastWriteFailure)
    assertNotNull(cache().find(nearBoston()))
  }

  @Test
  fun clearForgetsEverythingOnDiskToo() = test {
    installReleasedCache()
    val cache = cache()

    cache.clear()

    assertNull(cache.find(nearAlbany()))
    assertNull(cache().find(nearAlbany()))
    assertFalse(legacy.exists())
  }

  // --- Which cached place answers a request.

  @Test
  fun aPlaceFoundByNameComesBackInTheRequestedTimeZoneWithoutChangingTheEntry() = test {
    val cache = cache()
    cache.store(typedPlace("boston", NEW_YORK))

    val inChicago = cache.find(typed("boston", CHICAGO))
    val inNewYork = cache.find(typed("boston", NEW_YORK))

    assertEquals(CHICAGO, inChicago?.timeZone)
    assertEquals(NEW_YORK, inNewYork?.timeZone)
    // A different time zone is a significant change, so this finds the entry only if it is still
    //  in New York's.
    assertNotNull(cache.find(fix(BOSTON_LATITUDE, BOSTON_LONGITUDE)))
    assertNotNull(cache().find(fix(BOSTON_LATITUDE, BOSTON_LONGITUDE)))
  }

  @Test
  fun aPlaceIsFoundWithinAKilometer() = test {
    val cache = cache()
    cache.store(place("Boston, Massachusetts", BOSTON_LATITUDE, BOSTON_LONGITUDE))

    assertNotNull(cache.find(nearBoston()))
    // About 1.1 km north.
    assertNull(cache.find(fix(BOSTON_LATITUDE + 0.01, BOSTON_LONGITUDE)))
  }

  @Test
  fun aPlaceIsNotFoundByCoordinatesInAnotherTimeZone() = test {
    val cache = cache()
    cache.store(place("Boston, Massachusetts", BOSTON_LATITUDE, BOSTON_LONGITUDE))

    assertNull(cache.find(fix(BOSTON_LATITUDE, BOSTON_LONGITUDE, CHICAGO)))
  }

  @Test
  fun aMatchByNameWinsOverAMatchByCoordinates() = test {
    val cache = cache()
    cache.store(place("Boston, Massachusetts", BOSTON_LATITUDE, BOSTON_LONGITUDE))
    cache.store(typedPlace("albany", NEW_YORK))
    val request =
      ZmanimLocation.Builder()
        .locationName("albany")
        .atLatitude(BOSTON_LATITUDE)
        .atLongitude(BOSTON_LONGITUDE)
        .inTimeZone(NEW_YORK)
        .build()

    assertEquals("Albany, New York", cache.find(request)?.locationName)
  }

  @Test
  fun anUnresolvedPlaceIsNotStored() = test {
    val cache = cache()

    cache.store(fix(BOSTON_LATITUDE, BOSTON_LONGITUDE))

    assertNull(cache.find(nearBoston()))
    assertFalse(current.exists())
  }

  @Test
  fun aPlaceAlreadyAnsweredIsNotStoredAgain() = test {
    val cache = cache()
    cache.store(place("Boston, Massachusetts", BOSTON_LATITUDE, BOSTON_LONGITUDE))

    cache.store(place("Beacon Hill, Boston", BOSTON_LATITUDE + 0.001, BOSTON_LONGITUDE))

    assertEquals("Boston, Massachusetts", cache().find(nearBoston())?.locationName)
    assertEquals("1", current.readLines().first().split('\t')[2])
  }

  @Test
  fun theOldestPlaceIsDroppedBeyondTheLimit() = test {
    val cache = cache()
    repeat(FileGeocodeCache.MAX_ENTRIES + 1) { i -> cache.store(place("Place $i", 10.0 + i, 20.0)) }

    val reopened = cache()
    assertNull(reopened.find(fix(10.0, 20.0)))
    assertEquals("Place 1", reopened.find(fix(11.0, 20.0))?.locationName)
    assertEquals(
      "Place ${FileGeocodeCache.MAX_ENTRIES}",
      reopened.find(fix(10.0 + FileGeocodeCache.MAX_ENTRIES, 20.0))?.locationName,
    )
  }

  private fun installReleasedCache() {
    val fixture =
      checkNotNull(javaClass.classLoader?.getResourceAsStream(RELEASED_CACHE)) { RELEASED_CACHE }
    fixture.use { input -> legacy.outputStream().use { input.copyTo(it) } }
  }

  private companion object {
    const val RELEASED_CACHE = "upgrade/zmanim-v409/files/CachedLocations"
    val NEW_YORK: TimeZone = TimeZone.getTimeZone("America/New_York")
    val CHICAGO: TimeZone = TimeZone.getTimeZone("America/Chicago")
    val JERUSALEM: TimeZone = TimeZone.getTimeZone("Asia/Jerusalem")
    const val SARATOGA_LATITUDE = 43.0831
    const val ALBANY_LATITUDE = 42.6526
    const val ALBANY_LONGITUDE = -73.7562
    const val BOSTON_LATITUDE = 42.3601
    const val BOSTON_LONGITUDE = -71.0589
    const val BOSTON_ELEVATION = 43.0
    const val BOSTON_ACCURACY = 12.5f

    fun fix(latitude: Double, longitude: Double, timeZone: TimeZone = NEW_YORK): ZmanimLocation =
      ZmanimLocation.Builder()
        .atLatitude(latitude)
        .atLongitude(longitude)
        .inTimeZone(timeZone)
        .build()

    fun place(name: String, latitude: Double, longitude: Double): ZmanimLocation =
      ZmanimLocation.Builder()
        .locationName(name)
        .atLatitude(latitude)
        .atLongitude(longitude)
        .inTimeZone(NEW_YORK)
        .build()

    fun typed(name: String, timeZone: TimeZone): ZmanimLocation =
      ZmanimLocation.Builder().locationName(name).inTimeZone(timeZone).build()

    fun typedPlace(name: String, timeZone: TimeZone): ZmanimLocation =
      ZmanimLocation.Builder()
        .locationName(if (name == "albany") "Albany, New York" else "Boston, Massachusetts")
        .userName(name)
        .atLatitude(if (name == "albany") ALBANY_LATITUDE else BOSTON_LATITUDE)
        .atLongitude(if (name == "albany") ALBANY_LONGITUDE else BOSTON_LONGITUDE)
        .inTimeZone(timeZone)
        .build()

    /** About 100 meters from the released cache's Albany, at its elevation. */
    fun nearAlbany(): ZmanimLocation =
      ZmanimLocation.Builder()
        .atLatitude(42.6531)
        .atLongitude(-73.7568)
        .atElevation(45.0)
        .inTimeZone(NEW_YORK)
        .build()

    fun nearJerusalem(): ZmanimLocation =
      ZmanimLocation.Builder()
        .atLatitude(31.7690)
        .atLongitude(35.2140)
        .atElevation(750.0)
        .inTimeZone(JERUSALEM)
        .build()

    /** About 110 meters from Boston. */
    fun nearBoston(): ZmanimLocation = fix(BOSTON_LATITUDE + 0.001, BOSTON_LONGITUDE)
  }
}
