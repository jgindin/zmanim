/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.buildlogic

import org.gradle.api.Project
import org.moallemi.gradle.internal.AdvancedBuildVersionExtension
import org.moallemi.gradle.internal.VersionCodeType

/** The versionCode/versionName pair computed for an app module. */
data class AppVersion(val code: Int, val name: String)

/**
 * Drives the `org.moallemi.advanced-build-version` plugin (applied by
 * `zmanim.android-application-conventions`) with this module's major/minor numbers and returns the
 * resulting version code/name, ready to be assigned to `android.defaultConfig`.
 *
 * The version *name* deliberately keeps the historical `<major>.<minor>.<code>` shape - e.g.
 * `4.0.402` - which the APK file names and the about-screen template both depend on.
 *
 * This is a plain function rather than a custom Gradle extension applied from the convention
 * plugin on purpose: `advancedVersioning.versionCode` is read eagerly at configuration time (it
 * increments a counter stored in each module's `version.properties`), so it must be evaluated
 * *after* the module supplies its major/minor and *before* `android.defaultConfig.versionCode` is
 * assigned. Making that ordering an explicit call in the module's own build script - instead of
 * hiding it behind an `afterEvaluate {}` race with AGP's own DSL finalization - is both simpler
 * and more predictable, and it is exactly the order the previous
 * `common/gindin-versioning.gradle.kts` ran in.
 *
 * Note that unlike that script, this holds a real, typed reference to
 * [AdvancedBuildVersionExtension]: a plugin declared in a convention plugin's `plugins {}` block
 * is loaded through a single classloader, so there is no classloader-identity mismatch to work
 * around and no need for `groovy.lang.GroovyObject` dynamic dispatch.
 */
fun Project.zmanimAppVersion(
  major: Int,
  minor: Int,
  versionCodeType: VersionCodeType = VersionCodeType.AUTO_INCREMENT_ONE_STEP,
): AppVersion {
  val advancedVersioning = extensions.getByType(AdvancedBuildVersionExtension::class.java)

  advancedVersioning.nameOptions.versionMajor(major)
  advancedVersioning.nameOptions.versionMinor(minor)
  advancedVersioning.codeOptions.versionCodeType(versionCodeType)

  val code = advancedVersioning.versionCode
  return AppVersion(code, "${advancedVersioning.versionName}.$code")
}
