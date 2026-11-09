/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.buildlogic

import java.io.File
import java.net.HttpURLConnection
import java.net.URI
import java.security.MessageDigest
import org.gradle.api.GradleException
import org.gradle.api.logging.Logger

/**
 * Downloads the files a pins file names, each of which must have the SHA-256 the pins give.
 *
 * A pins file has lines of `file<TAB>sha256<TAB>url`; `#` starts a comment line. A downloaded file
 * that does not match fails with the checksum it does have, followed by [changedUpstream]:
 * regenerating from newer data is a deliberate change to the pins, reviewed like any other. Files
 * already in [downloads] with the pinned checksum are not downloaded again.
 */
class PinnedDownloads(
  private val downloads: File,
  private val logger: Logger,
  private val changedUpstream: String,
) {

  /** Every pinned file, by its name in the pins file. */
  fun fetchAll(pins: File): Map<String, File> = readPins(pins).associate { it.file to fetch(it) }

  private data class Pin(val file: String, val sha256: String, val url: String)

  private fun readPins(pins: File): List<Pin> =
    pins
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
    val target = File(downloads, pin.file)
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
        "${pin.url} has SHA-256 $actual, but the pins expect ${pin.sha256}. $changedUpstream"
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
    const val PIN_COLUMNS = 3
    const val TIMEOUT_MILLIS = 60_000
    const val BUFFER_SIZE = 64 * 1024
  }
}
