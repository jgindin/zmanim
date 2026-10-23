/*
 * Copyright (c) 2026. Jay R. Gindin
 */

import com.gindin.buildlogic.zmanimAppVersion
import com.gindin.buildlogic.zmanimReleaseCheckBuildType
import com.gindin.buildlogic.zmanimReleaseSigning

plugins {
  // build-logic's precompiled convention plugin. It applies com.android.application,
  //  org.moallemi.advanced-build-version and Roborazzi, and carries everything this module and
  //  :ZehutYosef configure identically: compileSdk/targetSdk, Java 21 compileOptions + test
  //  toolchain, the Roborazzi/Robolectric unit-test wiring, the APK/AAB output-renaming Variant API
  //  block, the shared :ZmanLib / libs/*.jar dependencies and the shared test dependencies.
  id("zmanim.android-application-conventions")
}


// Feeds this module's major/minor into the advanced-build-version plugin (applied by the
//  convention plugin) and reads back the computed code/name. The version code auto-increments
//  through Zmanim/version.properties exactly as before.
val appVersion = zmanimAppVersion(major = 4, minor = 0)


android {
  namespace = "com.gindin.zmanim.android"

  defaultConfig {
    applicationId = "com.gindin.zmanim.android"

    /*
     * NOTES:
     *	  1. In order for the supports-screens to work right, we have to specify the target SDK of at least 11.
     *		2. If minSdkVersion is >= 13, then the behavior of AsyncTask is CHANGED!
     *					See http://commonsware.com/blog/2012/04/20/asynctask-threading-regression-confirmed.html
     *					For minSdkVersion >= 11, there's some other interesting stuff in that article...
     */
    minSdk = 24

    versionCode = appVersion.code
    versionName = appVersion.name
  }

  // The on-device tests start from the same synthetic saved state of the released app that the
  //  Robolectric tests use, so the committed fixture directory doubles as an androidTest asset root
  //  (readable by the test APK as assets/zmanim-v409/...) instead of being copied.
  sourceSets {
    getByName("androidTest") {
      assets.srcDir("src/test/resources/upgrade")
    }
  }

  // `copy` is a `Project` member, not something `defaultConfig {}` exposes, so this has to be a
  //  direct sibling of `defaultConfig` rather than nested inside it. Runs at project-
  //  configuration time, filling the version placeholder into the about-screen template.
  run {
    val replace = appVersion.name
    project.copy {
      from("src/template/assets/com/gindin/zmanim/android/about.html") {
        filter { line: String -> line.replace("@@VERSION@@", replace) }
      }
      into("src/main/assets/com/gindin/zmanim/android")
    }
  }

  buildTypes {
    getByName("release") {
      isMinifyEnabled = true
      isShrinkResources = true
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }

    getByName("debug") {
    }
  }
}

// Release signing (upload key) comes from gindin.signing.* Gradle properties; see Signing.kt.
zmanimReleaseSigning(keyAlias = "android-app")

// The release build signed with the debug key, for running the on-device tests against R8-minified code; see
//  ReleaseCheck.kt. It copies the release build type as configured above, so it has to come after it.
zmanimReleaseCheckBuildType()

dependencies {
  // The net.xpece.android support libraries only exist in jcenter for the older version that
  //  is in use here (to support older Android OS versions). Since jcenter has gone away, I've
  //  just copied the necessary files into our libs directory.
  //  So, this now replaces "implementation 'net.xpece.android:support-preference:1.3.1'"
  // Newer versions are hosted on Maven Central.
  // See https://github.com/consp1racy/android-support-preference
  api(fileTree(mapOf("include" to listOf("*.aar"), "dir" to "libs")))

  implementation(project(":ZmanUtils"))

  implementation(libs.kosherjava.zmanim)

  implementation(libs.bundles.zmanim.ui)

  implementation(libs.kotlinx.serialization.json)

  // Drives the system notification permission prompt, which belongs to another app, from the on-device tests.
  androidTestImplementation(libs.androidx.test.uiautomator)
}
