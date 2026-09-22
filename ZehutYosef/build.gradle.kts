/*
 * Copyright (c) 2026. Jay R. Gindin
 */

import com.gindin.buildlogic.zmanimAppVersion

plugins {
  // See Zmanim/build.gradle.kts for what this convention plugin (build-logic) brings in.
  id("zmanim.android-application-conventions")
}

// Optional-apply pattern replacing the Groovy try/catch: zehut-yosef.gradle is a never-committed,
//  gitignored file (see .hgignore, "^ZehutYosef/zehut-yosef.gradle") with the real release
//  signing config, so it may simply not exist on a fresh checkout. Kotlin DSL's
//  `apply(from = ...)` fails the whole script evaluation before the applied file's own contents
//  are even parsed when the file is missing (there's no analogous runtime exception for a bare
//  try/catch to swallow the way Groovy's dynamic `apply from:` allowed), so an explicit
//  existence check is used for that case. The try/catch around the apply() call itself is kept
//  (matching the original) to also tolerate the file existing but failing while it evaluates --
//  which, empirically, is exactly what happens with the copy of this file on disk today. Got
//  this idea from Mike Penz's AboutLibraries.
if (file("zehut-yosef.gradle").exists()) {
  try {
    apply(from = "zehut-yosef.gradle")
  }
  catch (ignored: Exception) {
  }
}


// Feeds this module's major/minor into the advanced-build-version plugin (applied by the
//  convention plugin) and reads back the computed code/name. The version code auto-increments
//  through ZehutYosef/version.properties exactly as before.
val appVersion = zmanimAppVersion(major = 1, minor = 0)

android {
  namespace = "com.gindin.zehutyosef"

  defaultConfig {
    minSdk = 24
    versionCode = appVersion.code
    versionName = appVersion.name
  }

  buildTypes {

    getByName("release") {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")

      try {
        // In case this dev doesn't have the signing keys, this will fail...
        signingConfig = signingConfigs.getByName("release")
      }
      catch (ignored: Exception) {
      }
    }

    getByName("debug") {
    }
  }

  flavorDimensions.add("sidur")

  productFlavors {
    create("barMitzvah") {
      applicationId = "com.gindin.barmitzvah"
    }

    create("sidur") {
      applicationId = "com.gindin.zehutyosef"
    }
  }
}

dependencies {

  extra["supportLibsVersion"] = "25.4.0"
  implementation(libs.androidx.appcompat)

  /* Used to provide out of the box icon font support. simplifies development, and provides scalable icons.
   *  the core is very very light. https://github.com/mikepenz/Android-Iconics
   */
  implementation("${libs.mikepenz.iconics.core.get()}@aar")
  implementation("${libs.mikepenz.google.material.typeface.get()}@aar")
  implementation("${libs.mikepenz.fontawesome.typeface.get()}@aar")

  /* https://github.com/mikepenz/MaterialDrawer */
  implementation("${libs.mikepenz.materialdrawer.get()}@aar")

  /* https://github.com/mikepenz/AboutLibraries */
  implementation("${libs.mikepenz.aboutlibraries.get()}@aar") {
    isTransitive = true
  }
}
