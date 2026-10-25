/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.buildlogic

import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Project

/**
 * Wires this module's `release` build type to the upload key in the shared
 * `jay-gindin-android-apps` keystore, reading everything secret from Gradle properties instead of
 * from a file in the repository.
 *
 * The properties are expected in `~/.gradle/gradle.properties` (or, on CI, as
 * `ORG_GRADLE_PROJECT_<name>` environment variables):
 * ```
 * gindin.signing.storeFile=/absolute/path/to/jay-gindin-android-apps.keystore
 * gindin.signing.storePassword=...
 * gindin.signing.keyPassword=...
 * ```
 *
 * The key alias isn't secret and differs per app, so it's passed in by the module instead.
 *
 * Since enrolling in Play App Signing, this key is only the *upload* key: Google re-signs what's
 * installed on devices with the app signing key it holds.
 *
 * If any property is missing (e.g. a fresh checkout on another machine), the release build type is
 * simply left unsigned, so debug builds and tests still work.
 */
fun Project.zmanimReleaseSigning(keyAlias: String) {
  val storeFile = providers.gradleProperty("gindin.signing.storeFile").orNull
  val storePassword = providers.gradleProperty("gindin.signing.storePassword").orNull
  val keyPassword = providers.gradleProperty("gindin.signing.keyPassword").orNull

  if (storeFile == null || storePassword == null || keyPassword == null) {
    logger.warn(
      """
      |
      |    ===================================================================
      |    $path: gindin.signing.* Gradle properties not set.
      |    You can't build the release version. Build a debug version instead.
      |    ===================================================================
      """
        .trimMargin()
    )
    return
  }

  val android = extensions.getByType(ApplicationExtension::class.java)
  val release = android.signingConfigs.create("release")
  release.storeFile = file(storeFile)
  release.storePassword = storePassword
  release.keyAlias = keyAlias
  release.keyPassword = keyPassword

  android.buildTypes.getByName("release").signingConfig = release
}
