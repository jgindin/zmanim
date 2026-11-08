/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zmanlib.location

import java.io.IOException
import java.io.InputStream
import java.text.Normalizer
import java.util.Locale
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin
import kotlin.math.sqrt
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

/**
 * A [PlaceIndex] over the bundled place data, read from [open] the first time it is asked and kept
 * in memory. About 34,000 places is a few megabytes, and a scan over all of them takes a few
 * milliseconds, so there is no spatial index.
 *
 * Data that cannot be read or is malformed leaves the index empty rather than failing the caller:
 * the place index is a fallback for when the system geocoder cannot help, so a missing answer is
 * the safe one. [loadFailure] says why, for diagnostics. It is not read again.
 */
public class InMemoryPlaceIndex(
  private val open: () -> InputStream,
  private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : PlaceIndex {

  private val mutex = Mutex()

  private var places: List<SearchablePlace>? = null

  /** Why the data could not be read, or null if it was read (or has not been tried yet). */
  @Volatile
  public var loadFailure: Exception? = null
    private set

  override suspend fun nearest(latitude: Double, longitude: Double): IndexedPlace? {

    var best: IndexedPlace? = null
    var bestDistance = Double.MAX_VALUE
    for (candidate in loaded()) {
      val distance =
        greatCircleRadians(latitude, longitude, candidate.place.latitude, candidate.place.longitude)
      // Strictly less: of two equally near places, the one listed first (the larger) wins.
      if (distance < bestDistance) {
        best = candidate.place
        bestDistance = distance
      }
    }
    return best
  }

  /**
   * Exact matches first, then names that start with the text; each group in the data's order, which
   * lists larger places first. Matching ignores case, accents and apostrophes, and treats hyphens
   * as spaces.
   */
  override suspend fun byName(text: String): List<IndexedPlace> {

    val wanted = searchKey(text)
    if (wanted.isEmpty()) return emptyList()

    val candidates = loaded()
    val exact = candidates.filter { it.key == wanted }
    val prefix = candidates.filter { it.key != wanted && it.key.startsWith(wanted) }
    return (exact + prefix).map { it.place }
  }

  private suspend fun loaded(): List<SearchablePlace> = mutex.withLock {
    places ?: withContext(dispatcher) { load() }.also { places = it }
  }

  private fun load(): List<SearchablePlace> =
    try {
      open().bufferedReader(Charsets.UTF_8).useLines { lines ->
        PlaceIndexFormat.read(lines).map { SearchablePlace(it, searchKey(it.name)) }
      }
    } catch (e: IOException) {
      loadFailure = e
      emptyList()
    } catch (e: PlaceIndexFormatException) {
      loadFailure = e
      emptyList()
    }

  private class SearchablePlace(val place: IndexedPlace, val key: String)

  private companion object {

    private val COMBINING_MARKS = Regex("\\p{M}+")
    private val APOSTROPHES = Regex("['`‘’ʻʼʿʾ]")
    private val SEPARATORS = Regex("[\\s\\-]+")

    /**
     * Folds a name for matching: no accents, apostrophes or case; hyphens and runs of spaces as one
     * space.
     */
    fun searchKey(text: String): String =
      Normalizer.normalize(text, Normalizer.Form.NFD)
        .replace(COMBINING_MARKS, "")
        .replace(APOSTROPHES, "")
        .replace(SEPARATORS, " ")
        .trim()
        .lowercase(Locale.ROOT)

    /** The central angle between two points (haversine), which orders places by distance. */
    fun greatCircleRadians(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
      val phi1 = Math.toRadians(lat1)
      val phi2 = Math.toRadians(lat2)
      val halfDeltaPhi = (phi2 - phi1) / 2
      val halfDeltaLambda = Math.toRadians(lon2 - lon1) / 2
      val h =
        sin(halfDeltaPhi) * sin(halfDeltaPhi) +
          cos(phi1) * cos(phi2) * sin(halfDeltaLambda) * sin(halfDeltaLambda)
      return 2 * asin(min(1.0, sqrt(h)))
    }
  }
}
