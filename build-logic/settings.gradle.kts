/*
 * Copyright (c) 2026. Jay R. Gindin
 */

dependencyResolutionManagement {
  repositories {
    gradlePluginPortal()
    google()
    mavenCentral()
  }

  // An included build is a *separate* Gradle build with its own settings; it does not inherit the
  //  main build's version catalog. Declaring the catalog here gives build-logic/build.gradle.kts
  //  the same `libs.*` accessors every other module in this repo already uses, so the AGP /
  //  advanced-build-version / Roborazzi versions that the convention plugins compile and run
  //  against stay centralized in gradle/libs.versions.toml rather than being hardcoded here.
  versionCatalogs { create("libs") { from(files("../gradle/libs.versions.toml")) } }
}

rootProject.name = "build-logic"
