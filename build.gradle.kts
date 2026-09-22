/*
 * Copyright (c) 2026. Jay R. Gindin
 */

// Top-level build file where you can add configuration options common to all sub-projects/modules.

// NOTE: there is deliberately no `buildscript { dependencies { classpath(...) } }` block here any
//  more. The Android Gradle Plugin, org.moallemi.advanced-build-version and Roborazzi used to be
//  put on the classic buildscript classpath from here so that `apply(plugin = "...")` /
//  `apply(from = "...")` in the app modules could find them. They are now declared by the
//  `zmanim.android-application-conventions` convention plugin in build-logic (which depends on
//  them via gradle/libs.versions.toml) and reach the app modules through its own `plugins {}`
//  block. Keeping them here as well would make Gradle refuse to apply them ("plugin is already on
//  the classpath with an unknown version").

allprojects {
  repositories {
    mavenCentral()
    maven {
      url = uri("https://raw.githubusercontent.com/jgindin/maven-repo/master/")
    }
    google()
  }
}
