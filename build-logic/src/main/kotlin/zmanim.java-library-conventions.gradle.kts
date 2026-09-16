/*
 * Copyright (c) 2026. Jay R. Gindin
 */

// Convention plugin capturing the Java toolchain configuration shared by the plain
//  java-library modules (:ZmanUtils, :ZmanLib). Extracted verbatim from what was
//  previously duplicated in each module's build.gradle.

plugins {
  id("java-library")
}

java {
  toolchain {
    languageVersion = JavaLanguageVersion.of(21)
  }
}
