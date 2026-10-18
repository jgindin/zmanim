/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.buildlogic

import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction

/**
 * Copies a single file to a new name.
 *
 * Used instead of Gradle's built-in `Copy` task because `Copy` declares its whole destination
 *  *directory* as an output. Here, the destination directory is one AGP's own bundle task
 *  already writes to, and two tasks with overlapping output directories confuse Gradle's
 *  up-to-date checks. Declaring just the one output file avoids that.
 */
abstract class CopyFileTask : DefaultTask() {

  @get:InputFile
  @get:PathSensitive(PathSensitivity.NONE)
  abstract val source: RegularFileProperty

  @get:OutputFile
  abstract val destination: RegularFileProperty

  @TaskAction
  fun copy() {
    source.get().asFile.copyTo(destination.get().asFile, overwrite = true)
  }
}
