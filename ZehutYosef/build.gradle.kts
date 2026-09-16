/*
 * Copyright (c) 2026. Jay R. Gindin
 */

import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.moallemi.gradle.internal.VersionCodeType

// See the equivalent block at the top of Android/build.gradle.kts for why this file is
//  structured this way (apply(from=...)/apply(plugin=...) instead of a `plugins {}` block, and
//  `configure<BaseAppModuleExtension> {}` instead of the bare `android {}` accessor): AGP needs
//  to stay on the classic buildscript classpath -- not applied via `plugins {}` -- for the
//  classes referenced by common/gindin-versioning.gradle.kts (applied below) to stay resolvable
//  there, and Kotlin DSL only generates the typed `android {}`/`implementation(...)` accessors
//  for a plugin declared via this script's own `plugins {}` block.
apply(from = "../common/gindin-android.gradle.kts")

// Roborazzi: records/verifies golden PNG screenshots rendered by Robolectric. See
//  docs/modernization/phase1 and ZehutYosef/src/test/java/com/gindin/zehutyosef/snapshot.
apply(plugin = "io.github.takahirom.roborazzi")

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


project.extra["majorVersion"] = 1
project.extra["minorVersion"] = 0
project.extra["versionCodeType"] = VersionCodeType.AUTO_INCREMENT_ONE_STEP

apply(from = "../common/gindin-versioning.gradle.kts")

/**
 * Swizzle the APK name. See the equivalent, more-commented block in Android/build.gradle.kts for
 *  why this lives here rather than in the shared common/gindin-versioning.gradle.kts script.
 */
extensions.getByType(ApplicationAndroidComponentsExtension::class.java).onVariants { variant ->
  variant.outputs.forEach { output ->
    output.outputFileName.set("${project.name}-${variant.flavorName}-${variant.name}-${output.versionName.get()}.apk")
  }
}

configure<BaseAppModuleExtension> {
  namespace = "com.gindin.zehutyosef"

  compileSdk = 33

  defaultConfig {
    minSdk = 22
    targetSdk = 35
    versionCode = project.extra["appVersionCode"] as Int
    versionName = project.extra["appVersionName"] as String
  }

  buildFeatures.buildConfig = true

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


  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
  }

  testOptions {
    unitTests.apply {
      // Roborazzi/Robolectric need real resources (layouts, drawables, styles) loaded to render
      //  screenshots that look like the actual app, not just default/stub values.
      isIncludeAndroidResources = true

      // gradle.properties' robolectric.graphicsMode=NATIVE only configures the Gradle build
      //  itself; it is NOT forwarded to the forked test JVM automatically. Without this, Robolectric
      //  falls back to drawing a debug view-hierarchy diagram instead of real pixels, and every
      //  Roborazzi capture comes out as a diagram of colored boxes/property dumps, not a screenshot.
      all {
        it.systemProperty("robolectric.graphicsMode", "NATIVE")
      }
    }
  }
}

dependencies {

  extra["supportLibsVersion"] = "25.4.0"
  "implementation"(libs.androidx.appcompat)

  /* Used to provide out of the box icon font support. simplifies development, and provides scalable icons.
   *  the core is very very light. https://github.com/mikepenz/Android-Iconics
   */
  "implementation"("${libs.mikepenz.iconics.core.get()}@aar")
  "implementation"("${libs.mikepenz.google.material.typeface.get()}@aar")
  "implementation"("${libs.mikepenz.fontawesome.typeface.get()}@aar")

  /* https://github.com/mikepenz/MaterialDrawer */
  "implementation"("${libs.mikepenz.materialdrawer.get()}@aar")

  /* https://github.com/mikepenz/AboutLibraries */
  "implementation"("${libs.mikepenz.aboutlibraries.get()}@aar") {
    isTransitive = true
  }

  "testImplementation"(libs.junit)
  "testImplementation"(libs.junit.jupiter)
  "testRuntimeOnly"(libs.junit.vintage.engine)
  "testRuntimeOnly"(libs.junit.platform.launcher)

  // Roborazzi screen snapshot safety net (see ZehutYosef/src/test/.../snapshot).
  "testImplementation"(libs.robolectric)
  "testImplementation"(libs.roborazzi)
  "testImplementation"(libs.roborazzi.junit.rule)
  "testImplementation"(libs.androidx.test.core)
  "testImplementation"(libs.androidx.test.ext.junit)

  // Not used by this app (no Jetpack Compose here) -- Roborazzi's single facade class exposes
  //  both View- and Compose-based captureRoboImage() overloads, so javac needs these Compose
  //  testing types resolvable at compile time just to resolve the plain View overload we call.
  //  transitive = false: we only need the .class files to exist for overload resolution, and
  //  pulling their real transitive graphs (a much newer Kotlin/AndroidX/coroutines stack) fights
  //  this project's existing dependency locking/consistent resolution.
  "testCompileOnly"(libs.androidx.compose.ui.test) { isTransitive = false }
  "testCompileOnly"(libs.androidx.compose.ui.test.junit4) { isTransitive = false }
}

// JUnit4-style tests here (including Task 3's Roborazzi snapshot tests) keep running unmodified
//  via the vintage engine under the same JUnit Platform runner used by :ZmanUtils/:ZmanLib's
//  Jupiter-native tests. See docs/modernization/phase1 Task 5.
tasks.withType<Test> {
  useJUnitPlatform()
}
