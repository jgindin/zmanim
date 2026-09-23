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
 * The version *name* deliberately keeps the `<major>.<minor>.<code>` shape - e.g. `4.0.402` -
 * that the APK file names and the about-screen template both depend on.
 *
 * ### What the upstream plugin actually does
 *
 * Both reads here are pure computation, done eagerly at configuration time and with no side
 * effect on disk:
 *  * `advancedVersioning.versionName` just formats the major/minor numbers set just above it.
 *  * `advancedVersioning.versionCode`, for [VersionCodeType.AUTO_INCREMENT_ONE_STEP], *reads*
 *    the module's `version.properties` and returns `AI_VERSION_CODE + 1`. It never writes, so
 *    calling it twice in one build yields the same number - [zmanimAppVersion] is idempotent.
 *
 * The actual increment is written by the plugin itself, from its own `afterEvaluate {}`, and only
 * when one of the names in `gradle.startParameter.taskNames` contains `"release"`
 * (`codeOptions.dependsOnTasks`, default `["release"]`, matched case-insensitively). So the
 * counter advances once per release-task invocation, not once per read.
 *
 * This is a plain function rather than a custom Gradle extension applied from the convention
 * plugin, because the reads must happen *after* the module supplies its major/minor and *before*
 * `android.defaultConfig.versionCode` is assigned. Making that ordering an explicit call in the
 * module's own build script - instead of hiding it behind an `afterEvaluate {}` race with AGP's
 * own DSL finalization - is both simpler and more predictable.
 *
 * ### Hazard: every application module must call this
 *
 * `VersionCodeOptions.getVersionCode()` is also what lazily assigns the plugin's internal
 * `versionPropsFile` field. The plugin's own `afterEvaluate {}` then dereferences it unguarded
 * (`advancedVersioning.codeOptions.versionFile.canRead()`). A module that applies
 * `zmanim.android-application-conventions` but never calls [zmanimAppVersion] therefore leaves
 * that field null and will NPE *inside the plugin* - but only on a `*release*`-named task
 * invocation, since that `afterEvaluate` body is gated on the requested task names. Such a module
 * would sail through every debug and test build and only blow up at release time, so if you add a
 * new application module, call this function from it.
 *
 * This holds a real, typed reference to [AdvancedBuildVersionExtension]: a plugin declared in a
 * convention plugin's `plugins {}` block is loaded through a single classloader, so there's no
 * classloader-identity mismatch to work around and no need for `groovy.lang.GroovyObject`
 * dynamic dispatch.
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
