/*
 * Copyright (c) 2026. Jay R. Gindin
 */

import com.ncorti.ktfmt.gradle.KtfmtExtension

// Convention plugin applying the Kotlin formatter (ktfmt) and static analysis (detekt) to a module.
//  The Java, Kotlin multiplatform and Android application convention plugins all apply it, so
//  every module with Kotlin sources is checked the same way and the tool versions live in one
//  place.
//
//  ./gradlew ktfmtFormat   rewrites the Kotlin sources and build scripts into the canonical layout
//  ./gradlew ktfmtCheck    fails if any file is not in that layout (what CI runs)
//  ./gradlew detekt        runs the static analysis (what CI runs)

plugins {
  id("com.ncorti.ktfmt.gradle")
  id("dev.detekt")
}

configure<KtfmtExtension> {
  // Google style is ktfmt's 2-space layout, which is what this code base is written in; the
  //  default 100 columns is widened to the 120 the code base already keeps to.
  googleStyle()
  maxWidth.set(120)
}

detekt {
  // Only overrides of the defaults are kept, so the file lists exactly where this repo departs
  //  from detekt's own opinion, each with its reason.
  config.setFrom(rootProject.file("config/detekt/detekt.yml"))
  buildUponDefaultConfig = true

  // detekt's default source is src/main/kotlin and src/test/kotlin, which silently skips a
  //  multiplatform module's src/commonMain, src/jvmTest and the like. Taking every Kotlin
  //  directory under src/ gives all modules the same coverage, whatever their layout.
  source.setFrom(fileTree("src") { include("**/kotlin/**/*.kt") })
}
