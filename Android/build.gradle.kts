/*
 * Copyright (c) 2026. Jay R. Gindin
 */

import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.moallemi.gradle.internal.VersionCodeType
import java.io.FileInputStream
import java.util.Properties

// NOTE ON TYPED ACCESSORS THROUGHOUT THIS FILE: "com.android.application" is applied below via
//  `apply(from = "../common/gindin-android.gradle.kts")` (transitively) rather than this script's
//  own `plugins {}` block -- matching the original Groovy structure, and required so that the
//  AGP/moallemi classes referenced by common/gindin-versioning.gradle.kts (applied further down)
//  stay resolvable there too (see that file for why: those classes need to live on the classic
//  buildscript classpath, which conflicts with also declaring the same plugin via `plugins {}`).
//  The tradeoff is that Kotlin DSL only generates the type-safe bare `android {}` extension
//  function and the `implementation(...)`-style dependency accessors when a plugin is declared
//  via that script's own `plugins {}` block -- so this file uses their fully-typed-but-explicit
//  equivalents instead: `configure<BaseAppModuleExtension> {}` (the exact extension type the
//  generated `android {}` accessor itself resolves to here, given this repo's
//  `android.newDsl=false` in gradle.properties) and the string-keyed dependency notation.
apply(from = "../common/gindin-android.gradle.kts")

// Roborazzi: records/verifies golden PNG screenshots rendered by Robolectric. See
//  docs/modernization/phase1 and Android/src/test/java/com/gindin/zmanim/android/snapshot.
apply(plugin = "io.github.takahirom.roborazzi")

// Optional-apply pattern replacing the Groovy try/catch: zmanim.gradle is a never-committed,
//  gitignored file (see .hgignore, "^Android/zmanim.gradle") with the real release signing
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


project.extra["majorVersion"] = 4
project.extra["minorVersion"] = 0
project.extra["versionCodeType"] = VersionCodeType.AUTO_INCREMENT_ONE_STEP

apply(from = "../common/gindin-versioning.gradle.kts")

/**
 * Swizzle the APK name.
 *
 * Historically this used the old (now-removed) variant API:
 *   android.applicationVariants.all { variant -> variant.outputs.all { outputFileName = ... } }
 * (previously defined in common/gindin-versioning.gradle, shared with ZehutYosef). That API no
 *  longer exists as of the AGP version in use here. The AGP-recommended Kotlin DSL replacement
 *  is the androidComponents.onVariants {} Variant API -- see
 *  https://developer.android.com/build/build-variants#customize-artifact-name -- which exposes
 *  each output's file name as a settable Property<String> instead of a closure property. This
 *  now lives here (duplicated in ZehutYosef/build.gradle.kts) rather than in the shared
 *  common/gindin-versioning.gradle.kts script that used to hold the Groovy equivalent, because a
 *  script applied via `apply(from = ...)` can't safely hold a typed reference to AGP's Variant
 *  API types (see the comment in gindin-versioning.gradle.kts for the classloader-identity
 *  problem this ran into).
 */
extensions.getByType(ApplicationAndroidComponentsExtension::class.java).onVariants { variant ->
  variant.outputs.forEach { output ->
    output.outputFileName.set("${project.name}-${variant.flavorName}-${variant.name}-${output.versionName.get()}.apk")
  }
}

/*
 * Stuff that we need to load...
 *
 * NOTE: Since these files contain secret keys, they're never committed to source control. So...that means
 *  that not everyone will have them.
 */
val acraProps = Properties()
var acraUser: String
var acraPassword: String
try {
  // .use{} auto-closes the stream after load() - a harmless, intentional improvement over the
  // original Groovy, which never closed it explicitly.
  FileInputStream(project.projectDir.path + "/../common/AcraApiKey.properties").use { acraProps.load(it) }
  acraUser = "\"" + acraProps.getProperty("acra.user") + "\""
  acraPassword = "\"" + acraProps.getProperty("acra.password") + "\""
}
catch (ignored: Exception) {
  acraUser = "\"\""
  acraPassword = "\"\""
}


configure<BaseAppModuleExtension> {
  namespace = "com.gindin.zmanim.android"
  compileSdk = 36

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
    targetSdk = 36

    versionCode = project.extra["appVersionCode"] as Int
    versionName = project.extra["appVersionName"] as String
  }

  buildFeatures.buildConfig = true

  // NOTE: `buildFeatures` and `copy` were nested inside `defaultConfig {}` in the original
  //  Groovy, relying on Groovy's closure owner-chain fallback to resolve them against the
  //  enclosing `android {}` extension / the project respectively (the defaultConfig object has
  //  neither a `buildFeatures` nor a `copy` member of its own). Kotlin DSL lambdas don't fall
  //  back to an outer scope like that, so both are hoisted here as direct siblings -- same net
  //  effect (configuring the android extension's buildFeatures, and running the copy at
  //  project-configuration time), just addressed explicitly instead of implicitly.
  run {
    val replace = project.extra["appVersionName"] as String
    project.copy {
      from("src/template/assets/com/gindin/zmanim/android/about.html") {
        filter { line: String -> line.replace("@@VERSION@@", replace) }
      }
      into("src/main/assets/com/gindin/zmanim/android")
    }
  }

  buildTypes {
    getByName("release") {

      buildConfigField("String", "ACRA_KEY", acraPassword)
      buildConfigField("String", "ACRA_USER", acraUser)

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
      buildConfigField("String", "ACRA_KEY", "\"DEV\"")
      buildConfigField("String", "ACRA_USER", "\"DEV\"")
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

// Pin the JVM used to actually RUN this module's Test tasks (Robolectric/Roborazzi included) to
//  JDK 21, matching :ZmanUtils/:ZmanLib's zmanim.java-library-conventions toolchain and the
//  sourceCompatibility/targetCompatibility already declared above. `compileOptions {}` only
//  controls javac's bytecode target -- absent an explicit toolchain, the Test task instead runs
//  on whatever JDK the Gradle daemon itself happens to be launched with, which is NOT pinned and
//  can silently vary machine to machine. This matters here specifically because JDK 20+'s CLDR
//  locale data formats the AM/PM designator with U+202F (narrow no-break space) instead of a
//  plain ASCII space, which changes what Roborazzi's Robolectric-rendered golden screenshots
//  look like (see Task 1's :ZmanLib fix for the same underlying JDK/CLDR difference, and
//  docs/modernization/phase1 final-review-fix-report). AGP applies JavaBasePlugin internally, so
//  the plain `java {}` accessor -- which requires a `plugins {}`-block-declared plugin to be
//  generated by Kotlin DSL -- isn't available here (same reason `android {}` isn't, see the note
//  at the top of this file); `configure<JavaPluginExtension> {}` is the equivalent
//  fully-typed-but-explicit form that *is* resolvable, and both `JavaPluginExtension` and
//  `JavaLanguageVersion` are part of Gradle Kotlin DSL's default script imports, so no explicit
//  `import` is needed for either.
configure<JavaPluginExtension> {
  toolchain {
    languageVersion = JavaLanguageVersion.of(21)
  }
}

dependencies {
  // The net.xpece.android support libraries only exist in jcenter for the older version that
  //  is in use here (to support older Android OS versions). Since jcenter has gone away, I've
  //  just copied the necessary files into our libs directory.
  //  So, this now replaces "implementation 'net.xpece.android:support-preference:1.3.1'"
  // Newer versions are hosted on Maven Central.
  // See https://github.com/consp1racy/android-support-preference
  "api"(fileTree(mapOf("include" to listOf("*.aar"), "dir" to "libs")))

  "implementation"(project(":ZmanUtils"))

  "implementation"(libs.kosherjava.zmanim)

  "implementation"(libs.androidx.constraintlayout)
  "implementation"(libs.androidx.legacy.support.v4)
  "implementation"(libs.androidx.appcompat)
  "implementation"(libs.androidx.preference)
  "implementation"(libs.google.material)
  "implementation"(libs.play.services.location)

//  def acraVersion = "5.7.0"
//  implementation "ch.acra:acra-http:$acraVersion"
//  implementation "ch.acra:acra-toast:$acraVersion"
//  implementation "ch.acra:acra-limiter:$acraVersion"
//  implementation "ch.acra:acra-advanced-scheduler:$acraVersion"


  "testImplementation"(libs.junit)
  "testImplementation"(libs.junit.jupiter)
  "testRuntimeOnly"(libs.junit.vintage.engine)
  "testRuntimeOnly"(libs.junit.platform.launcher)
  "testImplementation"(libs.org.json)

  // Roborazzi screen snapshot safety net (see Android/src/test/.../snapshot).
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
