/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zmanlib.location

import java.io.BufferedInputStream
import java.io.ByteArrayOutputStream
import java.io.DataInputStream
import java.io.EOFException
import java.io.IOException
import java.io.InputStream
import kotlin.math.floor
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

/**
 * How far mean sea level is above the WGS 84 ellipsoid, which is what GPS altitudes measure from.
 */
public fun interface GeoidModel {

  /** The geoid height at the point in meters, or null when it is not known. */
  public suspend fun heightMeters(latitude: Double, longitude: Double): Double?
}

/**
 * A [GeoidModel] over the bundled geoid grid, read from [open] the first time it is asked and kept
 * in memory (181 by 360 heights, about 130 KB). Between the grid's nodes the height is interpolated
 * bilinearly, which is within a meter of the full model almost everywhere.
 *
 * The grid is two lines of UTF-8 text and then the heights:
 * - `zmanim-geoid<TAB>1<TAB>rows<TAB>columns`: the format name, version and grid shape.
 * - `# ...`: the source of the data.
 * - rows × columns signed 16-bit big-endian integers, the heights in decimeters, row by row: rows
 *   from 90° N southwards, `180 / (rows - 1)` degrees apart; columns from 0° eastwards, `360 /
 *   columns` degrees apart.
 *
 * A grid that cannot be read or is malformed answers null everywhere rather than failing the
 * caller, so the elevation falls back to the next source. [loadFailure] says why, for diagnostics.
 * It is not read again.
 */
public class BundledGeoidGrid(
  private val open: () -> InputStream,
  private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : GeoidModel {

  private val mutex = Mutex()

  private var grid: Grid? = null

  private var tried = false

  /** Why the grid could not be read, or null if it was read (or has not been tried yet). */
  @Volatile
  public var loadFailure: Exception? = null
    private set

  override suspend fun heightMeters(latitude: Double, longitude: Double): Double? {
    if (!latitude.isFinite() || !longitude.isFinite() || latitude !in -NORTH_POLE..NORTH_POLE) {
      return null
    }
    return loaded()?.heightMeters(latitude, longitude)
  }

  private suspend fun loaded(): Grid? = mutex.withLock {
    if (!tried) {
      tried = true
      grid = withContext(dispatcher) { load() }
    }
    grid
  }

  private fun load(): Grid? =
    try {
      DataInputStream(BufferedInputStream(open())).use { read(it) }
    } catch (e: IOException) {
      loadFailure = e
      null
    } catch (e: GeoidGridFormatException) {
      loadFailure = e
      null
    }

  private fun read(input: DataInputStream): Grid {

    val header = line(input).split('\t')
    ensure(header.size == HEADER_FIELDS && header[0] == FORMAT_NAME) {
      "Not a geoid grid: '${header.joinToString("\t")}'"
    }
    val version = header[VERSION_FIELD]
    val rowsText = header[ROWS_FIELD]
    val columnsText = header[COLUMNS_FIELD]
    ensure(version == FORMAT_VERSION) { "Unknown geoid grid version $version" }
    val rows = rowsText.toIntOrNull() ?: 0
    val columns = columnsText.toIntOrNull() ?: 0
    ensure(rows >= 2 && columns >= 1) { "Bad geoid grid shape $rowsText by $columnsText" }
    ensure(line(input).startsWith("#")) { "The geoid grid has no source line" }

    val decimeters = ShortArray(rows * columns)
    try {
      for (i in decimeters.indices) decimeters[i] = input.readShort()
    } catch (e: EOFException) {
      throw GeoidGridFormatException("The geoid grid ends before its last height", e)
    }
    ensure(input.read() < 0) { "The geoid grid has more heights than its shape" }
    return Grid(rows, columns, decimeters)
  }

  private inline fun ensure(condition: Boolean, message: () -> String) {
    if (!condition) throw GeoidGridFormatException(message())
  }

  /** One line of the header, without its newline. */
  private fun line(input: DataInputStream): String {
    val bytes = ByteArrayOutputStream()
    while (true) {
      val b = input.read()
      ensure(b >= 0) { "The geoid grid's header is cut short" }
      if (b == '\n'.code) break
      bytes.write(b)
      ensure(bytes.size() <= MAX_HEADER_LINE) { "The geoid grid's header line is too long" }
    }
    return bytes.toString(Charsets.UTF_8.name())
  }

  private class Grid(val rows: Int, val columns: Int, val decimeters: ShortArray) {

    private val rowDegrees = (2 * NORTH_POLE) / (rows - 1)
    private val columnDegrees = FULL_CIRCLE / columns

    fun heightMeters(latitude: Double, longitude: Double): Double {

      val row = (NORTH_POLE - latitude) / rowDegrees
      val row0 = floor(row).toInt().coerceAtMost(rows - 2)
      val rowFraction = row - row0

      val column = ((longitude % FULL_CIRCLE) + FULL_CIRCLE) % FULL_CIRCLE / columnDegrees
      val column0 = floor(column).toInt().coerceAtMost(columns - 1)
      val column1 = (column0 + 1) % columns
      val columnFraction = column - column0

      val north = lerp(at(row0, column0), at(row0, column1), columnFraction)
      val south = lerp(at(row0 + 1, column0), at(row0 + 1, column1), columnFraction)
      return lerp(north, south, rowFraction) / DECIMETERS_PER_METER
    }

    private fun at(row: Int, column: Int): Double = decimeters[row * columns + column].toDouble()

    private fun lerp(from: Double, to: Double, fraction: Double) = from + (to - from) * fraction
  }

  private class GeoidGridFormatException(message: String, cause: Throwable? = null) :
    Exception(message, cause)

  private companion object {
    const val FORMAT_NAME = "zmanim-geoid"
    const val FORMAT_VERSION = "1"
    const val HEADER_FIELDS = 4
    const val VERSION_FIELD = 1
    const val ROWS_FIELD = 2
    const val COLUMNS_FIELD = 3
    const val MAX_HEADER_LINE = 1024
    const val NORTH_POLE = 90.0
    const val FULL_CIRCLE = 360.0
    const val DECIMETERS_PER_METER = 10.0
  }
}
