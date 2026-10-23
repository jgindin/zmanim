/*
 * Copyright (c) 2026. Jay R. Gindin
 */

plugins {
  `kotlin-dsl`
  alias(libs.plugins.ktfmt)
}

// Same layout as the modules get from zmanim.static-analysis-conventions, which cannot be applied
//  to the build that defines it.
ktfmt {
  googleStyle()
  maxWidth.set(120)
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
  implementation(libs.ktfmt.gradlePlugin)
  implementation(libs.detekt.gradlePlugin)

  // AGP's built-in Kotlin support (what lets the app modules compile Kotlin) uses the Kotlin Gradle
  //  plugin, and it can only reach AGP's classes if both come from this one classpath. The Kotlin
  //  library modules apply the plugin from here too (see the zmanim.*-conventions plugins), so that
  //  the build only ever has one copy of it.
  implementation(libs.kotlin.gradlePlugin)
}
