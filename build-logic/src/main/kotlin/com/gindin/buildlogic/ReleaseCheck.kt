/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.buildlogic

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.android.build.api.variant.HostTestBuilder
import com.android.build.gradle.internal.tasks.L8DexDesugarLibTask
import org.gradle.api.Project

/** The build type that runs the on-device tests against R8-minified code. */
const val RELEASE_CHECK_BUILD_TYPE = "releaseCheck"

/** Gradle property naming the build type whose variants get on-device test tasks. */
private const val TEST_BUILD_TYPE_PROPERTY = "zmanim.testBuildType"

/**
 * Adds the `releaseCheck` build type: the `release` build exactly as configured by the module (same
 * R8 and resource shrinking, same proguard-rules.pro, not debuggable) but signed with the debug
 * key, so that CI, which has no upload key, can install it and run the on-device tests against the
 * minified code the users get. Call it after the module's `release` build type and
 * [zmanimReleaseSigning] are set up, because `initWith` copies what is there at the time of the
 * call.
 *
 * Two extra rule files, both next to the module's proguard-rules.pro, exist only for the tests:
 * - `proguard-androidTest-app.pro` joins the app's own rules for this build type only. It keeps the
 *   app's entry points that the tests call (to seed state and to drive real code paths) and the
 *   parts of the libraries that the test APK shares with the app, all of which R8 would otherwise
 *   inline, strip or remove from the app. It must never keep a name that proguard-rules.pro
 *   protects, or the tests could no longer notice that rule being dropped.
 * - `proguard-androidTest.pro` is passed through `testProguardFiles`, which AGP applies when it
 *   runs R8 on the test APK of a minified app. AGP also hands that R8 run the app's mapping, so the
 *   test code's references to app classes follow the app's renaming.
 *
 * AGP builds on-device test tasks for one build type per module only (`testBuildType`, `debug` by
 * default). Pass `-Pzmanim.testBuildType=releaseCheck` to get `connectedReleaseCheckAndroidTest`
 * (and, in :ZehutYosef, `connected<Flavor>ReleaseCheckAndroidTest`) instead of the debug ones.
 *
 * The JVM unit tests are not built for this build type: they never see R8's output, so they would
 * only repeat what the debug and release unit tests already run.
 *
 * The desugared java.time library (core library desugaring) is neither shrunk nor renamed in this
 * build type. Both the app and the test APK (androidx.test uses java.time) carry their own copy of
 * it, and AGP shrinks and renames each copy on its own, so the same `j$` name can mean different
 * classes in the two APKs. They load into one class loader with the test APK first, and the app
 * then crashes with a VerifyError. Kept whole and unrenamed, the two copies are identical, as they
 * are in debug builds. The release build still shrinks its copy; only that step goes untested.
 */
fun Project.zmanimReleaseCheckBuildType() {

  val android = extensions.getByType(ApplicationExtension::class.java)
  android.buildTypes.create(RELEASE_CHECK_BUILD_TYPE) {
    initWith(android.buildTypes.getByName("release"))
    signingConfig = android.signingConfigs.getByName("debug")
    matchingFallbacks += "release"
    proguardFile("proguard-androidTest-app.pro")
    testProguardFile("proguard-androidTest.pro")
  }

  providers.gradleProperty(TEST_BUILD_TYPE_PROPERTY).orNull?.let { android.testBuildType = it }

  val androidComponents = extensions.getByType(ApplicationAndroidComponentsExtension::class.java)
  androidComponents.beforeVariants(
    androidComponents.selector().withBuildType(RELEASE_CHECK_BUILD_TYPE)
  ) { variant ->
    variant.hostTests[HostTestBuilder.UNIT_TEST_TYPE]?.enable = false
  }

  // AGP has no public setting for this, so it reaches the (internal) L8 task directly. Its names
  // are l8DexDesugarLib<Variant> and l8DexDesugarLib<Variant>AndroidTest.
  tasks.withType(L8DexDesugarLibTask::class.java).configureEach {
    if (name.contains(RELEASE_CHECK_BUILD_TYPE.replaceFirstChar { it.uppercase() })) {
      keepRulesConfigurations.addAll("-dontshrink", "-dontobfuscate", "-dontoptimize")
    }
  }
}
