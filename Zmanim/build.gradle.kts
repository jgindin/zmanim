/*
 * Copyright (c) 2026. Jay R. Gindin
 */

import com.gindin.buildlogic.zmanimAppVersion

plugins {
  // build-logic's precompiled convention plugin. It applies com.android.application,
  //  org.moallemi.advanced-build-version and Roborazzi, and carries everything this module and
  //  :ZehutYosef configure identically: compileSdk/targetSdk, Java 21 compileOptions + test
  //  toolchain, the Roborazzi/Robolectric unit-test wiring, the APK output-renaming Variant API
  //  block, the shared :ZmanLib / libs/*.jar dependencies and the shared test dependencies.
  id("zmanim.android-application-conventions")
}

// Optional-apply pattern replacing the Groovy try/catch: zmanim.gradle is a never-committed,
//  gitignored file (see .hgignore, "^Zmanim/zmanim.gradle") with the real release signing
//  config, so it may simply not exist on a fresh checkout. Kotlin DSL's `apply(from = ...)`
//  fails the whole script evaluation before the applied file's own contents are even parsed
//  when the file is missing (there's no analogous runtime exception for a bare try/catch to
//  swallow the way Groovy's dynamic `apply from:` allowed), so an explicit existence check is
//  used for that case. The try/catch around the apply() call itself is kept (matching the
//  original) to also tolerate the file existing but failing while it evaluates. Got this idea
//  from Mike Penz's AboutLibraries.
if (file("zmanim.gradle").exists()) {
  try {
    apply(from = "zmanim.gradle")
  }
  catch (ignored: Exception) {
    println()
    println()
    println("    ===================================================================")
    println("    You can't build the release version. Build a debug version instead.")
    println("    ===================================================================")
  }
} else {
  println()
  println()
  println("    ===================================================================")
  println("    You can't build the release version. Build a debug version instead.")
  println("    ===================================================================")
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

      isMinifyEnabled = false

      /*
       * Library-specific proguard files.
       * See https://github.com/krschultz/android-proguard-snippets.
       */
      proguardFile("proguard-google-play-services.pro")
      proguardFile("proguard-support-design.pro")
      proguardFile("proguard-support-v7-appcompat.pro")
      proguardFile("proguard-project.pro")

      /* Reminder: Use the default proguard from the SDK, and then add in our custom proguard rules. */
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), file("proguard-rules.pro"))

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
}

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

  testImplementation(libs.org.json)
}
