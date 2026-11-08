/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zmanim.android.location.resolvers

import com.gindin.zmanlib.location.GeocodeCache
import com.gindin.zmanlib.location.ZmanLocationJSON
import com.gindin.zmanlib.location.ZmanimLocation
import java.io.DataInputStream
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

/**
 * The places already looked up, kept in [directory] (the app's files directory).
 *
 * The file is text: a line with the format's name, its version and the number of places, then one
 * place per line in the [ZmanLocationJSON] format. The count catches a file cut short at the end of
 * a line, which would otherwise read as a shorter cache. The file is replaced whole on every
 * change, through a temporary file renamed over it, so a crash leaves the old file or the new one
 * and never half of either. Until it is first written, the entries come from the file the app has
 * always kept (`CachedLocations`: a count, then that many locations as modified UTF-8), which is
 * deleted once its entries are in the new file. A file that cannot be read is discarded, as it
 * always has been: the cache only saves lookups.
 *
 * At most [MAX_ENTRIES] places are kept; storing one more drops the one stored longest ago.
 */
class FileGeocodeCache(
  private val directory: File,
  private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : GeocodeCache {

  private val mutex = Mutex()

  /** Oldest first; null until the files are read. */
  private var entries: MutableList<ZmanimLocation>? = null

  /** Why the cache could last not be saved, or null if the last save worked. */
  @Volatile
  var lastWriteFailure: IOException? = null
    private set

  /**
   * The place the user typed under the request's name, or else a named place within a kilometer of
   * the request's coordinates and 5 meters of its elevation, in the same time zone (see
   * [ZmanimLocation.isSignificantChangeFrom]). A place found by name comes back in the request's
   * time zone, since the user may have chosen one; the cached entry itself is not changed.
   */
  override suspend fun find(request: ZmanimLocation): ZmanimLocation? = mutex.withLock {
    val found = match(loaded(), request) ?: return@withLock null
    if (found.timeZone == request.timeZone) {
      found
    } else {
      ZmanimLocation.Builder().from(found).inTimeZone(request.timeZone).build()
    }
  }

  /** Keeps a place that has a name and coordinates, unless a cached place already answers it. */
  override suspend fun store(location: ZmanimLocation) {

    if (!location.isResolved()) {
      return
    }

    mutex.withLock {
      val entries = loaded()
      if (match(entries, location) != null) {
        return@withLock
      }

      entries.add(location)
      while (entries.size > MAX_ENTRIES) {
        entries.removeAt(0)
      }
      write(entries)
    }
  }

  /** Forgets every place, on disk too. */
  suspend fun clear() {
    mutex.withLock {
      val entries = loaded()
      entries.clear()
      write(entries)
    }
  }

  private fun match(entries: List<ZmanimLocation>, request: ZmanimLocation): ZmanimLocation? =
    entries.firstOrNull { request.locationName == it.originalUserName }
      ?: entries.lastOrNull { it.hasName() && !request.isSignificantChangeFrom(it) }

  private suspend fun loaded(): MutableList<ZmanimLocation> =
    entries ?: withContext(dispatcher) { read() }.toMutableList().also { entries = it }

  private fun read(): List<ZmanimLocation> {

    val current = File(directory, FILE_NAME)
    val legacy = File(directory, LEGACY_FILE_NAME)
    return when {
      current.isFile -> readOrNothing { readCurrent(current) }
      legacy.isFile -> readOrNothing { readLegacy(legacy) }
      else -> emptyList()
    }
  }

  private fun readOrNothing(read: () -> List<ZmanimLocation>): List<ZmanimLocation> =
    try {
      read()
    } catch (_: IOException) {
      emptyList()
    } catch (_: IllegalArgumentException) {
      // Includes kotlinx.serialization's SerializationException: a place that is not valid JSON.
      emptyList()
    }

  private fun readCurrent(file: File): List<ZmanimLocation> {

    val lines = file.readLines(Charsets.UTF_8)
    val places = lines.drop(1)
    if (lines.firstOrNull() != header(places.size)) {
      throw IOException("$file is not a complete $FORMAT_NAME $FORMAT_VERSION file")
    }

    return places.map { ZmanLocationJSON.fromJSON(it) }
  }

  private fun readLegacy(file: File): List<ZmanimLocation> =
    DataInputStream(file.inputStream().buffered()).use { input ->
      val count = input.readInt()
      if (count < 0) {
        throw IOException("$file holds a negative count")
      }
      List(count) { ZmanimLocation.load(input) }
    }

  private suspend fun write(entries: List<ZmanimLocation>) {

    val text = buildString {
      append(header(entries.size)).append('\n')
      entries.forEach { append(oneLine(ZmanLocationJSON.toJSON(it))).append('\n') }
    }

    withContext(dispatcher) {
      val temporary = File(directory, "$FILE_NAME.tmp")
      try {
        temporary.writeText(text, Charsets.UTF_8)
        Files.move(
          temporary.toPath(),
          File(directory, FILE_NAME).toPath(),
          StandardCopyOption.REPLACE_EXISTING,
          StandardCopyOption.ATOMIC_MOVE,
        )
        File(directory, LEGACY_FILE_NAME).delete()
        lastWriteFailure = null
      } catch (e: IOException) {
        temporary.delete()
        lastWriteFailure = e
      }
    }
  }

  private fun header(count: Int): String = "$FORMAT_NAME\t$FORMAT_VERSION\t$count"

  /** [ZmanLocationJSON] indents its output over several lines; the file has one place a line. */
  private fun oneLine(json: String): String = Json.parseToJsonElement(json).toString()

  companion object {

    /** As many places as the app has always kept. */
    const val MAX_ENTRIES: Int = 25

    const val FILE_NAME: String = "GeocodeCache"

    /** The file the app has always kept its cache in. */
    const val LEGACY_FILE_NAME: String = "CachedLocations"

    private const val FORMAT_NAME = "zmanim-geocode-cache"
    private const val FORMAT_VERSION = 1
  }
}
