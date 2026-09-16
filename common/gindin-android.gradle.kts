// This is a script applied via `apply(from = ...)`, not a precompiled script plugin, so the
//  `plugins {}` block isn't available here (Kotlin DSL only allows that block in a project's
//  own build script) and the type-safe `implementation(...)`/`api(...)` extension accessors
//  aren't generated for this file either -- both use their string-based equivalents instead.
//  (Applying "com.android.application" via `plugins {}` in the consuming module's own script
//  instead of here was considered, but that plugin also needs to be resolvable from the classic
//  buildscript classpath for common/gindin-versioning.gradle.kts's typed access to AGP's Variant
//  API further down the chain -- and Gradle refuses to apply the same plugin both via `plugins {}`
//  and via the buildscript classpath in the same build ("plugin is already on the classpath with
//  an unknown version"). So this keeps doing exactly what the original Groovy script did.)
apply(plugin = "com.android.application")

dependencies {
  "implementation"(fileTree(mapOf("include" to listOf("*.jar"), "dir" to "libs")))

  "implementation"(project(":ZmanLib"))
}
