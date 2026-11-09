/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.buildlogic.places

import com.gindin.buildlogic.PinnedDownloads
import java.io.File
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
 * the SHA-256 it must have (see [PinnedDownloads]). The output is committed, so ordinary builds
 * never run this task or touch the network.
 */
@DisableCachingByDefault(because = "It downloads its inputs, and its output is committed")
abstract class PlaceIndexGeneratorTask : DefaultTask() {

  /** The files to download and their checksums, in [PinnedDownloads]' format. */
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

    val files =
      PinnedDownloads(
          downloads = downloads.get().asFile,
          logger = logger,
          changedUpstream =
            "GeoNames has published newer data; to regenerate from it, put the new checksum in " +
              "the pins file.",
        )
        .fetchAll(pins.get().asFile)
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

  private companion object {
    const val CITIES_ZIP = "cities15000.zip"
    const val CITIES_ENTRY = "cities15000.txt"
    const val COUNTRY_INFO = "countryInfo.txt"
  }
}
