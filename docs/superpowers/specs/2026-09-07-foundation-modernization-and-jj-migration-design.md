# Phase 1: Foundation Modernization, VCS Migration to Jujutsu (jj), and Visual Safety Net

**Date:** 2026-09-07  
**Status:** Approved  
**Scope:** `zmanim` repository (`:ZmanUtils`, `:ZmanLib`, `:Android`, `:ZehutYosef`) and preparation for `zehut-yosef-text` integration

---

## 1. Executive Summary & Context

The `zmanim` codebase is a multi-module Android project built around Jewish calendar and prayer calculations. The project has recently had Gradle upgraded to 9.6.0, the Android Gradle Plugin (AGP) upgraded to 9.4.0, and Java 21 toolchains configured. However, several technical debts and breakages exist before major feature work (such as the ZehutYosef daily prayer expansion and location provider overhaul) can proceed:

1. **`ZmanLib` Test Failures:** 52 tests fail because commit 1350 removed the deprecated `GR'A Kol Eliyahu` zman from `SofZmanShema.java` without updating the golden string test fixtures.
2. **`ZehutYosef:sidur` Compilation Failure:** The `sidur` product flavor fails compilation because `About.java` and `ZehutYosefActivity.java` reference strings, themes (`EliPurple`), and drawables (`header`) that exist only in `src/barMitzvah/res/`.
3. **Outdated Testing Framework:** Unit tests still run on JUnit 4 (4.13.2) rather than modern JUnit 5 (Jupiter).
4. **Outdated SDK Targets:** `:Android` specifies `minSdkVersion 14` (Ice Cream Sandwich, 2011), necessitating legacy local AAR workarounds (`support-preference-1.3.1.aar`), while `:ZehutYosef` specifies `minSdkVersion 22`. Both specify `compileSdk 33`.
5. **No Visual Regression Safety Net:** UI dependencies (Material Components 1.0.0 from 2018, ConstraintLayout 1.1.3, local Xpece preference AARs, MikePenz MaterialDrawer from 2015) are severely outdated, but cannot be safely upgraded without visual regression tests verifying that screens render without layout or styling regressions.
6. **No Kotlin / KMP in Base Modules:** Base utilities still use legacy Java constructs including a bespoke `com.gindin.util.Pair` class.
7. **VCS Legacy:** The repository is currently managed with Mercurial (`hg`), whereas the desired workflow is Jujutsu (`jj`), a modern Git-compatible VCS.
8. **Disabled Minification / Outdated Proguard Rules:** Both applications set `minifyEnabled false` with obsolete pre-AndroidX Proguard files.

This Phase 1 design resolves all of these baseline items in a disciplined, safety-first sequence.

---

## 2. Updated Stage Architecture

```
Stage 1A: Green Baseline & Immediate Visual Safety Net (Mercurial)
  ├── Task 1: Fix ZmanLib test fixtures for removed deprecated zmanim
  ├── Task 2: Provide fallback resources in ZehutYosef main/res (fix sidur compilation)
  └── Task 3: Setup Roborazzi Snapshot Safety Net (Record untouched golden baselines)
         │
         ▼
Stage 1B: Toolchain Modernization & KMP Preparation (Mercurial)
  ├── Task 4: Upgrade test suite to JUnit 5 (Jupiter 5.11+)
  ├── Task 5: Align Android SDK baselines (minSdkVersion 24, compileSdk 35)
  └── Task 6: Configure Kotlin Multiplatform (KMP), drop custom Pair, and port :ZmanUtils
         │
         ▼
Stage 1C: Version Control Migration from hg to jj
  └── Task 7: Fast-export hg history into Git, initialize colocated jj, convert .hgignore
         │
         ▼
Stage 1D: Controlled Library Modernization & Visual Parity Verification (Jujutsu)
  ├── Task 8: Upgrade Material (1.12), ConstraintLayout (2.2), AndroidX Preference;
  │           replace MikePenz MaterialDrawer/Iconics; verify 100% visual parity with Roborazzi
  └── Task 9: Modernize and enable Proguard / R8 minification (minifyEnabled true)
```

---

## 3. Detailed Component Specifications

### 3.1. Task 1: Stabilize `ZmanLib` Test Suite
- Realign formatted table test fixtures in `UT_ShemaTimes.java`, `UT_Zmanim.java`, `UT_ZmanimWithElevation.java`, `UT_WakeupScheduler_*.java`, and `UT_YomTovCandleLighting.java` to match `kosherjava 2.5.0` output without `GR'A Kol Eliyahu`.
- Verify all 191 unit tests succeed (`./gradlew :ZmanLib:test`).

### 3.2. Task 2: Fix `ZehutYosef:sidur` Compilation and Resource Architecture
- Add fallback `about_description_text` in `ZehutYosef/src/main/res/values/strings.xml`.
- Add fallback base theme `EliPurple` in `ZehutYosef/src/main/res/values/styles.xml` (overridden in `barMitzvah`).
- Add fallback neutral `header.xml` drawable in `ZehutYosef/src/main/res/drawable/` (overridden in `barMitzvah`).
- Verify both `sidur` and `barMitzvah` compile and assemble cleanly.

### 3.3. Task 3: Establish Roborazzi Screen Snapshot Safety Net (Untouched Baseline)
- Integrate `io.github.takahirom.roborazzi` (1.40+) with Robolectric 4.14+ Native Graphics (`robolectric.graphicsMode=NATIVE`).
- Implement snapshot tests:
  - `ZmanimActivity`: captures main zmanim screen (matching user reference photo `zmanim.png`).
  - `PersonalPreferencesActivity`: captures preferences and location options (matching `preferences.png` and `location.png`).
  - `ZehutYosefActivity`: captures Hebrew prayer reader and drawer header for both `barMitzvah` (matching `barmitzvah.png`) and `sidur` flavor.
- Run `./gradlew recordRoborazziDebug` to record the untouched golden baseline PNGs.
- This safety net guards all subsequent tasks against visual regressions.

### 3.4. Task 4: Upgrade Testing Framework to JUnit 5 (Jupiter)
- Add `org.junit.jupiter:junit-jupiter:5.11.4` and configure `test { useJUnitPlatform() }`.
- Migrate test suites across `:ZmanUtils`, `:ZmanLib`, and Android unit tests from JUnit 4 to JUnit 5 Jupiter annotations and assertions.

### 3.5. Task 5: Align SDK Baseline Targets Across Android Modules
- In `Android/build.gradle`: raise `minSdkVersion` to 24, set `compileSdk` to 35.
- In `ZehutYosef/build.gradle`: raise `minSdkVersion` to 24, set `compileSdk` to 35.
- Verify `./gradlew assembleDebug verifyRoborazziDebug` passes cleanly.

### 3.6. Task 6: Configure Kotlin Multiplatform (KMP), Drop Custom `Pair`, and Migrate `:ZmanUtils`
- Add Kotlin Gradle plugins (`kotlin-multiplatform`, `kotlin-android`) with Java 21 / 25 toolchains.
- Delete `com.gindin.util.Pair` in favor of standard `kotlin.Pair` across all call sites in `:ZmanLib` and `:Android`.
- Configure `:ZmanUtils` as a Kotlin Multiplatform library (`kotlin("multiplatform")` targeting JVM + Android, ready for iOS/Desktop).
- Port `EventBus`, `Event`, `EventHandler`, `EventCacheControl`, and MVP contracts to idiomatic Kotlin with proper null-safety and Java-interop annotations.
- Define platform-agnostic `TelemetryTracker` interface in `commonMain` for privacy-first analytics and session measurement.
- Rewrite unit tests in idiomatic Kotlin with JUnit 5.
- Verify `./gradlew test assembleDebug verifyRoborazziDebug` succeeds 100%.

### 3.7. Task 7: Migrate Version Control from `hg` to `jj`
- Use `fast-export` to stream full Mercurial history (1356+ changesets) into Git (`.git`).
- Initialize colocated Jujutsu: `jj git init --colocated`.
- Convert `.hgignore` to `.gitignore` and archive `.hg/` to `.hg.backup/`.
- Verify `jj status`, `jj log -r '::@'`, and `./gradlew test assembleDebug verifyRoborazziDebug` pass.

### 3.8. Task 8: Comprehensive Library Modernization & Visual Parity Verification
- **Android Module:**
  - Remove local Xpece AARs (`support-preference-1.3.1.*`, `support-spinner-1.3.1.*`).
  - Migrate preference XMLs to standard `androidx.preference.*`.
  - Upgrade `material` (1.0 ➔ 1.12.0), `constraintlayout` (1.1.3 ➔ 2.2.0), `appcompat` (1.6.1 ➔ 1.7.0), `play-services-location` (21.0.1 ➔ 21.3.0).
  - Remove deprecated `androidx.legacy:legacy-support-v4:1.0.0`.
- **ZehutYosef Module:**
  - Replace legacy `com.mikepenz:materialdrawer:3.0.8` with standard Material `DrawerLayout` + `NavigationView`.
  - Replace `com.mikepenz:iconics-core` with standard Android Vector Drawables.
  - Modernize or replace `aboutlibraries`.
- **Visual Verification:**
  - Run `./gradlew verifyRoborazziDebug`.
  - Inspect diff reports and adjust theme attributes until 100% visual parity with Task 3 baselines is achieved.

### 3.9. Task 9: Modernize & Enable Proguard / R8 Minification and Android App Bundle (.aab) Packaging
- Remove obsolete pre-AndroidX Proguard files (`proguard-support-v7-appcompat.pro`, etc.).
- Configure modern R8 rules in `proguard-rules.pro` protecting reflection and serialization points.
- Enable `minifyEnabled true` in `buildTypes.release` for `:Android` and `:ZehutYosef`.
- Verify `./gradlew assembleRelease bundleRelease` produces optimized, minified APKs and Google Play App Bundles (`.aab`).
- Verify minified Roborazzi/unit tests pass cleanly.

---

## 4. Success Criteria & Deliverables
- [ ] Roborazzi visual regression tests pass with 0 regressions against recorded baselines.
- [ ] Unit tests pass 100% on JUnit 5 across all modules.
- [ ] `:ZmanUtils` is structured as a Kotlin Multiplatform library in idiomatic Kotlin with no custom `Pair`, including `TelemetryTracker`.
- [ ] All Android modules build with `minSdkVersion 24` and `compileSdk 35`.
- [ ] Repository is fully migrated to colocated Jujutsu (`jj`).
- [ ] Local legacy AARs and obsolete MikePenz libraries are completely retired in favor of standard AndroidX/Material components.
- [ ] Release builds assemble with R8 minification enabled (`minifyEnabled true`) and generate verified `.aab` bundles.
