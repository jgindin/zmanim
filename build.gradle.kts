/*
 * Copyright (c) 2026. Jay R. Gindin
 */

// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
  repositories {
    mavenCentral()
    maven {
      url = uri("https://maven.google.com")
    }
    google()
  }
  dependencies {
    classpath(libs.android.gradlePlugin)

    // See https://github.com/moallemi/gradle-advanced-build-version
    classpath(libs.moallemi.advanced.build.version.gradlePlugin)

    // Roborazzi: JVM-rendered (Robolectric) screenshot testing, used to establish and later
    //  verify a visual regression safety net against golden PNGs. See docs/modernization/phase1.
    classpath(libs.roborazzi.gradlePlugin)

    // NOTE: Do not place your application dependencies here; they belong
    // in the individual module build.gradle.kts files
  }
}

allprojects {
  repositories {
    mavenCentral()
    maven {
      url = uri("https://raw.githubusercontent.com/jgindin/maven-repo/master/")
    }
    google()
  }
}
