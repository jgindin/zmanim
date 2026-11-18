/*
 * Copyright (c) 2026. Jay R. Gindin
 */

// Applies the Kotlin Multiplatform plugin from build-logic so that it comes from the same copy of
//  the Kotlin Gradle plugin as the other modules' (see build-logic/build.gradle.kts).
//  Gradle loads one copy per place a plugin is applied from, and the Kotlin Gradle plugin does not
//  support being loaded more than once in a build; the Android plugins also have to share a copy
//  of it to see AGP's classes.

plugins {
  id("org.jetbrains.kotlin.multiplatform")
  // Android lint for the Android target (see :ZmanUtils' `checkNewApi`). It is applied here, not
  //  in the module, because build-logic is what puts AGP, and so this plugin, on the classpath.
  id("com.android.lint")
  id("zmanim.static-analysis-conventions")
}
