/*
 * Copyright (c) 2026. Jay R. Gindin
 */

// Top-level build file where you can add configuration options common to all sub-projects/modules.

// NOTE: there is deliberately no `buildscript { dependencies { classpath(...) } }` block here.
//  AGP, org.moallemi.advanced-build-version, and Roborazzi are declared as dependencies of the
//  `zmanim.android-application-conventions` convention plugin in build-logic (see
//  gradle/libs.versions.toml) and reach the app modules through that plugin's own `plugins {}`
//  block. Adding them here too would make Gradle refuse to apply them ("plugin is already on the
//  classpath with an unknown version").

// The Kotlin plugins are applied by more than one subproject (:ZmanUtils, :ZmanLib). Declaring them
//  once here, unapplied, puts a single copy on the shared classpath; applying them per subproject
//  with the `plugins {}` DSL instead makes Gradle load the Kotlin Gradle plugin once per subproject.
plugins {
  alias(libs.plugins.kotlin.multiplatform) apply false
  alias(libs.plugins.kotlin.jvm) apply false
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
