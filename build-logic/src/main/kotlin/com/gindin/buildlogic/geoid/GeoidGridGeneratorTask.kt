/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.buildlogic.geoid

import com.gindin.buildlogic.PinnedDownloads
import java.io.File
import java.util.zip.ZipFile
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction
import org.gradle.work.DisableCachingByDefault

/**
 * Writes the bundled geoid grid from GeographicLib's EGM2008 grid (see [GeoidGridData] for the
 * format). The download is pinned by checksum (see [PinnedDownloads]). The output is committed, so
 * ordinary builds never run this task or touch the network.
 */
@DisableCachingByDefault(because = "It downloads its input, and its output is committed")
abstract class GeoidGridGeneratorTask : DefaultTask() {

  /** The file to download and its checksum, in [PinnedDownloads]' format. */
  @get:InputFile @get:PathSensitive(PathSensitivity.RELATIVE) abstract val pins: RegularFileProperty

  /** Degrees between the kept nodes, in both latitude and longitude. */
  @get:Input abstract val stepDegrees: Property<Int>

  @get:Internal abstract val downloads: DirectoryProperty

  @get:OutputFile abstract val output: RegularFileProperty

  @TaskAction
  fun generate() {

    val files =
      PinnedDownloads(
          downloads = downloads.get().asFile,
          logger = logger,
          changedUpstream =
            "The published grid has changed; check why before putting the new checksum in the " +
              "pins file.",
        )
        .fetchAll(pins.get().asFile)
    val pgm = readGrid(files[GRID_ZIP] ?: throw GradleException("The pins do not list $GRID_ZIP"))
    val asset =
      try {
        GeoidGridData.build(pgm, stepDegrees.get())
      } catch (e: GeoidDataException) {
        throw GradleException("The geoid grid was not written:\n${e.message}", e)
      }

    output.get().asFile.apply {
      parentFile.mkdirs()
      writeBytes(asset)
    }
    logger.lifecycle("Wrote a ${stepDegrees.get()}-degree geoid grid (${asset.size} bytes).")
  }

  private fun readGrid(zip: File): ByteArray =
    ZipFile(zip).use { archive ->
      val entry =
        archive.getEntry(GRID_ENTRY) ?: throw GradleException("$GRID_ZIP has no entry $GRID_ENTRY")
      archive.getInputStream(entry).use { it.readBytes() }
    }

  private companion object {
    const val GRID_ZIP = "egm2008-5.zip"
    const val GRID_ENTRY = "geoids/egm2008-5.pgm"
  }
}
