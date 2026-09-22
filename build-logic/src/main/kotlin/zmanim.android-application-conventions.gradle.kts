/*
 * Copyright (c) 2026. Jay R. Gindin
 */

import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import org.gradle.api.artifacts.VersionCatalogsExtension

// Convention plugin holding everything :Android and :ZehutYosef configure identically.
//
// This replaces the former `apply(from = "../common/gindin-android.gradle.kts")` /
//  `apply(from = "../common/gindin-versioning.gradle.kts")` script-plugin pair plus a large block
//  of copy-pasted config in each app module. Because this IS a real, precompiled script plugin,
//  the plugins below are declared in a normal `plugins {}` block, which means:
//    * Kotlin DSL generates the type-safe `android {}` / `implementation(...)` accessors here,
//      so no `configure<BaseAppModuleExtension> {}` / string-keyed-configuration workarounds; and
//    * there is exactly ONE classloader for each plugin's classes, so the advanced-build-version
//      extension can be referenced by its real type (see com.gindin.buildlogic.Versioning) instead
//      of being driven dynamically through `groovy.lang.GroovyObject`. The classloader-identity
//      problem documented at length in the deleted common/*.gradle.kts scripts was a consequence
//      of `apply(from = ...)` not inheriting the applying project's buildscript classpath; it does
//      not exist for a plugin declared this way.
plugins {
  id("com.android.application")
  id("org.moallemi.advanced-build-version")

  // Roborazzi: records/verifies golden PNG screenshots rendered by Robolectric. See
  //  docs/modernization/phase1 and each app's src/test/.../snapshot package.
  id("io.github.takahirom.roborazzi")
}

// Version-catalog accessors (`libs.junit`, ...) are not generated inside precompiled script
//  plugins - see gradle/gradle#15383. Looking the catalog up through VersionCatalogsExtension is
//  the supported workaround and still keeps every version in gradle/libs.versions.toml.
val libs = extensions.getByType(VersionCatalogsExtension::class.java).named("libs")

android {
  compileSdk = 36

  defaultConfig {
    targetSdk = 36
  }

  buildFeatures.buildConfig = true

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
      //  itself; it is NOT forwarded to the forked test JVM automatically. Without this,
      //  Robolectric falls back to drawing a debug view-hierarchy diagram instead of real pixels,
      //  and every Roborazzi capture comes out as a diagram of colored boxes/property dumps, not
      //  a screenshot.
      all {
        it.systemProperty("robolectric.graphicsMode", "NATIVE")
      }
    }
  }
}

/**
 * Swizzle the APK name.
 *
 * Historically this used the old (now-removed) variant API:
 *   android.applicationVariants.all { variant -> variant.outputs.all { outputFileName = ... } }
 * (originally in common/gindin-versioning.gradle, shared with ZehutYosef). That API no longer
 *  exists as of the AGP version in use here. The AGP-recommended Kotlin DSL replacement is the
 *  androidComponents.onVariants {} Variant API -- see
 *  https://developer.android.com/build/build-variants#customize-artifact-name -- which exposes
 *  each output's file name as a settable Property<String> instead of a closure property.
 *
 * This was briefly duplicated byte-for-byte in Android/build.gradle.kts and
 *  ZehutYosef/build.gradle.kts, because a script applied via `apply(from = ...)` could not safely
 *  hold a typed reference to AGP's Variant API types. A real convention plugin can, so it is back
 *  to living in exactly one place.
 */
extensions.getByType(ApplicationAndroidComponentsExtension::class.java).onVariants { variant ->
  variant.outputs.forEach { output ->
    output.outputFileName.set("${project.name}-${variant.flavorName}-${variant.name}-${output.versionName.get()}.apk")
  }
}

// Pin the JVM used to actually RUN these modules' Test tasks (Robolectric/Roborazzi included) to
//  JDK 21, matching :ZmanUtils/:ZmanLib's zmanim.java-library-conventions toolchain and the
//  sourceCompatibility/targetCompatibility declared above. `compileOptions {}` only controls
//  javac's bytecode target -- absent an explicit toolchain, the Test task instead runs on whatever
//  JDK the Gradle daemon itself happens to be launched with, which is NOT pinned and can silently
//  vary machine to machine. This matters here specifically because JDK 20+'s CLDR locale data
//  formats the AM/PM designator with U+202F (narrow no-break space) instead of a plain ASCII
//  space, which changes what Roborazzi's Robolectric-rendered golden screenshots look like (see
//  Task 1's :ZmanLib fix for the same underlying JDK/CLDR difference, and
//  docs/modernization/phase1 final-review-fix-report). AGP applies JavaBasePlugin internally
//  rather than via a `plugins {}` block, so the bare `java {}` accessor isn't generated even here;
//  `configure<JavaPluginExtension> {}` is the equivalent typed form that is always resolvable.
configure<JavaPluginExtension> {
  toolchain {
    languageVersion = JavaLanguageVersion.of(21)
  }
}

dependencies {
  // Local jars dropped into each app's libs/ directory.
  implementation(fileTree(mapOf("include" to listOf("*.jar"), "dir" to "libs")))

  implementation(project(":ZmanLib"))

  testImplementation(libs.findLibrary("junit").get())
  testImplementation(libs.findLibrary("junit-jupiter").get())
  testRuntimeOnly(libs.findLibrary("junit-vintage-engine").get())
  testRuntimeOnly(libs.findLibrary("junit-platform-launcher").get())

  // Roborazzi screen snapshot safety net (see each app's src/test/.../snapshot).
  testImplementation(libs.findLibrary("robolectric").get())
  testImplementation(libs.findLibrary("roborazzi").get())
  testImplementation(libs.findLibrary("roborazzi-junit-rule").get())
  testImplementation(libs.findLibrary("androidx-test-core").get())
  testImplementation(libs.findLibrary("androidx-test-ext-junit").get())

  // Not used by these apps (no Jetpack Compose here) -- Roborazzi's single facade class exposes
  //  both View- and Compose-based captureRoboImage() overloads, so javac needs these Compose
  //  testing types resolvable at compile time just to resolve the plain View overload we call.
  //  transitive = false: we only need the .class files to exist for overload resolution, and
  //  pulling their real transitive graphs (a much newer Kotlin/AndroidX/coroutines stack) fights
  //  this project's existing dependency locking/consistent resolution.
  testCompileOnly(libs.findLibrary("androidx-compose-ui-test").get()) { isTransitive = false }
  testCompileOnly(libs.findLibrary("androidx-compose-ui-test-junit4").get()) { isTransitive = false }
}

// JUnit4-style tests in these modules (including the Roborazzi snapshot tests) keep running
//  unmodified via the vintage engine under the same JUnit Platform runner used by
//  :ZmanUtils/:ZmanLib's Jupiter-native tests. See docs/modernization/phase1 Task 5.
tasks.withType<Test> {
  useJUnitPlatform()
}
