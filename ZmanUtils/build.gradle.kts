/*
 * Copyright (c) 2026. Jay R. Gindin
 */

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType

plugins {
  id("zmanim.kotlin-multiplatform-conventions")
  alias(libs.plugins.android.kotlin.multiplatform.library)
}

kotlin {
  explicitApi()

  jvmToolchain(21)

  jvm()

  android {
    namespace = "com.gindin.zmanutils"
    compileSdk = 36
    minSdk = 24
  }

  // The event bus is keyed by java.lang.Class, which common code cannot reference, so it lives in a
  //  source set shared by the two JVM-based targets (the plain JVM one and Android).
  @OptIn(ExperimentalKotlinGradlePluginApi::class)
  applyDefaultHierarchyTemplate {
    common {
      group("jvmCommon") {
        withJvm()
        withCompilations { it.platformType == KotlinPlatformType.androidJvm }
      }
    }
  }

  sourceSets {
    commonTest.dependencies {
      // kotlin-test only -- NOT a direct JUnit 5 Jupiter dependency here. Jupiter does not run on
      //  non-JVM Kotlin/Native targets, and an iOS target is planned for this module. kotlin-test
      //  maps to JUnit on the JVM/Android targets without pulling in a JVM-only dependency that
      //  would break commonTest once iOS is added.
      implementation(libs.kotlin.test)
    }
    jvmTest.dependencies {
      implementation(libs.junit.jupiter)
      // No JUnit 4 tests remain in this module (fully migrated to Jupiter) -- the vintage engine is
      //  deliberately omitted here (unlike :Zmanim/:ZehutYosef, which still have real JUnit 4 test
      //  files) to avoid pulling junit:junit 4.13.2 (and its hamcrest-core 1.3) onto the test
      //  runtime classpath for no purpose.
      runtimeOnly(libs.junit.platform.launcher)
    }
  }
}

tasks.withType<Test>().configureEach { useJUnitPlatform() }

// Kotlin Multiplatform names its test tasks per target (jvmTest, ...) and registers no plain
//  `test` task, so a project-wide `./gradlew test` would silently skip this module. This lifecycle
//  task keeps that command running every target's tests, matching the other modules.
tasks.register("test") {
  group = "verification"
  description = "Runs the tests of every target in this module."
  dependsOn("allTests")
}
