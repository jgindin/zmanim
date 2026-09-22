/*
 * Copyright (c) 2026. Jay R. Gindin
 */

pluginManagement {
  includeBuild("build-logic")

  repositories {
    gradlePluginPortal()
    google()
    mavenCentral()
  }
}

plugins {
  // Cannot use the `foojay-resolver-convention` alias from gradle/libs.versions.toml here:
  // Gradle does not yet expose version-catalog accessors (`libs.plugins.*`) inside a settings
  // script's own `plugins {}` block - the catalog is only wired up for project build scripts.
  // Confirmed by build failure ("Unresolved reference 'libs'") when attempted here, and this is
  // a known, currently-open Gradle limitation - see gradle/gradle#24876 and gradle/gradle#36437.
  // The version below must be kept in sync with `foojayResolverConvention` in libs.versions.toml.
  id("org.gradle.toolchains.foojay-resolver-convention") version "0.10.0"
}

include(":ZmanUtils")
include(":ZmanLib")
include(":Zmanim")
include(":ZehutYosef")
