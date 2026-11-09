/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.buildlogic.geoid

import java.io.ByteArrayOutputStream
import java.io.DataInputStream
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class UT_GeoidGridData {

  /**
   * A GeographicLib-style PGM [width] by [height], with offset -100 and scale 0.01, whose pixel at
   * (row, column) is [pixel] of them.
   */
  private fun pgm(
    width: Int,
    height: Int,
    offset: String? = "-100",
    scale: String? = "0.01",
    maxValue: Int = 65535,
    extraBytes: Int = 0,
    pixel: (Int, Int) -> Int,
  ): ByteArray {
    val out = ByteArrayOutputStream()
    val header = buildString {
      append("P5\n")
      append("# Description A synthetic grid\n")
      offset?.let { append("# Offset $it\n") }
      scale?.let { append("# Scale $it\n") }
      append("$width    $height\n")
      append("$maxValue\n")
    }
    out.write(header.toByteArray(Charsets.US_ASCII))
    for (row in 0 until height) {
      for (column in 0 until width) {
        val value = pixel(row, column)
        out.write(value shr Byte.SIZE_BITS)
        out.write(value and BYTE_MASK)
      }
    }
    repeat(extraBytes) { out.write(0) }
    return out.toByteArray()
  }

  /** The asset's two text lines and its values. */
  private data class Asset(val header: String, val comment: String, val decimeters: List<Short>)

  private fun read(asset: ByteArray): Asset {
    val input = DataInputStream(asset.inputStream())
    fun line() = buildString {
      while (true) {
        val b = input.read()
        if (b == '\n'.code) break
        append(b.toChar())
      }
    }
    val header = line()
    val comment = line()
    val values = buildList {
      while (input.available() > 0) {
        add(input.readShort())
      }
    }
    return Asset(header, comment, values)
  }

  @Test
  fun theAssetStartsWithItsFormatVersionAndShapeThenTheSource() {
    // 8 columns of 45 degrees and 5 rows of 45 degrees, sampled every 90 degrees.
    val asset = read(GeoidGridData.build(pgm(8, 5) { _, _ -> 10_000 }, stepDegrees = 90))

    assertEquals("zmanim-geoid\t1\t3\t4", asset.header)
    assertEquals("# ${GeoidGridData.SOURCE}", asset.comment)
  }

  @Test
  fun everyStepthNodeIsKeptFromTheNorthPoleAndTheMeridianInDecimeters() {
    // Pixel value row * 1000 + column * 10 is (row * 10 + column / 10 - 100) meters.
    val asset = read(GeoidGridData.build(pgm(8, 5) { r, c -> r * 1000 + c * 10 }, stepDegrees = 90))

    val expected =
      listOf(0, 2, 4, 6).map { -1000 + it } +
        listOf(0, 2, 4, 6).map { -1000 + 200 + it } +
        listOf(0, 2, 4, 6).map { -1000 + 400 + it }
    assertEquals(expected.map { it.toShort() }, asset.decimeters)
  }

  @Test
  fun heightsAreRoundedToTheNearestDecimeter() {
    // 10,004 * 0.01 - 100 = 0.04 m and 10,006 * 0.01 - 100 = 0.06 m.
    val low = read(GeoidGridData.build(pgm(4, 3) { _, _ -> 10_004 }, stepDegrees = 90))
    val high = read(GeoidGridData.build(pgm(4, 3) { _, _ -> 10_006 }, stepDegrees = 90))

    assertEquals(0, low.decimeters.first().toInt())
    assertEquals(1, high.decimeters.first().toInt())
  }

  @Test
  fun negativeHeightsAreKept() {
    // 0 * 0.01 - 100 = -100 m.
    val asset = read(GeoidGridData.build(pgm(4, 3) { _, _ -> 0 }, stepDegrees = 90))

    assertEquals(-1000, asset.decimeters.first().toInt())
  }

  @Test
  fun aGridWithoutOffsetOrScaleIsRefused() {
    val noOffset =
      assertFailsWith<GeoidDataException> {
        GeoidGridData.build(pgm(4, 3, offset = null) { _, _ -> 0 }, stepDegrees = 90)
      }
    val noScale =
      assertFailsWith<GeoidDataException> {
        GeoidGridData.build(pgm(4, 3, scale = null) { _, _ -> 0 }, stepDegrees = 90)
      }

    assertContains(noOffset.message.orEmpty(), "Offset")
    assertContains(noScale.message.orEmpty(), "Scale")
  }

  @Test
  fun aGridThatIsNotSixteenBitsOrIsTheWrongLengthIsRefused() {
    assertFailsWith<GeoidDataException> {
      GeoidGridData.build(pgm(4, 3, maxValue = 255) { _, _ -> 0 }, stepDegrees = 90)
    }
    assertFailsWith<GeoidDataException> {
      GeoidGridData.build(pgm(4, 3, extraBytes = 1) { _, _ -> 0 }, stepDegrees = 90)
    }
    assertFailsWith<GeoidDataException> {
      GeoidGridData.build(pgm(4, 3) { _, _ -> 0 }.dropLast(1).toByteArray(), stepDegrees = 90)
    }
  }

  @Test
  fun aStepThatDoesNotFallOnTheGridIsRefused() {
    // 4 columns of 90 degrees cannot be sampled every 45 or every 60 degrees.
    assertFailsWith<GeoidDataException> {
      GeoidGridData.build(pgm(4, 3) { _, _ -> 0 }, stepDegrees = 45)
    }
    assertFailsWith<GeoidDataException> {
      GeoidGridData.build(pgm(8, 5) { _, _ -> 0 }, stepDegrees = 60)
    }
  }

  @Test
  fun aHeightOutsideWhatTheAssetCanHoldIsRefused() {
    // 65,535 * 1 - 100 = 65,435 m, which does not fit in 16 bits as decimeters.
    assertFailsWith<GeoidDataException> {
      GeoidGridData.build(pgm(4, 3, scale = "1") { _, _ -> 65_535 }, stepDegrees = 90)
    }
  }

  private companion object {
    const val BYTE_MASK = 0xff
  }
}
