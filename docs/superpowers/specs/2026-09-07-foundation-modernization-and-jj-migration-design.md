# Phase 1: Foundation Modernization, VCS Migration to Jujutsu (jj) + GitHub, and Visual Safety Net

**Date:** 2026-09-07 (Revised 2026-09-14, twice — see Section 0)
**Status:** Approved
**Scope:** `zmanim` repository (`:ZmanUtils`, `:ZmanLib`, `:Android`, `:ZehutYosef`) and preparation for `zehut-yosef-text` integration

---

## 0. Revision Notes

**2026-09-14 (first pass)** — feasibility review; real tool paths (`hg` at `/usr/bin/hg`, `jj` at `~/.local/bin/jj`, `hg-fast-export` at `~/src/fast-export`), sr.ht as pre-migration checkpoint with Bitbucket dropped as stale, GitHub hosting added with a CI task immediately after, library modernization split into five sub-tasks, a real-device check added to the SDK-alignment task, the `EventBus` port explicitly framed as a stepping stone, and a Kotlin-interop-enablement step for the app modules.

**2026-09-14 (second pass)** — Kotlin/Android/KMP/Play Store best-practices review:
- **Compliance-critical correction:** the SDK-alignment task now targets **API 36** (Android 16), not API 35 — Google Play requires this for app updates effective August 31, 2026, with a grace extension available to November 1, 2026.
- **New Task 4: convert every build script to Kotlin DSL and adopt a Gradle version catalog**, inserted before the JUnit5/SDK/KMP tasks so they're written directly in Kotlin DSL rather than edited in Groovy and converted later. All subsequent tasks renumbered (was 9 tasks, now 11).
- **KMP task (now Task 7) expanded:** `:ZmanLib` converts to idiomatic Kotlin alongside `:ZmanUtils` (test-netted by Task 1's 191 tests) — but stays JVM/Android-only in this phase, since true multiplatform depends on a calculation-engine question resolved in Phase 6, not Phase 1. `explicitApiMode` applied to both KMP-facing library modules. `kotlinx.serialization`/`kotlinx-datetime` replace the last `org.json`/JVM-date usage in shared code.
- **CI task (now Task 9) gains `detekt`/`ktlint`** alongside unit tests and Roborazzi.
- **R8/AAB task (now Task 11) gains a 16 KB memory page size compliance check** (Play deadline February 1, 2027 — this app has no native dependencies today and should stay that way through Task 11's release-readiness checklist).

---

## 1. Executive Summary & Context

The `zmanim` codebase is a multi-module Android project built around Jewish calendar and prayer calculations. The project has recently had Gradle upgraded to 9.6.0, the Android Gradle Plugin (AGP) upgraded to 9.4.0, and Java 21 toolchains configured. However, several technical debts and breakages exist before major feature work (such as the ZehutYosef daily prayer expansion and location provider overhaul) can proceed:

1. **`ZmanLib` Test Failures:** 52 tests fail because commit 1350 removed the deprecated `GR'A Kol Eliyahu` zman from `SofZmanShema.java` without updating the golden string test fixtures.
2. **`ZehutYosef:sidur` Compilation Failure:** The `sidur` product flavor fails compilation because `About.java` and `ZehutYosefActivity.java` reference strings, themes (`EliPurple`), and drawables (`header`) that exist only in `src/barMitzvah/res/`.
3. **Outdated Testing Framework:** Unit tests still run on JUnit 4 (4.13.2) rather than modern JUnit 5 (Jupiter).
4. **Outdated SDK Targets:** `:Android` specifies `minSdkVersion 14` (Ice Cream Sandwich, 2011), necessitating legacy local AAR workarounds (`support-preference-1.3.1.aar`), while `:ZehutYosef` specifies `minSdkVersion 22`. Both specify `compileSdk 33` — well behind Google Play's current API 36 target requirement.
5. **No Visual Regression Safety Net:** UI dependencies (Material Components 1.0.0 from 2018, ConstraintLayout 1.1.3, local Xpece preference AARs, MikePenz MaterialDrawer from 2015) are severely outdated, but cannot be safely upgraded without visual regression tests verifying that screens render without layout or styling regressions.
6. **No Kotlin / KMP in Base Modules:** Base utilities still use legacy Java constructs including a bespoke `com.gindin.util.Pair` class, and every build script is Groovy with inline dependency coordinates rather than a version catalog.
7. **VCS Legacy:** The repository is currently managed with Mercurial (`hg`), whereas the desired workflow is Jujutsu (`jj`), a modern Git-compatible VCS, hosted on GitHub going forward.
8. **Disabled Minification / Outdated Proguard Rules:** Both applications set `minifyEnabled false` with obsolete pre-AndroidX Proguard files.
9. **No CI, No Static Analysis:** All verification (tests, Roborazzi) is currently invoked manually via Gradle, and nothing enforces Kotlin code quality as the codebase becomes majority-Kotlin over the coming phases.

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
Stage 1B: Build System Modernization (Mercurial)
  └── Task 4: Convert build scripts to Kotlin DSL, adopt a version catalog and convention plugins
         │
         ▼
Stage 1C: Toolchain Modernization & KMP Preparation (Mercurial)
  ├── Task 5: Upgrade test suite to JUnit 5 (Jupiter 5.11+)
  ├── Task 6: Align Android SDK baselines (minSdkVersion 24, compileSdk/targetSdk 36) + real-device check
  └── Task 7: Configure KMP, convert :ZmanUtils and :ZmanLib to Kotlin, adopt kotlinx.serialization/datetime,
              enable Kotlin interop in app modules
         │
         ▼
Stage 1D: Version Control Migration from hg to jj, Hosting Move to GitHub
  └── Task 8: Push hg tip to sr.ht, fast-export to Git, initialize colocated jj, push to GitHub
         │
         ▼
Stage 1E: Continuous Verification
  └── Task 9: Establish CI on GitHub Actions (unit tests + detekt/ktlint + Roborazzi + emulator smoke matrix)
         │
         ▼
Stage 1F: Controlled Library Modernization & Visual Parity Verification (jj + GitHub, CI-protected)
  └── Task 10: Upgrade Material (1.12), ConstraintLayout (2.2), AndroidX Preference; replace MikePenz
               MaterialDrawer/Iconics; verify 100% visual parity with Roborazzi — as five sub-tasks (10.1-10.5)
         │
         ▼
Stage 1G: Release Hardening
  └── Task 11: Modernize and enable Proguard / R8 minification, verify 16 KB page size compliance
```

---

## 3. Detailed Component Specifications

### 3.1. Task 1: Stabilize `ZmanLib` Test Suite
- Realign formatted table test fixtures in `UT_ShemaTimes.java`, `UT_TalitTimes.java`, `UT_Zmanim.java`, `UT_ZmanimWithElevation.java`, `UT_WakeupScheduler_*.java`, and `UT_YomTovCandleLighting.java` to match `kosherjava 2.5.0` output without `GR'A Kol Eliyahu`.
- Verify all 191 unit tests succeed (`./gradlew :ZmanLib:test`).

### 3.2. Task 2: Fix `ZehutYosef:sidur` Compilation and Resource Architecture
- Add fallback `about_description_text`, base theme `EliPurple`, and neutral `header.xml` drawable in `ZehutYosef/src/main/res/`.
- Verify both `sidur` and `barMitzvah` compile and assemble cleanly.

### 3.3. Task 3: Establish Roborazzi Screen Snapshot Safety Net (Untouched Baseline)
- Integrate `io.github.takahirom.roborazzi` (1.40+) with Robolectric 4.14+ Native Graphics.
- Snapshot `ZmanimActivity`, `PersonalPreferencesActivity`, and `ZehutYosefActivity` (both flavors).
- Run `./gradlew recordRoborazziDebug`. The recorded PNGs — not the reference phone photos in `docs/superpowers/specs/screenshots/` — are the actual regression target every subsequent task diffs against; the reference photos are a one-time human sanity check on the initial recording only.

### 3.4. Task 4: Convert Build Scripts to Kotlin DSL and Adopt a Version Catalog
- Convert `build.gradle` → `build.gradle.kts` and `settings.gradle` → `settings.gradle.kts` for the root project and all four modules (`ZmanUtils`, `ZmanLib`, `Android`, `ZehutYosef`).
- Create `gradle/libs.versions.toml` and migrate every inline dependency coordinate (AGP, Kotlin, AndroidX, MikePenz, kosherjava, JUnit, etc.) into it as `[versions]`/`[libraries]`/`[plugins]` entries; reference them from each module's Kotlin DSL script via the generated `libs.` accessor.
- Scaffold a `build-logic` included build with at least one convention plugin (e.g. `zmanim.android-library` or similar) capturing the shared Java 21 toolchain and lint configuration currently duplicated across modules, applied starting with `:ZmanLib` and `:ZmanUtils`.
- This is done before Tasks 5–7 so JUnit5 configuration, SDK alignment, and KMP setup are written directly in idiomatic Kotlin DSL, rather than edited in Groovy now and converted a second time later.
- Verify `./gradlew test assembleDebug verifyRoborazziDebug` still passes after the conversion, with zero behavior change — this task is a mechanical translation, not an opportunity to also change build logic.

### 3.5. Task 5: Upgrade Testing Framework to JUnit 5 (Jupiter)
- Add `org.junit.jupiter:junit-jupiter:5.11.4` (via the version catalog) and configure `tasks.withType<Test> { useJUnitPlatform() }` in Kotlin DSL.
- Migrate test suites across `:ZmanUtils`, `:ZmanLib`, and Android unit tests from JUnit 4 to JUnit 5 Jupiter annotations and assertions.

### 3.6. Task 6: Align SDK Baseline Targets Across Android Modules
- Pull current Android-version distribution for both apps from Play Console before changing `minSdkVersion`.
- Set `minSdkVersion = 24`, `compileSdk = 36`, `targetSdk = 36` in both `Android/build.gradle.kts` and `ZehutYosef/build.gradle.kts` — **36, not 35**, to satisfy Google Play's target API requirement effective August 31, 2026 (grace extension available to November 1, 2026 if genuinely needed; check Play Console independently, since that deadline doesn't wait for this phase to finish).
- Verify `./gradlew assembleDebug verifyRoborazziDebug`, plus a real-device/emulator smoke check at API 24 and API 36 for edge-to-edge/system-bar overlap that Robolectric's simulated insets don't reliably catch.

### 3.7. Task 7: Configure Kotlin Multiplatform (KMP), Convert `:ZmanLib` to Kotlin, Drop Custom `Pair`, Adopt `kotlinx.serialization`/`kotlinx-datetime`, and Enable Kotlin Interop in App Modules
- Delete `com.gindin.util.Pair` in favor of standard `kotlin.Pair` across all call sites in `:ZmanLib` and `:Android`.
- Configure `:ZmanUtils` as a Kotlin Multiplatform library (`kotlin("multiplatform")` targeting JVM + Android, ready for iOS/Desktop). Apply `explicitApiMode = ExplicitApiMode.Strict` (or `.Warning` initially, tightened to `.Strict` once the surface stabilizes) so its public API is deliberate.
- Convert `:ZmanLib`'s existing Java sources to idiomatic Kotlin, safety-netted by the 191 tests from Task 1. **`:ZmanLib` stays JVM + Android-only in this task** — its dependency on `com.kosherjava:zmanim` (JVM-only Java) blocks a further step to true multiplatform, which is deliberately out of scope here and addressed as a dedicated spike at the start of Phase 6 (see the master roadmap's Phase 6.1). Apply `explicitApiMode` here as well.
- Replace the three current `org.json` usages with `kotlinx.serialization`, and any shared `java.util.Date`/`Calendar` code with `kotlinx-datetime` — both choices are required for eventual iOS compilation and cost little to make now while the surface is small.
- Port `EventBus`, `Event`, `EventHandler`, `EventCacheControl`, and MVP contracts to idiomatic Kotlin with proper null-safety and Java-interop annotations (`@JvmStatic`, `@JvmOverloads`). **This port is deliberately mechanical**: Phase 4a's Task 4a.1 deletes this entire pattern in favor of `kotlin-inject`-based DI + `StateFlow`.
- Define platform-agnostic `TelemetryTracker` interface in `commonMain`. Unlike `EventBus`, this interface is permanent.
- Rewrite `UT_EventBus` in idiomatic Kotlin using **`kotlin-test`** assertions only (not a direct `org.junit.jupiter` dependency in `commonTest` — Jupiter doesn't run on non-JVM KMP targets, and Phase 6 adds an iOS target).
- Apply the `kotlin-android` plugin to `:Android` and `:ZehutYosef` (interop enablement only — do not convert any existing `.java` files in this task; that's deferred to Phase 4a/4b, screen-by-screen).
- Verify `./gradlew test assembleDebug verifyRoborazziDebug` succeeds 100%.

### 3.8. Task 8: Migrate Version Control from `hg` to `jj`, and Move Hosting to GitHub
- Confirm tooling: `hg` at `/usr/bin/hg`, `jj` at `~/.local/bin/jj` (add to `PATH` if needed), `hg-fast-export` at `~/src/fast-export`.
- Push the current Mercurial tip to sr.ht as a pre-conversion checkpoint. Bitbucket is stale/non-functional and is not used.
- `fast-export` full Mercurial history into Git; verify commit-count parity before proceeding.
- Initialize colocated Jujutsu: `jj git init --colocated`.
- Convert `.hgignore` to `.gitignore`, archive `.hg/` to `.hg.backup/`.
- Push the converted, colocated history to **https://github.com/jgindin/zmanim** as the new canonical `origin` (check whether it already exists before trying to create it). sr.ht may remain as a secondary mirror; Bitbucket is retired.
- Verify `jj status`, `jj log -r '::@'`, and `./gradlew test assembleDebug verifyRoborazziDebug` against the GitHub-hosted repo.

### 3.9. Task 9: Establish CI on GitHub Actions
- Workflow runs on every push/PR: `./gradlew test verifyRoborazziDebug`, plus `detekt` and `ktlint` checks.
- Second job: instrumented smoke test matrix on emulator images for API 24 and API 36 (e.g. via `reactivecircus/android-emulator-runner`), asserting basic screen-visibility/insets expectations. Fully unattended on GitHub-hosted runners.
- From this point forward, Tasks 10 and 11 (and every later phase) are protected by CI running on every push.

### 3.10. Task 10: Comprehensive Library Modernization & Visual Parity Verification
Five independently committed and verified sub-tasks:
- **10.1 — Android Module, Preferences:** remove local Xpece AARs; migrate preference XMLs to `androidx.preference.*`.
- **10.2 — Android Module, Core Libraries:** upgrade `material` (1.0 ➔ 1.12.0), `constraintlayout` (1.1.3 ➔ 2.2.0), `appcompat` (1.6.1 ➔ 1.7.0), `play-services-location` (21.0.1 ➔ 21.3.0); remove `androidx.legacy:legacy-support-v4`.
- **10.3 — ZehutYosef Module, Navigation Drawer:** replace `com.mikepenz:materialdrawer:3.0.8` with standard `DrawerLayout` + `NavigationView` — a real UI rewrite, expect the most iteration here.
- **10.4 — ZehutYosef Module, Iconography:** replace `com.mikepenz:iconics-core` with standard Vector Drawables.
- **10.5 — ZehutYosef Module, About Screen:** modernize or replace `aboutlibraries`.
- **Final verification:** `./gradlew verifyRoborazziDebug` against all five combined; reconcile until 100% visual parity with Task 3 baselines.

### 3.11. Task 11: Modernize & Enable Proguard / R8 Minification, App Bundle Packaging, and Release-Readiness Checks
- Remove obsolete pre-AndroidX Proguard files. Configure modern R8 rules protecting KosherJava/Kotlin `:ZmanLib` classes, the Kotlin `EventBus` classes from Task 7, JSON/serialization models, and view/ViewModel components.
- Enable `minifyEnabled true` in `buildTypes.release` for `:Android` and `:ZehutYosef`. Verify `./gradlew assembleRelease bundleRelease`.
- **Verify 16 KB memory page size compliance**: this app has no native (`.so`) dependencies today and Java/Kotlin-only apps are already compatible, but confirm via APK Analyzer that no `.so` files have been introduced by any Task 10 library upgrade, given Google Play's compliance deadline of February 1, 2027 for apps targeting API 35+ (which Task 6 now does). Cheap to check now; expensive to discover at release time in 2027.
- Manually install and smoke-test the minified release build on a real device or emulator (R8 is the one change in this phase that can pass every automated check and still crash at runtime via a missing `-keep` rule surfacing only through reflection).

---

## 4. Success Criteria & Deliverables
- [ ] `hg`, `jj`, and `hg-fast-export` tooling confirmed present and on `PATH` before Task 8 begins.
- [ ] Current Mercurial history pushed to sr.ht as a pre-conversion checkpoint.
- [ ] All build scripts are Kotlin DSL, all dependency versions live in `gradle/libs.versions.toml`, and at least one convention plugin is in use via a `build-logic` included build.
- [ ] Roborazzi visual regression tests pass with 0 regressions against recorded baselines.
- [ ] `detekt` and `ktlint` run clean in CI with zero suppressed violations at baseline.
- [ ] Unit tests pass 100% on JUnit 5 across all modules.
- [ ] `:ZmanUtils` and `:ZmanLib` are idiomatic Kotlin with `explicitApiMode` applied and no custom `Pair`; `:ZmanUtils` is a real Kotlin Multiplatform library including `TelemetryTracker`, using `kotlin-test` (not a direct JUnit 5 dependency) in `commonTest`; `:ZmanLib` is Kotlin but intentionally still JVM/Android-only pending the Phase 6 calculation-engine spike.
- [ ] The three remaining `org.json` usages are replaced with `kotlinx.serialization`; any shared date/time code uses `kotlinx-datetime`.
- [ ] `:Android` and `:ZehutYosef` have the `kotlin-android` interop plugin applied (no mass file conversion in Phase 1).
- [ ] All Android modules build with `minSdkVersion 24` and `compileSdk`/`targetSdk` **36**, verified against both Roborazzi and a real device/emulator check at API 24 and API 36, and against current Play Console install-base data.
- [ ] Repository is fully migrated to colocated Jujutsu (`jj`), with commit-count parity confirmed against the original Mercurial history, hosted canonically at `github.com/jgindin/zmanim`.
- [ ] CI is live on GitHub Actions: unit tests, static analysis, Roborazzi verification, and an emulator smoke matrix on every push/PR.
- [ ] Local legacy AARs and obsolete MikePenz libraries are completely retired, landed as five independently verified sub-tasks.
- [ ] Release builds assemble with R8 minification enabled and generate verified `.aab` bundles, with 16 KB page size compliance confirmed.
