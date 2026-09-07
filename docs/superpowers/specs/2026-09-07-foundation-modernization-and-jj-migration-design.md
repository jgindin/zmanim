# Phase 1: Foundation Modernization, VCS Migration to Jujutsu (jj), and Visual Safety Net

**Date:** 2026-09-07  
**Status:** Under Review  
**Scope:** `zmanim` repository (`:ZmanUtils`, `:ZmanLib`, `:Android`, `:ZehutYosef`) and preparation for `zehut-yosef-text` integration

---

## 1. Executive Summary & Context

The `zmanim` codebase is a multi-module Android project built around Jewish calendar and prayer calculations. The project has recently had Gradle upgraded to 9.6.0, the Android Gradle Plugin (AGP) upgraded to 9.4.0, and Java 21 toolchains configured. However, several technical debts and breakages exist before major feature work (such as the ZehutYosef daily prayer expansion and location provider overhaul) can proceed:

1. **`ZmanLib` Test Failures:** 52 tests fail because commit 1350 removed the deprecated `GR'A Kol Eliyahu` zman from `SofZmanShema.java` without updating the golden string test fixtures.
2. **`ZehutYosef:sidur` Compilation Failure:** The `sidur` product flavor fails compilation because `About.java` and `ZehutYosefActivity.java` reference strings, themes (`EliPurple`), and drawables (`header`) that exist only in `src/barMitzvah/res/`.
3. **Outdated SDK Targets:** `:Android` specifies `minSdkVersion 14` (Ice Cream Sandwich, 2011), necessitating legacy local AAR workarounds (`support-preference-1.3.1.aar`), while `:ZehutYosef` specifies `minSdkVersion 22`. Both specify `compileSdk 33`.
4. **VCS Legacy:** The repository is currently managed with Mercurial (`hg`), whereas the desired workflow is Jujutsu (`jj`), a modern Git-compatible VCS.
5. **No Visual Regression Safety Net:** UI dependencies (Material Components 1.0.0 from 2018, ConstraintLayout 1.1.3, local Xpece preference AARs) are severely outdated, but cannot be safely upgraded without visual regression tests verifying that screens render without layout or styling regressions.
6. **No Kotlin in Base Modules:** Base utilities still use legacy Java constructs including a bespoke `com.gindin.util.Pair` class.

This Phase 1 design resolves all of these baseline items in a disciplined, four-stage sequence.

---

## 2. Four-Stage Execution Architecture

```
Stage 1A: Green Baseline & Tech Debt Cleanup (Mercurial)
  ├── Fix ZmanLib test fixtures for removed deprecated zmanim
  ├── Move common ZehutYosef resources to src/main/res (fix sidur compilation)
  ├── Raise Android & ZehutYosef to minSdkVersion 24, compileSdk 35
  ├── Remove com.gindin.util.Pair in favor of idiomatic kotlin.Pair
  ├── Add Kotlin Gradle plugin (2.1+) with Java 21 toolchain across all modules
  ├── Migrate :ZmanUtils to idiomatic Kotlin
  └── Verification Gate: `./gradlew test assembleDebug` passes 100% in hg
         │
         ▼
Stage 1B: Version Control Migration from hg to jj
  ├── Fast-export Mercurial history into a Git repository (.git)
  ├── Initialize Jujutsu in colocated mode: `jj git init --colocated`
  ├── Convert .hgignore to .gitignore and archive .hg/
  └── Verification Gate: `jj status`, `jj log`, and `./gradlew test assembleDebug` pass
         │
         ▼
Stage 1C: Visual Regression Safety Net (Host JVM Screen Snapshots)
  ├── Integrate Roborazzi + Robolectric Native Graphics in `./gradlew testDebugUnitTest`
  ├── Implement snapshot tests for ZmanimActivity, PersonalPreferences, and ZehutYosefActivity
  ├── Record golden baseline images: `./gradlew recordRoborazziDebug`
  └── Verification Gate: `./gradlew verifyRoborazziDebug` passes 100% on golden images
         │
         ▼
Stage 1D: Controlled Library Modernization & Visual Parity Verification
  ├── Remove local Xpece AARs and update preference XMLs to standard androidx.preference.*
  ├── Upgrade Material Components (1.0 ➔ 1.12), ConstraintLayout (1.1 ➔ 2.2), AppCompat (1.6 ➔ 1.7)
  ├── Run `./gradlew verifyRoborazziDebug` to detect any layout/styling shifts
  ├── Reconcile themes and styles until zero visual diff remains
  └── Final Phase 1 Gate: Full verification clean and committed in jj
```

---

## 3. Detailed Component Specifications

### 3.1. Stage 1A: Green Baseline & Tech Debt Cleanup

#### 3.1.1. `ZmanLib` Test Stabilization
- **Root Cause:** Commit 1350 removed `GR'A Kol Eliyahu` from `SofZmanShema.java`. Tests in `UT_ShemaTimes`, `UT_Zmanim`, `UT_ZmanimWithElevation`, `UT_WakeupScheduler_*`, and `UT_YomTovCandleLighting` assert formatted tables that still expect the removed column.
- **Action:**
  - Update table headers in the test classes from:
    `| GR'A | GR'A Kol Eliyahu | MG'A |`
    to:
    `| GR'A | MG'A |`
  - Remove the corresponding time column from all test fixture rows.
  - Re-run `./gradlew :ZmanLib:test` until all 191 unit tests succeed.

#### 3.1.2. `ZehutYosef` Flavor Resource Architecture
- **Root Cause:** `About.java` and `ZehutYosefActivity.java` live in `src/main/java`, but reference `R.string.about_description_text`, `R.style.EliPurple`, and `R.drawable.header`, which only exist in `src/barMitzvah/res/`.
- **Action:**
  - In `ZehutYosef/src/main/res/values/strings.xml`: Add generic default `about_description_text` for the Siddur.
  - In `ZehutYosef/src/main/res/values/styles.xml`: Define a default base theme (`Theme.ZehutYosef`) and alias/stub for `EliPurple` inheriting from the base theme; allow `barMitzvah/res/values/purple_gray_style.xml` to override it with the custom Bar Mitzvah palette.
  - In `ZehutYosef/src/main/res/drawable/`: Add a neutral default `header.xml` drawable; allow `barMitzvah/res/drawable/` to override with custom header artwork.
  - Verify compilation with `./gradlew :ZehutYosef:compileSidurDebugJavaWithJavac :ZehutYosef:assembleDebug`.

#### 3.1.3. SDK Baseline Alignment
- In `Android/build.gradle`:
  - Change `minSdkVersion 14` to `minSdkVersion 24` (Android 7.0 Nougat).
  - Change `compileSdk 33` to `compileSdk 35` (Android 15).
  - Keep `targetSdkVersion 35`.
- In `ZehutYosef/build.gradle`:
  - Change `minSdkVersion 22` to `minSdkVersion 24`.
  - Change `compileSdk 33` to `compileSdk 35`.
  - Keep `targetSdkVersion 35`.

#### 3.1.4. Elimination of Custom `Pair` & Idiomatic Kotlin Migration for `:ZmanUtils`
- Delete `com.gindin.util.Pair` in `:ZmanUtils`.
- Replace all call sites across `:ZmanLib` and `:Android` with standard `kotlin.Pair` (e.g. `Pair(first, second)` or `kotlin.Pair<A, B>`).
- Add Kotlin Gradle plugin `org.jetbrains.kotlin.jvm` to `:ZmanUtils` and `:ZmanLib`, and `org.jetbrains.kotlin.android` to `:Android` and `:ZehutYosef`.
- Migrate `:ZmanUtils` classes to idiomatic Kotlin:
  - `Event.kt`: idiomatic Kotlin interface.
  - `EventCacheControl.kt`: Kotlin enum.
  - `EventHandler.kt`: functional interface (`fun interface`).
  - `EventBus.kt`: idiomatic thread-safe Kotlin implementation using `ConcurrentHashMap`, Kotlin null-safety, and `@JvmStatic` / `@JvmOverloads` annotations where necessary for Java consumers.
  - `MvpModule.kt`, `Presenter.kt`, `View.kt`, `AbstractPresenter.kt`: clean generic interfaces and base presenter in Kotlin.
  - `UT_EventBus.kt`: rewrite unit test suite in Kotlin using standard JUnit assertions.

---

### 3.2. Stage 1B: Version Control Migration from `hg` to `jj`

#### 3.2.1. History Conversion
- Using `fast-export` (via Python 3 and system `mercurial 7.2` package), stream the entire Mercurial history (1356+ changesets) into a Git repository in `zmanim/.git`.
- Map the active Mercurial bookmark `zmanim-v4` to the Git `zmanim-v4` branch.

#### 3.2.2. Colocated Jujutsu Setup
- Execute `jj git init --colocated` in the `zmanim` repository root.
- Ensure the current working copy commit is set to the head of `zmanim-v4`.
- Convert `.hgignore` to `.gitignore`:
  ```gitignore
  # Build artifacts
  build/
  .gradle/
  ijcompile/
  var/
  
  # IDE files
  .idea/
  *.iml
  
  # Local configuration & keys
  local.properties
  *.keystore
  common/ProjectApiKey.txt
  Android/zmanim.gradle
  ZehutYosef/zehut-yosef.gradle
  
  # Mercurial legacy metadata
  .hg/
  .hgignore
  .hgtags
  *.orig
  ```
- Archive the old `.hg` directory to `.hg.backup` to prevent SCM conflicts while preserving raw history.
- Run `jj status` and `jj log -r '::@'` to confirm seamless operation.

---

### 3.3. Stage 1C: Visual Regression Safety Net

#### 3.3.1. Roborazzi + Robolectric Native Graphics Harness
- Add Roborazzi plugin `io.github.takahirom.roborazzi` (version 1.40+) to root `build.gradle` and `Android/build.gradle`.
- In `gradle.properties`, enable native graphics mode:
  ```properties
  robolectric.graphicsMode=NATIVE
  ```
- Roborazzi executes on the host Linux machine without an emulator by using Skia graphics rendering inside Robolectric.

#### 3.3.2. Screen Snapshot Test Implementations
- **`Android/src/test/java/com/gindin/zmanim/android/snapshot/ZmanimSnapshotTest.kt`**:
  - Test 1: `captureMainScreen()` — Launches `ZmanimActivity` with a fixed test date (e.g. November 1, 2024) and fixed location (Seattle, WA), capturing the daily list of zmanim and header.
  - Test 2: `capturePreferencesScreen()` — Launches `PersonalPreferences` activity/fragment, capturing preference categories and toggles.
- **`ZehutYosef/src/test/java/com/gindin/zehutyosef/snapshot/SiddurSnapshotTest.kt`**:
  - Test 1: `captureSiddurReader()` — Launches `ZehutYosefActivity` rendering Hebrew prayer text with bundled fonts.
  - Test 2: `captureBarMitzvahReader()` — Launches `barMitzvah` flavor activity rendering customized header and theme.

#### 3.3.3. Recording Baseline Images
- Run `./gradlew recordRoborazziDebug`.
- Commit generated golden PNG images into `screenshots/` under `jj`.
- Verify with `./gradlew verifyRoborazziDebug`.

---

### 3.4. Stage 1D: Controlled Library Modernization & Visual Parity Verification

#### 3.4.1. Retire Local AARs & Migrate Preferences
- Remove `Android/libs/support-preference-1.3.1.*` and `support-spinner-1.3.1.*`.
- In `Android/build.gradle`, delete `api fileTree(include: ['*.aar'], dir: 'libs')`.
- In `Android/src/main/res/xml/personal_prefs.xml` and `personal_prefs_oreo.xml`:
  - Replace `net.xpece.android.support.preference.CheckBoxPreference` with `androidx.preference.CheckBoxPreference`.
  - Replace other `net.xpece` classes with modern `androidx.preference.*` counterparts.
- Remove deprecated dependency `androidx.legacy:legacy-support-v4:1.0.0`.

#### 3.4.2. Upgrade Core UI & Google Libraries
- Update `Android/build.gradle`:
  - `com.google.android.material:material`: `1.0.0` ➔ `1.12.0`
  - `androidx.constraintlayout:constraintlayout`: `1.1.3` ➔ `2.2.0`
  - `androidx.appcompat:appcompat`: `1.6.1` ➔ `1.7.0`
  - `com.google.android.gms:play-services-location`: `21.0.1` ➔ `21.3.0`

#### 3.4.3. Visual Parity Verification
- Execute `./gradlew verifyRoborazziDebug`.
- If differences are detected, inspect the HTML diff report in `build/reports/roborazzi/`.
- Adjust XML styling or theme attributes to eliminate any unintentional regressions until all tests pass.
- Execute full suite: `./gradlew test assembleDebug verifyRoborazziDebug`.
- Commit the finalized modernized baseline into Jujutsu (`jj commit`).

---

## 4. Risks & Mitigations

| Risk | Impact | Mitigation |
|---|---|---|
| Material 1.12 theme conflicts with older `Theme.AppCompat` | Medium | Roborazzi visual regression tests will highlight any color, font, or widget shifts immediately before any code is committed. |
| Kotlin interop issues with remaining Java modules | Low | Port `:ZmanUtils` first, maintaining Java getter/setter conventions and standard `@JvmStatic` / `@JvmOverloads` annotations where needed. |
| Hg to Git history translation discrepancies | Low | Mercurial tags and `zmanim-v4` bookmark will be cross-referenced against `git log` before archiving `.hg/`. |
| Robolectric graphics rendering differences across environments | Low | Roborazzi native graphics tests use deterministic seed data, fixed dates, fixed locale, and fixed screen dimensions (e.g. Pixel 6 layout). |

---

## 5. Success Criteria & Phase 1 Deliverables
- [ ] `./gradlew test` passes 100% across all 4 modules (`:ZmanUtils`, `:ZmanLib`, `:Android`, `:ZehutYosef`).
- [ ] Both `barMitzvah` and `sidur` flavors of `:ZehutYosef` assemble cleanly (`./gradlew assembleDebug`).
- [ ] `:ZmanUtils` is written in idiomatic Kotlin with zero custom `Pair` classes.
- [ ] Repository is fully managed via Jujutsu (`jj`) in colocated mode with a clean `.gitignore`.
- [ ] Roborazzi visual regression tests are established with committed golden baseline PNGs.
- [ ] Modern UI dependencies are updated, local legacy AARs are removed, and visual parity is verified.
