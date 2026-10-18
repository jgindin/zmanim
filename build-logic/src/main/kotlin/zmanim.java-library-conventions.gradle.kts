/*
 * Copyright (c) 2026. Jay R. Gindin
 */

// Convention plugin capturing the Java toolchain configuration for the plain java-library
//  modules (currently :ZmanLib). The Kotlin JVM plugin and its own toolchain are configured in the
//  module's build script, alongside this plugin.

plugins {
  id("java-library")
}

java {
  toolchain {
    languageVersion = JavaLanguageVersion.of(21)
  }
}
