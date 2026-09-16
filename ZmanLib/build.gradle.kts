/*
 * Copyright (c) 2026. Jay R. Gindin
 */

plugins {
  id("zmanim.java-library-conventions")
}

dependencies {
  api(fileTree(mapOf("include" to listOf("*.jar"), "dir" to "libs")))
  implementation(project(":ZmanUtils"))
  implementation(libs.kosherjava.zmanim)
  implementation(libs.jetbrains.annotations)
  compileOnly(libs.org.json)

  testImplementation(libs.junit)
  testImplementation(libs.org.json)
}

sourceSets {
  create("utils") {
    compileClasspath += sourceSets["main"].output
    compileClasspath += sourceSets["test"].output
    runtimeClasspath += sourceSets["main"].output
    runtimeClasspath += sourceSets["test"].output
  }
}

configurations {
  named("utilsImplementation") { extendsFrom(configurations["testImplementation"]) }
  // Unlike utilsImplementation/utilsCompileOnly, the java-library plugin does not create an
  //  "Api" configuration for non-main source sets automatically. The original Groovy
  //  `configurations { utilsApi.extendsFrom(api) }` relied on Gradle's dynamic-Groovy
  //  auto-vivification of unknown container elements to freshly create "utilsApi" here;
  //  maybeCreate() is the literal Kotlin DSL equivalent of that auto-creation.
  maybeCreate("utilsApi").extendsFrom(configurations["api"])
  named("utilsCompileOnly") { extendsFrom(configurations["compileOnly"]) }
}
