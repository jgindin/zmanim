/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.buildlogic.geoid

import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import kotlin.math.roundToInt

/** A problem with the source grid; the generator writes nothing. */
class GeoidDataException(message: String) : Exception(message)

/**
 * Turns GeographicLib's geoid grid into the bundled geoid asset.
 *
 * The source is a 16-bit PGM image: rows of latitude from 90° N southwards to 90° S, columns of
 * longitude from 0° eastwards, each pixel `Offset + Scale × value` meters, with `Offset` and
 * `Scale` given in `#` comment lines of the header.
 *
 * The asset is two lines of UTF-8 text and then the heights:
 * - `zmanim-geoid<TAB>1<TAB>rows<TAB>columns`: the format name, version and grid shape.
 * - `# ...`: the source of the data.
 * - rows × columns signed 16-bit big-endian integers, row by row, in the source's order: the geoid
 *   height at each node in decimeters (the height of mean sea level above the WGS 84 ellipsoid).
 *   Rows are `180 / (rows - 1)` degrees apart and columns `360 / columns` degrees apart.
 *
 * Only the nodes on every whole step are kept: the nodes themselves are exact, so the app's
 * bilinear interpolation between them is as accurate as the step allows.
 */
object GeoidGridData {

  const val FORMAT_NAME: String = "zmanim-geoid"

  const val FORMAT_VERSION: Int = 1

  const val SOURCE: String =
    "Geoid heights from EGM2008 (US National Geospatial-Intelligence Agency, public domain), " +
      "as gridded by GeographicLib (egm2008-5), sampled for this app."

  private const val PGM_MAGIC = "P5"
  private const val SIXTEEN_BIT_MAX = 65535
  private const val HEADER_TOKENS = 4
  private const val DECIMETERS_PER_METER = 10.0
  private const val DEGREES_OF_LATITUDE = 180
  private const val DEGREES_OF_LONGITUDE = 360
  private const val BYTE_MASK = 0xff
  private const val NEWLINE = '\n'.code.toByte()

  /** The asset for [pgm] with a node every [stepDegrees] degrees. */
  fun build(pgm: ByteArray, stepDegrees: Int): ByteArray {

    val source = parse(pgm)
    val columnStride = strideFor(source.width, DEGREES_OF_LONGITUDE, stepDegrees, "columns")
    val rowStride = strideFor(source.height - 1, DEGREES_OF_LATITUDE, stepDegrees, "rows")
    val rows = (source.height - 1) / rowStride + 1
    val columns = source.width / columnStride

    val out = ByteArrayOutputStream()
    out.write("$FORMAT_NAME\t$FORMAT_VERSION\t$rows\t$columns\n".toByteArray(Charsets.UTF_8))
    out.write("# $SOURCE\n".toByteArray(Charsets.UTF_8))
    DataOutputStream(out).use { data ->
      for (row in 0 until source.height step rowStride) {
        for (column in 0 until source.width step columnStride) {
          data.writeShort(decimeters(source, row, column))
        }
      }
    }
    return out.toByteArray()
  }

  private class Pgm(
    val width: Int,
    val height: Int,
    val offset: Double,
    val scale: Double,
    val pixels: ByteArray,
    val start: Int,
  ) {
    fun pixel(row: Int, column: Int): Int {
      val at = start + 2 * (row * width + column)
      val high = pixels[at].toInt() and BYTE_MASK
      val low = pixels[at + 1].toInt() and BYTE_MASK
      return (high shl Byte.SIZE_BITS) or low
    }
  }

  private fun parse(pgm: ByteArray): Pgm {

    val comments = mutableMapOf<String, String>()
    val tokens = mutableListOf<String>()
    var position = 0
    while (tokens.size < HEADER_TOKENS) {
      val end = (position until pgm.size).firstOrNull { pgm[it] == NEWLINE }
      ensure(end != null) { "The grid's header ends before its size is given" }
      val line = String(pgm, position, end!! - position, Charsets.US_ASCII)
      position = end + 1
      if (line.startsWith("#")) {
        val words = line.removePrefix("#").trim().split(Regex("\\s+"), limit = 2)
        if (words.size == 2) comments[words[0]] = words[1]
      } else {
        tokens += line.trim().split(Regex("\\s+")).filter { it.isNotEmpty() }
      }
    }

    val magic = tokens[0]
    val maxValue = tokens[HEADER_TOKENS - 1]
    ensure(magic == PGM_MAGIC && maxValue.toIntOrNull() == SIXTEEN_BIT_MAX) {
      "The grid is not a 16-bit binary PGM ($magic, max $maxValue)"
    }
    val width = number(tokens[1], "width").toInt()
    val height = number(tokens[2], "height").toInt()
    ensure(pgm.size - position == 2 * width * height) {
      "The grid has ${pgm.size - position} bytes of heights; $width by $height needs " +
        "${2 * width * height}"
    }

    return Pgm(
      width,
      height,
      offset = number(comments["Offset"], "Offset"),
      scale = number(comments["Scale"], "Scale"),
      pixels = pgm,
      start = position,
    )
  }

  private fun number(text: String?, what: String): Double {
    val value = text?.toDoubleOrNull()
    ensure(value != null) { "The grid's header has no numeric $what" }
    return value!!
  }

  private inline fun ensure(condition: Boolean, message: () -> String) {
    if (!condition) throw GeoidDataException(message())
  }

  /** How many source nodes apart the kept nodes are, when [spans] nodes cover [degrees]. */
  private fun strideFor(spans: Int, degrees: Int, stepDegrees: Int, what: String): Int {
    val nodesPerStep = spans * stepDegrees
    ensure(nodesPerStep % degrees == 0 && degrees % stepDegrees == 0) {
      "A $stepDegrees-degree step does not fall on the grid's $what"
    }
    return nodesPerStep / degrees
  }

  private fun decimeters(source: Pgm, row: Int, column: Int): Int {
    val meters = source.offset + source.scale * source.pixel(row, column)
    val decimeters = (meters * DECIMETERS_PER_METER).roundToInt()
    ensure(decimeters in Short.MIN_VALUE..Short.MAX_VALUE) {
      "A height of $meters m does not fit in the asset"
    }
    return decimeters
  }
}
