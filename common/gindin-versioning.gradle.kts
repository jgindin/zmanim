/**
 * Configures the versioning for the app.
 *
 * Each app needs 3 lines prior to applying this file which look something like:
 *
 *    project.extra["majorVersion"] = 1
 *    project.extra["minorVersion"] = 0
 *    project.extra["versionCodeType"] = VersionCodeType.AUTO_INCREMENT_ONE_STEP
 *
 */

// This is a script applied via `apply(from = ...)`, not a precompiled script plugin. Kotlin DSL
//  script plugins applied that way do NOT inherit the applying project's buildscript classpath
//  for their own compilation (confirmed independently with a minimal repro -- a genuine Kotlin
//  DSL limitation, not specific to this plugin or to AGP). Giving this script its own local
//  `buildscript {}` block fixes *compilation*, but then the plugin extension *type* it resolves
//  comes from a different classloader instance than the one Gradle used to actually apply the
//  plugin to the project elsewhere in this build, and `extensions.getByType(X::class.java)`
//  fails at *runtime* ("Extension of type X does not exist", even though the registered-types
//  list in that same error names X) -- also confirmed. So rather than importing the
//  advanced-build-version plugin's internal types at all, this talks to its extension the same
//  way Groovy always could: purely dynamically via `GroovyObject`, which is part of Gradle's own
//  embedded Groovy runtime (always resolvable, unlike a third-party plugin's classes) and free
//  of the classloader-identity problem since method dispatch happens by name, not by static type.
apply(plugin = "org.moallemi.advanced-build-version")

val advancedVersioning = extensions.getByName("advancedVersioning") as groovy.lang.GroovyObject

advancedVersioning.invokeMethod("nameOptions", closureOf<Any> {
  val nameOptions = this as groovy.lang.GroovyObject
  nameOptions.invokeMethod("versionMajor", arrayOf(project.extra["majorVersion"]))
  nameOptions.invokeMethod("versionMinor", arrayOf(project.extra["minorVersion"]))
})
advancedVersioning.invokeMethod("codeOptions", closureOf<Any> {
  val codeOptions = this as groovy.lang.GroovyObject
  codeOptions.invokeMethod("versionCodeType", arrayOf(project.extra["versionCodeType"]))
})

/*

  // This no longer works with the updated Android Gradle Plugin. You wind up with an error:
  //  Cannot set the value of read-only property 'outputFile' for ApkVariantOutputImpl_Decorated{apkData=Main{type=MAIN, fullName=debug, filters=[]}} of type com.android.build.gradle.internal.api.ApkVariantOutputImpl.
  //
  // The block below (now the androidComponents.onVariants{} block in each module's own
  //  build.gradle.kts -- see the comment there for why it isn't here) shows the way to make
  //  this work.

  outputOptions {
    renameOutput true
    nameFormat '${appName}-${flavorName}-${buildType}-${versionName}'
  }
*/

val appVersionCode = advancedVersioning.getProperty("versionCode") as Int
val appVersionName = advancedVersioning.getProperty("versionName").toString() + "." + appVersionCode

project.extra["appVersionCode"] = appVersionCode
project.extra["appVersionName"] = appVersionName

// NOTE: The APK-renaming logic that used to live here too (translating
//  `android.applicationVariants.all { variant -> variant.outputs.all { outputFileName = ... } }`)
//  now lives in each consuming module's own build.gradle.kts (Android/, ZehutYosef/) instead of
//  here. That logic needs typed access to AGP's Variant API
//  (ApplicationAndroidComponentsExtension, VariantOutput, ...), which -- unlike the
//  advanced-build-version plugin's extension above -- is a plain Kotlin interface, not a
//  GroovyObject, so it can't be driven purely dynamically by name the same way. Since a plain
//  applied-from script can't safely hold a typed reference to it (see the classloader-identity
//  problem explained above), and each module's own entry-point build.gradle.kts *does* already
//  have working typed access to AGP's classes (confirmed: it's what makes the `BaseAppModuleExtension`
//  usage below/above work), that's where the rename logic now lives instead. Search this repo's
//  Android/build.gradle.kts and ZehutYosef/build.gradle.kts for "Swizzle the APK name" to find it.
