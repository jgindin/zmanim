/*
 * Copyright (c) 2026. Jay R. Gindin
 */

// Top-level build file where you can add configuration options common to all sub-projects/modules.

// NOTE: there is deliberately no `buildscript { dependencies { classpath(...) } }` block here.
//  AGP, the Kotlin Gradle plugin, org.moallemi.advanced-build-version, and Roborazzi are declared
//  as dependencies of build-logic (see gradle/libs.versions.toml). The app modules get AGP,
//  advanced-build-version and Roborazzi through the `zmanim.android-application-conventions`
//  convention plugin's own `plugins {}` block, get Kotlin through AGP's built-in Kotlin support,
//  and the Kotlin library modules get the Kotlin plugin through their own convention plugins.
//  Adding any of them here too would make Gradle load a second copy, or refuse to apply them
//  ("plugin is already on the classpath with an unknown version").

allprojects {
  repositories {
    mavenCentral()
    maven {
      url = uri("https://raw.githubusercontent.com/jgindin/maven-repo/master/")
    }
    google()
  }
}
