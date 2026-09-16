/*
 * Copyright (c) 2026. Jay R. Gindin
 */

plugins {
  id("zmanim.java-library-conventions")
}

dependencies {
  implementation(fileTree(mapOf("include" to listOf("*.jar"), "dir" to "libs")))

  testImplementation(libs.junit)
}
