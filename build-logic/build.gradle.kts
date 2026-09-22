/*
 * Copyright (c) 2026. Jay R. Gindin
 */

plugins {
  `kotlin-dsl`
}

// NOTE: repositories for this build are declared centrally in settings.gradle.kts'
//  dependencyResolutionManagement block, alongside the version catalog wiring.

dependencies {
  // A precompiled script plugin that declares `plugins { id("...") }` needs that plugin's
  //  implementation artifact as a real (not compileOnly) dependency: it is what the generated
  //  Kotlin DSL accessors compile against here, and it is also what carries the plugin onto the
  //  classpath of the projects that apply our convention plugin.
  implementation(libs.android.gradlePlugin)
  implementation(libs.moallemi.advanced.build.version.gradlePlugin)
  implementation(libs.roborazzi.gradlePlugin)
}
