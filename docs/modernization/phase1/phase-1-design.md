# Phase 1: Foundation Modernization, VCS Migration to Jujutsu (jj) + GitHub, and Visual Safety Net

**Date:** 2026-09-07 (Revised 2026-09-14 twice, 2026-09-15 three times, and 2026-09-16 — see Section 0)
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

**2026-09-15 (third pass)** — repo visibility confirmed **public**, plus a working-tree audit that found live-looking secrets:
- **Task 8 gains a mandatory secrets audit and history scrub**, inserted before the GitHub push, not after: a real signing keystore (`common/jay-gindin-android-apps.keystore`, dated 2015) and a likely-live API key (`common/ProjectApiKey.txt`, dated 2012) are sitting in the working tree today, and `hg-fast-export` carries full history forward regardless of current `.gitignore` entries. If either was ever committed, `git-filter-repo` scrubs it before the first push; either way, treat any exposed key as compromised and rotate it.
- **Task 8 also adds a top-level `LICENSE`** (Apache-2.0, confirmed) now that the repo is public.
- **Task 9 gains Gradle Wrapper Validation, a `dependabot.yml` for the version catalog, and branch protection** requiring the CI check to pass before merge.
- **Task 11 gains a step migrating release signing to Google Play App Signing**, removing the checked-in keystore from the repository rather than merely `.gitignore`-ing it going forward.
- **New Task 12: author a repo-root `AGENTS.md`**, added last so it documents the structure Tasks 1–11 actually produce, not the structure planned before they ran. Phase 1 is now 12 tasks.
- **Task 4's Step 6 corrected** to commit the Groovy→Kotlin DSL conversion via `hg move --after` rather than `hg rm` + `hg add`, preserving each build script's Mercurial history instead of making it look newly created — this was going to silently break `git log --follow`/blame on every build script once Task 8's `fast-export` ran.

**2026-09-15 (fourth pass)** — Task 8's secrets-audit step corrected: it was written entirely as `git` commands, which don't work until Step 2's `hg-fast-export` has run, so it wasn't actually runnable as the standalone pre-check the surrounding text described. It now leads with an `hg`-native audit that only needs Mercurial and can be run today, with the `git`-based sweep kept as a secondary cross-check once Step 2 has produced a local git conversion.

**2026-09-15 (fifth pass)** — the `hg`-native audit was actually run against the real repository. Result: `common/jay-gindin-android-apps.keystore`, `common/ProjectApiKey.txt`, and `common/MapQuest.properties.orig` were never committed to Mercurial under any path across the full history — the original "treat as compromised" framing for these no longer applies, and there's nothing to scrub for them. Two other files were found in history instead: `common/AcraApiKey.properties` (2016, self-hosted tool, long unused) and `thirdparty/flurry/ProjectApiKey.txt` (Flurry Analytics, service shut down March 2024) — both low current risk, both still folded into the `git-filter-repo` pass since it's free to include them.

**2026-09-16 (sixth pass) — adversarial review, 7 blocking + 6 non-blocking fixes.** An independent adversarial pass against the live repository (not just internal document review) found real, reproducible defects. Blocking: Task 2's Steps 2/3 read as full-file replacements but the real `strings.xml`/`styles.xml` have real, in-use content — rewritten as explicit insertions. Task 1's file list included 4 files confirmed by grep to contain zero occurrences of the fixture string this task is built around — flagged, with Step 1 rewritten to diagnose rather than assume. Task 8's push command used invalid git syntax (`--all` combined with an explicit branch). Task 8 also had an unresolved branch/bookmark identity problem (`zmanim-v4` is a bookmark, not the Mercurial branch name, which is `default`; six other bookmarks go unmentioned) and a licensing conflict (`LICENSE.md` already declares LGPL-2.1; the plan's Apache-2.0 addition was decided without knowing this) — both now flagged as needing Jay's decision before Task 8 proceeds. `master-roadmap.md` was stale relative to this document's own revision history and has been updated. Non-blocking: Task 4 was missing two real Groovy build files including one non-mechanical closure; Task 5's JUnit5 migration scope was ambiguous about which modules' test files actually change; Task 3's reference-photo set doesn't map 1:1 onto its planned snapshots; three Play Console-dependent steps are now explicitly flagged as human-required; Task 9's branch-protection API call switched to a more reliable JSON payload encoding; the secrets-scrub step now clarifies it doesn't touch the current working-tree `AcraApiKey.properties` symlink.

**2026-09-16 (seventh pass) — pre-execution readiness check, run against the live repository rather than the documents alone.** Result: the plan holds up well. Independently confirmed exactly as documented: Task 1's "191 tests / 52 failures" baseline (ran `./gradlew :ZmanLib:test`), Task 1's 4-file grep list, Task 2's 3-symbol compile failure, the two easy-to-miss `common/*.gradle` files from Task 4, the three never-committed secret-shaped files plus the `AcraApiKey.properties` symlink from Task 8's audit, the LGPL-2.1 text in `LICENSE.md`, and all seven Mercurial bookmarks (`zmanim-v4` currently points at the same revision as the `default` branch tip, so Task 8's branch-identity check should resolve cleanly if run soon — still worth doing explicitly since that alignment can drift). One inaccuracy found and fixed: `strings.xml`'s "33 existing entries" was wrong — `grep -c '<string '` against the live file returns 24; the 33 figure conflated the file's total line count with its entry count. Task 2's Step 2 verification target is corrected accordingly (25 after insertion, not 34). One real, previously-unflagged gap found: the four ZmanLib test files Task 1 lists as "confirmed zero occurrences, don't assume they need the fix" are **not** clean at baseline — they fail too (`UT_TalitTimes` 2, the three `UT_WakeupScheduler_*` 4+6+2 = 14 total), which combined with the 4 confirmed files' 38 failures accounts for the full 52. This doesn't change what Task 1 should do — its Step 1 already says to diagnose these separately rather than assume the same fix applies — but it confirms that diagnosis is actually needed, not a hypothetical hedge; whoever executes Task 1 should expect real, currently-uninvestigated scope there before declaring `:ZmanLib:test` green. Tooling check: `hg`, `jj` 0.45.1, and `hg-fast-export.sh` are present and working; `git-filter-repo` is still not installed (already flagged in Task 8 as needing install first); `gh` CLI was missing at review time and has since been installed and authenticated (account `jgindin`, `repo` scope confirmed via `gh auth status`), closing the one gap that would have affected Task 8 Step 7 and Task 9 Step 5.

**2026-09-21** — after Tasks 1-6 executed, a batch of six chat-directed follow-up items landed ahead of Task 7, including replacing the `apply(from=...)`/`GroovyObject` build-script workaround with a real convention plugin, removing ACRA, and **renaming the `:Android` Gradle module to `:Zmanim`** (structure only — `applicationId`/namespace unchanged). This document's `:Android`/`Android/`-path references from Task 7 onward have been updated to `:Zmanim` to match; the full record of that batch is in `docs/modernization/modernization-roadmap.md`'s 2026-09-21 revision note. Section 3.6 (Task 6, already executed under the old name) is left referring to `:Android`/`Android/build.gradle.kts` as it actually ran.

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
Stage 1D: Version Control Migration from hg to jj, Hosting Move to Public GitHub
  └── Task 8: Push hg tip to sr.ht, audit + scrub secrets, fast-export to Git, initialize colocated jj,
              add LICENSE, push to public GitHub
         │
         ▼
Stage 1E: Continuous Verification
  └── Task 9: Establish CI on GitHub Actions (wrapper validation + detekt/ktlint + Roborazzi + emulator
              smoke matrix + Dependabot + branch protection)
         │
         ▼
Stage 1F: Controlled Library Modernization & Visual Parity Verification (jj + GitHub, CI-protected)
  └── Task 10: Upgrade Material (1.12), ConstraintLayout (2.2), AndroidX Preference; replace MikePenz
               MaterialDrawer/Iconics; verify 100% visual parity with Roborazzi — as five sub-tasks (10.1-10.5)
         │
         ▼
Stage 1G: Release Hardening
  └── Task 11: Modernize and enable Proguard / R8 minification, migrate to Play App Signing,
               verify 16 KB page size compliance
         │
         ▼
Stage 1H: Agent-Handoff Documentation
  └── Task 12: Author AGENTS.md documenting the modernized structure for later phases
```

---

## 3. Detailed Component Specifications

### 3.1. Task 1: Stabilize `ZmanLib` Test Suite
- Realign formatted table test fixtures to match `kosherjava 2.5.0` output without `GR'A Kol Eliyahu`. **Confirmed by grep against the live repo (2026-09-16): only `UT_ShemaTimes.java`, `UT_Zmanim.java`, `UT_ZmanimWithElevation.java`, and `UT_YomTovCandleLighting.java` actually contain the string.** `UT_TalitTimes.java` and the three `UT_WakeupScheduler_*.java` files, listed in earlier drafts, contain zero occurrences — don't apply the same fix there without first confirming (via an actual test run) that they're failing at all, let alone for this reason.
- Verify all tests succeed (`./gradlew :ZmanLib:test`). The "191 tests / 52 failures" figures from earlier drafts were never independently confirmed by actually running the suite — treat them as a rough prior, not a target to hit exactly.

### 3.2. Task 2: Fix `ZehutYosef:sidur` Compilation and Resource Architecture
- Add fallback `about_description_text`, base theme `EliPurple`, and neutral `header.xml` drawable in `ZehutYosef/src/main/res/`. **`strings.xml` (24 existing entries, several in active use — corrected 2026-09-16 by direct `grep -c` against the live file; earlier drafts said 33, which conflated the file's 33 total lines with its actual entry count) and `styles.xml` (defines the module's base `AppTheme`) both already exist — this task inserts new elements into their existing `<resources>` blocks. It does not replace either file's content.**
- Verify both `sidur` and `barMitzvah` compile and assemble cleanly.

### 3.3. Task 3: Establish Roborazzi Screen Snapshot Safety Net (Untouched Baseline)
- Integrate `io.github.takahirom.roborazzi` (1.40+) with Robolectric 4.14+ Native Graphics.
- Snapshot `ZmanimActivity`, `PersonalPreferencesActivity`, and `ZehutYosefActivity` (both flavors).
- Run `./gradlew recordRoborazziDebug`. The recorded PNGs — not the reference phone photos in `docs/modernization/phase1/screenshots/` — are the actual regression target every subsequent task diffs against; the reference photos are a one-time human sanity check on the initial recording only. The reference-photo set (`zmanim.png`, `location.png`, `preferences.png`, `barmitzvah.png`) doesn't map 1:1 onto the planned snapshots — there's no reference photo for the `sidur`-flavor snapshot, and `location.png` has no corresponding snapshot at all. That's expected, not a gap to close.

### 3.4. Task 4: Convert Build Scripts to Kotlin DSL and Adopt a Version Catalog
- Convert `build.gradle` → `build.gradle.kts` and `settings.gradle` → `settings.gradle.kts` for the root project and all four modules (`ZmanUtils`, `ZmanLib`, `Android`, `ZehutYosef`) — **plus `common/gindin-android.gradle` and `common/gindin-versioning.gradle`, two `apply from:`-applied Groovy scripts missed in earlier drafts.** `gindin-versioning.gradle`'s APK-renaming logic (`android.applicationVariants.all { variant -> variant.outputs.all { outputFileName = ... } }`) is a Groovy-closure idiom that doesn't translate mechanically — verify output artifact naming is unchanged after conversion, don't assume it. Also confirm whether `Android/zmanim.gradle`/`ZehutYosef/zehut-yosef.gradle`'s current Groovy try/catch-guarded optional-apply pattern has a Kotlin DSL equivalent in place after conversion.
- Create `gradle/libs.versions.toml` and migrate every inline dependency coordinate (AGP, Kotlin, AndroidX, MikePenz, kosherjava, JUnit, etc.) into it as `[versions]`/`[libraries]`/`[plugins]` entries; reference them from each module's Kotlin DSL script via the generated `libs.` accessor.
- Scaffold a `build-logic` included build with at least one convention plugin (e.g. `zmanim.android-library` or similar) capturing the shared Java 21 toolchain and lint configuration currently duplicated across modules, applied starting with `:ZmanLib` and `:ZmanUtils`.
- This is done before Tasks 5–7 so JUnit5 configuration, SDK alignment, and KMP setup are written directly in idiomatic Kotlin DSL, rather than edited in Groovy now and converted a second time later.
- Verify `./gradlew test assembleDebug verifyRoborazziDebug` still passes after the conversion, with zero behavior change — this task is a mechanical translation for everything except the APK-renaming closure noted above.
- Commit the file renames via `hg move --after`, not `hg rm` + `hg add` — the working tree ends up identical either way, but only `hg move --after` records the pair as a rename, preserving each build script's pre-conversion history so it survives Task 8's `fast-export` into Git.

### 3.5. Task 5: Upgrade Testing Framework to JUnit 5 (Jupiter)
- Add `org.junit.jupiter:junit-jupiter:5.11.4` and `junit-vintage-engine` (via the version catalog) and configure `tasks.withType<Test> { useJUnitPlatform() }` in Kotlin DSL, across all four modules.
- **Migrate test annotations to Jupiter in `:ZmanUtils` and `:ZmanLib` only in Task 5 itself.** `:Android` (`:Zmanim`) and `:ZehutYosef`'s Robolectric-based tests (`ZmanimSnapshotTest.java`/`SiddurSnapshotTest.java`) stay on JUnit4 and keep running via the vintage engine — Robolectric has no official JUnit 5 support. An earlier draft's file list read as covering all four modules, which was ambiguous — it doesn't. **Superseded in part by the 2026-09-21 follow-up batch:** `:Zmanim`/`:ZehutYosef`'s plain-JVM (non-Robolectric) test files were migrated to Jupiter at that point, closing the gap this design left open; the two Robolectric-based files remain on JUnit4 + vintage as described above.

### 3.6. Task 6: Align SDK Baseline Targets Across Android Modules
- **Requires a human:** pull current Android-version distribution for both apps from Play Console before changing `minSdkVersion`, and separately confirm current API-36 compliance status — an autonomous agent has no Play Console credentials and cannot do either step itself.
- Set `minSdkVersion = 24`, `compileSdk = 36`, `targetSdk = 36` in both `Android/build.gradle.kts` and `ZehutYosef/build.gradle.kts` — **36, not 35**, to satisfy Google Play's target API requirement. The primary deadline (August 31, 2026) has already passed as of this plan's writing; any update right now is already relying on the grace extension to November 1, 2026 — confirm actual status in Play Console rather than assuming there's slack left.
- Verify `./gradlew assembleDebug verifyRoborazziDebug`, plus a real-device/emulator smoke check at API 24 and API 36 for edge-to-edge/system-bar overlap that Robolectric's simulated insets don't reliably catch (this step also needs a device/emulator the executing agent may not have access to — flag for human hand-off if so).

### 3.7. Task 7: Configure Kotlin Multiplatform (KMP), Convert `:ZmanLib` to Kotlin, Drop Custom `Pair`, Adopt `kotlinx.serialization`/`kotlinx-datetime`, and Enable Kotlin Interop in App Modules
- Delete `com.gindin.util.Pair` in favor of standard `kotlin.Pair` across all call sites in `:ZmanLib` and `:Zmanim`.
- Configure `:ZmanUtils` as a Kotlin Multiplatform library (`kotlin("multiplatform")` targeting JVM + Android, ready for iOS/Desktop). Apply `explicitApiMode = ExplicitApiMode.Strict` (or `.Warning` initially, tightened to `.Strict` once the surface stabilizes) so its public API is deliberate.
- Convert `:ZmanLib`'s existing Java sources to idiomatic Kotlin, safety-netted by the 191 tests from Task 1. **`:ZmanLib` stays JVM + Android-only in this task** — its dependency on `com.kosherjava:zmanim` (JVM-only Java) blocks a further step to true multiplatform, which is deliberately out of scope here and addressed as a dedicated spike at the start of Phase 6 (see the master roadmap's Phase 6.1). Apply `explicitApiMode` here as well.
- Replace the three current `org.json` usages with `kotlinx.serialization`, and any shared `java.util.Date`/`Calendar` code with `kotlinx-datetime` — both choices are required for eventual iOS compilation and cost little to make now while the surface is small.
- Port `EventBus`, `Event`, `EventHandler`, `EventCacheControl`, and MVP contracts to idiomatic Kotlin with proper null-safety and Java-interop annotations (`@JvmStatic`, `@JvmOverloads`). **This port is deliberately mechanical**: Phase 4a's Task 4a.1 deletes this entire pattern in favor of `kotlin-inject`-based DI + `StateFlow`.
- Define platform-agnostic `TelemetryTracker` interface in `commonMain`. Unlike `EventBus`, this interface is permanent.
- Rewrite `UT_EventBus` in idiomatic Kotlin using **`kotlin-test`** assertions only (not a direct `org.junit.jupiter` dependency in `commonTest` — Jupiter doesn't run on non-JVM KMP targets, and Phase 6 adds an iOS target).
- Apply the `kotlin-android` plugin to `:Zmanim` and `:ZehutYosef` (interop enablement only — do not convert any existing `.java` files in this task; that's deferred to Phase 4a/4b, screen-by-screen).
- Verify `./gradlew test assembleDebug verifyRoborazziDebug` succeeds 100%.

### 3.8. Task 8: Migrate Version Control from `hg` to `jj`, and Move Hosting to Public GitHub
- Confirm tooling: `hg` at `/usr/bin/hg`, `jj` at `~/.local/bin/jj` (add to `PATH` if needed), `hg-fast-export` at `~/src/fast-export`, `git-filter-repo` (install before this task).
- Push the current Mercurial tip to sr.ht as a pre-conversion checkpoint. Bitbucket is stale/non-functional and is not used.
- `fast-export` full Mercurial history into Git, **then verify which branch it actually produced.** The Mercurial named branch is `default`, not `zmanim-v4` — `zmanim-v4` is one of seven bookmarks in this repo, and nothing before this fix confirmed `hg-fast-export`'s git branch actually corresponds to the `zmanim-v4` bookmark's commit rather than an arbitrary HEAD. Match by commit message/author/date against `hg log -r zmanim-v4`, don't assume `git checkout zmanim-v4 || git checkout -b zmanim-v4` lands in the right place. **Separately, decide what happens to the other six bookmarks** (`zmanim-v5`, `zmanim-prev-v5`, `shachrit`, `user-holidays`, `z-35-save-favorites`, `Z-93-new-plag-hanmincha`) before pushing — this needs a decision from Jay, not an assumption. Verify commit-count parity before proceeding, understanding that it checks total counts, not that the right branch tip is correct.
- **Secrets audit — complete, findings below (2026-09-15).** A full-history `hg`-native sweep (`hg log -r "all()"` file-add templates across all 1356+ revisions, plus `hg log --follow`/`hg cat` on specific candidates) confirmed `common/jay-gindin-android-apps.keystore`, `common/ProjectApiKey.txt`, and `common/MapQuest.properties.orig` were **never committed to Mercurial** under any path, despite sitting in the working tree today — nothing to scrub for these. The same sweep found two items that **were** committed: `common/AcraApiKey.properties` (2016, self-hosted tool, long unused) and `thirdparty/flurry/ProjectApiKey.txt` (Flurry Analytics — the service itself was shut down in March 2024). Both are low current risk since neither's underlying service is live, but both get scrubbed with `git-filter-repo` once Task 8's Step 2 has produced the git conversion, since it costs nothing extra to include them (this rewrites history only — it doesn't touch the current, harmless `AcraApiKey.properties` symlink on disk). Re-run the same full-history sweep once more immediately before Task 8's push, in case the working tree has changed since this audit.
- Initialize colocated Jujutsu: `jj git init --colocated` (after the audit/scrub, not before — rewriting history post-`jj init` is messier).
- **Relicensing — requires Jay's explicit confirmation, not a mechanical step:** the repository already has a `LICENSE.md` declaring LGPL-2.1. This plan's Apache-2.0 `LICENSE` addition was decided in an earlier pass without knowing that file existed — it's a real relicensing decision, not a green-field choice. Confirm with Jay before adding the new `LICENSE` and removing/updating `LICENSE.md`; don't leave both present with contradictory terms.
- Convert `.hgignore` to `.gitignore`, archive `.hg/` to `.hg.backup/`.
- Push the converted, colocated history to **https://github.com/jgindin/zmanim**, created **public**, as the new canonical `origin` (check whether it already exists before trying to create it, and that its visibility is actually public if so). Push only the resolved branch by name (`git push github zmanim-v4`) — `git push <remote> <branch> --all` is invalid git syntax and will simply fail. sr.ht may remain as a secondary mirror; Bitbucket is retired.
- Verify `jj status`, `jj log -r '::@'`, and `./gradlew test assembleDebug verifyRoborazziDebug` against the GitHub-hosted repo; spot-check that the `LICENSE` is recognized on the repo page (and that `LICENSE.md` isn't still declaring a different license) and nothing sensitive is visible under `common/`.

### 3.9. Task 9: Establish CI on GitHub Actions
- Workflow runs on every push/PR: `gradle/actions/wrapper-validation@v6` first (catches a tampered wrapper jar before anything else runs — a real supply-chain concern for a public repo), then `./gradlew test verifyRoborazziDebug`, plus `detekt` and `ktlint` checks.
- Second job: instrumented smoke test matrix on emulator images for API 24 and API 36 (e.g. via `reactivecircus/android-emulator-runner`), asserting basic screen-visibility/insets expectations. Fully unattended on GitHub-hosted runners.
- `.github/dependabot.yml` for the `gradle` and `github-actions` ecosystems, weekly, so the versions this phase just pinned in the catalog don't go stale again the way they did before. Dependabot first (zero-config, native); Renovate documented as the fallback if its version-catalog support proves insufficient in practice.
- Branch protection on the push branch requiring the `verify` job to pass before merge — the actual point of building CI is that it can't be silently ignored. Set this up via `gh api ... --input -` with an explicit JSON payload, not `--field`-based nested-array syntax — that encoding has documented reliability problems against this exact endpoint (422s from JSON-schema ambiguity), and the API has also been shifting field names (`contexts` → `checks`); confirm current syntax against `gh api --help`/GitHub's docs at execution time rather than trusting any single example verbatim.
- From this point forward, Tasks 10 and 11 (and every later phase) are protected by CI running on every push.

### 3.10. Task 10: Comprehensive Library Modernization & Visual Parity Verification
Five independently committed and verified sub-tasks:
- **10.1 — Android Module, Preferences:** remove local Xpece AARs; migrate preference XMLs to `androidx.preference.*`.
- **10.2 — Android Module, Core Libraries:** upgrade `material` (1.0 ➔ 1.12.0), `constraintlayout` (1.1.3 ➔ 2.2.0), `appcompat` (1.6.1 ➔ 1.7.0), `play-services-location` (21.0.1 ➔ 21.3.0); remove `androidx.legacy:legacy-support-v4`.
- **10.3 — ZehutYosef Module, Navigation Drawer:** replace `com.mikepenz:materialdrawer:3.0.8` with standard `DrawerLayout` + `NavigationView` — a real UI rewrite, expect the most iteration here.
- **10.4 — ZehutYosef Module, Iconography:** replace `com.mikepenz:iconics-core` with standard Vector Drawables.
- **10.5 — ZehutYosef Module, About Screen:** modernize or replace `aboutlibraries`.
- **Final verification:** `./gradlew verifyRoborazziDebug` against all five combined; reconcile until 100% visual parity with Task 3 baselines.

### 3.11. Task 11: Modernize & Enable Proguard / R8 Minification, Migrate to Play App Signing, App Bundle Packaging, and Release-Readiness Checks
- Remove obsolete pre-AndroidX Proguard files. Configure modern R8 rules protecting KosherJava/Kotlin `:ZmanLib` classes, the Kotlin `EventBus` classes from Task 7, JSON/serialization models, and view/ViewModel components.
- **Migrate release signing to Google Play App Signing** — **requires a human for the Play Console enrollment step**, same as Task 6's Play Console dependencies. Task 8's audit confirmed the keystore was never actually committed to Mercurial history, so there's no history to scrub for it, but it's still one accidental `add` away from becoming exactly that as long as it lives in `common/`. Enroll both apps if not already enrolled, move the release keystore out of the repository to a local gitignored path or secrets manager, and point `signingConfigs.release` at gitignored properties/environment variables instead of a hardcoded path. Once confirmed working, delete `common/jay-gindin-android-apps.keystore` from the working tree — the point is that it no longer lives in the repo at all, not merely that it's `.gitignore`d going forward.
- Enable `minifyEnabled true` in `buildTypes.release` for `:Zmanim` and `:ZehutYosef`. Verify `./gradlew assembleRelease bundleRelease`.
- **Verify 16 KB memory page size compliance**: this app has no native (`.so`) dependencies today and Java/Kotlin-only apps are already compatible, but confirm via APK Analyzer that no `.so` files have been introduced by any Task 10 library upgrade, given Google Play's compliance deadline of February 1, 2027 for apps targeting API 35+ (which Task 6 now does). Cheap to check now; expensive to discover at release time in 2027.
- Manually install and smoke-test the minified release build on a real device or emulator (R8 is the one change in this phase that can pass every automated check and still crash at runtime via a missing `-keep` rule surfacing only through reflection).

### 3.12. Task 12: Author `AGENTS.md` for Agent-Directed Development
- Added last, deliberately, so it documents what Tasks 1–11 actually produced rather than what was planned before they ran.
- Document the module map (`:ZmanUtils`, `:ZmanLib`, `:Zmanim`, `:ZehutYosef` and its flavors) with purpose, current language, and the exact build/test command for each in isolation, plus the full-repo verification command and the one real-device exception (Task 6/Task 9).
- Document where `detekt`/`ktlint` config lives and the exact local command to run both, so an agent runs them before committing rather than discovering a red CI check afterward.
- Document version-control conventions: colocated `jj` over Git, public GitHub hosting, `jj commit` for day-to-day work, plain descriptive commit messages, and — since the repo is public — what to do if a credential is ever committed by mistake (treat it as compromised, don't just delete it in a follow-up commit).
- Document how this plan, its design spec, and the master roadmap relate, so an agent picking up Phase 2 knows where to look for direction versus task-level detail.
- Verify the document by having a fresh read (ideally a new agent session) answer a few concrete questions using only `AGENTS.md` — if an answer requires guessing or searching the repo, revise it.

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
- [ ] `:Zmanim` and `:ZehutYosef` have the `kotlin-android` interop plugin applied (no mass file conversion in Phase 1).
- [ ] All Android modules build with `minSdkVersion 24` and `compileSdk`/`targetSdk` **36**, verified against both Roborazzi and a real device/emulator check at API 24 and API 36, and against current Play Console install-base data.
- [ ] Repository history re-swept for secrets immediately before the public push (Task 8, Step 4b), confirming no new finding since the 2026-09-15 audit; `common/AcraApiKey.properties` and `thirdparty/flurry/ProjectApiKey.txt` scrubbed via `git-filter-repo`.
- [ ] All build-script renames from Task 4 preserve file history via `hg move --after` — confirmed with `hg log --follow` (pre-migration) or `git log --follow` (post-migration) on at least one converted script.
- [ ] Repository is fully migrated to colocated Jujutsu (`jj`), with commit-count parity confirmed against the original Mercurial history, hosted canonically at `github.com/jgindin/zmanim`, **public**, with the branch confirmed to correspond to the `zmanim-v4` bookmark's actual commit (not an arbitrary HEAD) and a decision made and recorded about the other six Mercurial bookmarks.
- [ ] Relicensing confirmed with Jay before Task 8 ran: the repo has a single, unambiguous top-level `LICENSE` (Apache-2.0) with the pre-existing `LICENSE.md` (LGPL-2.1) removed or reconciled, not left in place with contradictory terms.
- [ ] CI is live on GitHub Actions: Gradle Wrapper validation, unit tests, static analysis, Roborazzi verification, and an emulator smoke matrix on every push/PR; Dependabot is configured for the Gradle version catalog and GitHub Actions; branch protection requires the CI check to pass before merge.
- [ ] Local legacy AARs and obsolete MikePenz libraries are completely retired, landed as five independently verified sub-tasks.
- [ ] Release builds assemble with R8 minification enabled and generate verified `.aab` bundles, with 16 KB page size compliance confirmed, signed via Google Play App Signing with no keystore file remaining in the repository.
- [ ] `AGENTS.md` exists at the repo root and correctly answers, without guessing, how to build/test each module in isolation, how to run static analysis locally, and what version-control tool and host the repo uses.
