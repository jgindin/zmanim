/*
 * Copyright (c) 2026. Jay R. Gindin
 */

import com.gindin.buildlogic.zmanimAppVersion
import com.gindin.buildlogic.zmanimReleaseCheckBuildType
import com.gindin.buildlogic.zmanimReleaseSigning

plugins {
  // See Zmanim/build.gradle.kts for what this convention plugin (build-logic) brings in.
  id("zmanim.android-application-conventions")
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
      isMinifyEnabled = true
      isShrinkResources = true
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }

    getByName("debug") {}
  }

  flavorDimensions.add("sidur")

  productFlavors {
    create("barMitzvah") { applicationId = "com.gindin.barmitzvah" }

    create("sidur") { applicationId = "com.gindin.zehutyosef" }
  }
}

// Release signing (upload key) comes from gindin.signing.* Gradle properties; see Signing.kt.
//  Both flavors share this key.
zmanimReleaseSigning(keyAlias = "zehutyosef")

// The release build signed with the debug key, for running the on-device tests against R8-minified code; see
//  ReleaseCheck.kt. It copies the release build type as configured above, so it has to come after it.
zmanimReleaseCheckBuildType()

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
  implementation("${libs.mikepenz.aboutlibraries.get()}@aar") { isTransitive = true }
}
