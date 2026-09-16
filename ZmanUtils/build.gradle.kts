/*
 * Copyright (c) 2026. Jay R. Gindin
 */

plugins {
  id("zmanim.java-library-conventions")
}

dependencies {
  implementation(fileTree(mapOf("include" to listOf("*.jar"), "dir" to "libs")))

  testImplementation(libs.junit.jupiter)
  // No JUnit 4 tests remain in this module (fully migrated to Jupiter) -- the vintage engine is
  //  deliberately omitted here (unlike :Android/:ZehutYosef, which still have real JUnit 4 test
  //  files) to avoid pulling junit:junit 4.13.2 (and its hamcrest-core 1.3) onto the test runtime
  //  classpath for no purpose. See docs/modernization/phase1 final-review-fix-report.
  testRuntimeOnly(libs.junit.platform.launcher)
}

tasks.withType<Test> {
  useJUnitPlatform()
}
