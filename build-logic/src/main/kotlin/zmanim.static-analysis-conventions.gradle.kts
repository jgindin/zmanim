/*
 * Copyright (c) 2026. Jay R. Gindin
 */

import org.gradle.api.artifacts.VersionCatalogsExtension
import org.jlleitschuh.gradle.ktlint.KtlintExtension

// Convention plugin applying the Kotlin static-analysis tools to a module. The Java, Kotlin
//  multiplatform and Android application convention plugins all apply it, so every module with
//  Kotlin sources is checked the same way and the tool versions live in one place.

plugins {
  id("org.jlleitschuh.gradle.ktlint")
}

// Version-catalog accessors are not generated inside precompiled script plugins - see
//  gradle/gradle#15383 - so the catalog is looked up by name, as in the other convention plugins.
val libs = extensions.getByType(VersionCatalogsExtension::class.java).named("libs")

configure<KtlintExtension> {
  // The Gradle plugin's own version and the ktlint engine it runs are independent; pinning the
  //  engine keeps a plugin upgrade from silently changing which rules apply.
  version.set(libs.findVersion("ktlint").get().requiredVersion)

  // Findings that exist in the code as it was converted are recorded in a baseline next to the
  //  module, so the check starts clean and fails only on findings introduced afterwards.
  baseline.set(layout.projectDirectory.file("config/ktlint/baseline.xml"))
}
