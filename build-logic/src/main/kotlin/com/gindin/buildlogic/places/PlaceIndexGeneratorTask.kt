/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.buildlogic.places

import java.io.File
import java.net.HttpURLConnection
import java.net.URI
import java.security.MessageDigest
import java.util.zip.ZipFile
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction
import org.gradle.work.DisableCachingByDefault

/**
 * Writes the bundled place index from GeoNames' files (see [PlaceIndexData] for the format).
 *
 * GeoNames rebuilds its files every day under the same URLs, so the pins file names each file with
 * the SHA-256 it must have. A downloaded file that does not match fails the task with the checksum
 * it does have: regenerating from newer data is a deliberate change to the pins, reviewed like any
 * other. Files already in [downloads] with the pinned checksum are not downloaded again. The output
 * is committed, so ordinary builds never run this task or touch the network.
 */
@DisableCachingByDefault(because = "It downloads its inputs, and its output is committed")
abstract class PlaceIndexGeneratorTask : DefaultTask() {

  /** Lines of `file<TAB>sha256<TAB>url`; `#` starts a comment line. */
  @get:InputFile @get:PathSensitive(PathSensitivity.RELATIVE) abstract val pins: RegularFileProperty

  @get:InputFile
  @get:PathSensitive(PathSensitivity.RELATIVE)
  abstract val overrides: RegularFileProperty

  @get:Internal abstract val downloads: DirectoryProperty

  @get:OutputFile abstract val output: RegularFileProperty

  /** The PS records and what the overrides make of them, for the maintainer's review. */
  @get:OutputFile abstract val review: RegularFileProperty

  @TaskAction
  fun generate() {

    val files = readPins().associate { pin -> pin.file to fetch(pin) }
    val result =
      try {
        buildIndex(files)
      } catch (e: PlaceDataException) {
        throw GradleException("The place index was not written:\n${e.message}", e)
      }

    output.get().asFile.apply {
      parentFile.mkdirs()
      writeText(result.lines.joinToString(separator = "\n", postfix = "\n"))
    }
    review.get().asFile.apply {
      parentFile.mkdirs()
      writeText(result.reviewOfPsRecords.joinToString(separator = "\n", postfix = "\n"))
    }

    val counts = result.elevationCounts
    logger.lifecycle("Records GeoNames assigns to PS, and what the overrides make of them:")
    result.reviewOfPsRecords.forEach { logger.lifecycle("  $it") }
    logger.lifecycle(
      "Wrote ${result.lines.count { it.startsWith("P\t") }} places " +
        "(${output.get().asFile.length()} bytes). Elevation from the elevation column: " +
        "${counts.fromElevation}, from dem: ${counts.fromDem}, dem 'no data': " +
        "${counts.demNoData}, neither column: ${counts.none}."
    )
  }

  private fun buildIndex(files: Map<String, File>): PlaceIndexOutput {

    fun pinned(name: String) = files[name] ?: throw PlaceDataException("The pins do not list $name")

    val cities =
      ZipFile(pinned(CITIES_ZIP)).use { zip ->
        val entry =
          zip.getEntry(CITIES_ENTRY)
            ?: throw PlaceDataException("$CITIES_ZIP has no entry $CITIES_ENTRY")
        zip.getInputStream(entry).bufferedReader().useLines { PlaceIndexData.parseCities(it) }
      }
    val countries = pinned(COUNTRY_INFO).useLines { PlaceIndexData.parseCountries(it) }
    val overrideRows = overrides.get().asFile.useLines { PlaceIndexData.parseOverrides(it) }
    return PlaceIndexData.build(cities, countries, overrideRows)
  }

  private data class Pin(val file: String, val sha256: String, val url: String)

  private fun readPins(): List<Pin> =
    pins
      .get()
      .asFile
      .readLines()
      .filter { it.isNotBlank() && !it.startsWith("#") }
      .map { line ->
        val fields = line.split('\t')
        if (fields.size != PIN_COLUMNS) {
          throw GradleException("Pins line '$line': expected $PIN_COLUMNS tab-separated columns")
        }
        Pin(file = fields[0], sha256 = fields[1].lowercase(), url = fields[2])
      }

  private fun fetch(pin: Pin): File {
    val target = downloads.get().file(pin.file).asFile
    if (target.isFile && sha256(target) == pin.sha256) {
      return target
    }

    target.parentFile.mkdirs()
    val partial = File(target.parentFile, "${pin.file}.partial")
    logger.lifecycle("Downloading ${pin.url}")
    val connection = URI(pin.url).toURL().openConnection() as HttpURLConnection
    connection.connectTimeout = TIMEOUT_MILLIS
    connection.readTimeout = TIMEOUT_MILLIS
    try {
      if (connection.responseCode != HttpURLConnection.HTTP_OK) {
        throw GradleException("${pin.url} answered HTTP ${connection.responseCode}")
      }
      connection.inputStream.use { input -> partial.outputStream().use { input.copyTo(it) } }
    } finally {
      connection.disconnect()
    }

    val actual = sha256(partial)
    if (actual != pin.sha256) {
      partial.delete()
      throw GradleException(
        "${pin.url} has SHA-256 $actual, but the pins expect ${pin.sha256}. GeoNames has " +
          "published newer data; to regenerate from it, put the new checksum in the pins file."
      )
    }
    partial.renameTo(target)
    return target
  }

  private fun sha256(file: File): String {
    val digest = MessageDigest.getInstance("SHA-256")
    file.inputStream().use { input ->
      val buffer = ByteArray(BUFFER_SIZE)
      while (true) {
        val read = input.read(buffer)
        if (read < 0) break
        digest.update(buffer, 0, read)
      }
    }
    return digest.digest().joinToString("") { "%02x".format(it) }
  }

  private companion object {
    const val CITIES_ZIP = "cities15000.zip"
    const val CITIES_ENTRY = "cities15000.txt"
    const val COUNTRY_INFO = "countryInfo.txt"
    const val PIN_COLUMNS = 3
    const val TIMEOUT_MILLIS = 60_000
    const val BUFFER_SIZE = 64 * 1024
  }
}
