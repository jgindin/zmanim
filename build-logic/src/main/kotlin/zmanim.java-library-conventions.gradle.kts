/*
 * Copyright (c) 2026. Jay R. Gindin
 */

// Convention plugin capturing the Java toolchain configuration for the plain java-library
//  modules (currently :ZmanLib). The Kotlin JVM plugin is applied here, rather than from the
// module,
//  so that it comes from the same copy of the Kotlin Gradle plugin as the app modules' (see
//  build-logic/build.gradle.kts); the module configures Kotlin's own toolchain.

plugins {
  id("java-library")
  id("org.jetbrains.kotlin.jvm")
  id("zmanim.static-analysis-conventions")
}

java { toolchain { languageVersion = JavaLanguageVersion.of(21) } }
