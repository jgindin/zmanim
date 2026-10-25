# AGENTS.md

Read this before changing anything. It is for AI coding agents (Claude Code, Gemini CLI, Codex and
similar) and for people. Everything here was checked against the repository when it was written; if
something below disagrees with the code, trust the code and fix this file in the same change.

## What this is

Two published Android apps in one repository (`github.com/jgindin/zmanim`, public, LGPL-2.1):

| App | applicationId | Notes |
|---|---|---|
| **Zmanim** (`:Zmanim`) | `com.gindin.zmanim.android` | Daily zmanim (halachic times), calendar, alerts. Uses the KosherJava `zmanim` library. |
| **Zehut Yosef** (`:ZehutYosef`) | `com.gindin.barmitzvah` (flavor `barMitzvah`) and `com.gindin.zehutyosef` (flavor `sidur`) | A siddur (prayer book) reader. |

Both are published on Google Play with **real users and real saved data**. Treat every change as one
that ships to them. `minSdk` is 24, `compileSdk`/`targetSdk` are 36, and the build uses Java 21.

## Modules

| Module | What | Language |
|---|---|---|
| `:ZmanUtils` | Event bus, MVP base types, `TelemetryTracker`. | Kotlin Multiplatform (`commonMain`, `jvmCommonMain`, `androidMain` via the AGP KMP library plugin) |
| `:ZmanLib` | The domain: Hebrew calendar, holidays, zmanim, locations, notification scheduling. Wraps KosherJava. | Kotlin (JVM/Android), `explicitApi()` |
| `:Zmanim` | The Zmanim app UI and services. | Java (Kotlin interop is on; a few Kotlin files) |
| `:ZehutYosef` | The siddur app UI. | Java |
| `build-logic/` | An included build with the convention plugins and custom Gradle tasks. | Kotlin |

`:ZmanLib` and `:ZmanUtils` are called from the Java apps: **their Java-visible API (names,
signatures, `@JvmStatic`/`@JvmField`/`const`, nullability, `@Throws`, visibility) must not change
without fixing every Java caller.** Kotlin file and class names are visible to Java too. Roadmap
phases 4a/4b will convert the apps to Kotlin and Compose screen by screen; do not convert Java
files opportunistically.

## Build, test and run

Always use the Gradle wrapper from the repository root (`./gradlew`). Versions live in
`gradle/libs.versions.toml`; do not hardcode them in build scripts.

```bash
./gradlew test assembleDebug verifyRoborazziDebug   # unit tests + screenshot verification + debug build
./gradlew :ZmanLib:test                              # just the domain tests
./gradlew :ZmanUtils:jvmTest                         # just ZmanUtils' tests (`test` also works)
./gradlew :Zmanim:testDebugUnitTest                  # Zmanim's JVM tests (Robolectric)
./gradlew :Zmanim:installDebug                       # install on the connected device/emulator
./gradlew :ZehutYosef:installBarMitzvahDebug         # (or installSidurDebug)
```

Release variants are minified with R8 (see "Release builds"):
`:Zmanim:assembleRelease`, `:Zmanim:bundleRelease`, `:ZehutYosef:bundleBarMitzvahRelease`,
`:ZehutYosef:bundleSidurRelease`, `:Zmanim:installRelease`, `:ZehutYosef:installBarMitzvahRelease`.

The build needs a `local.properties` with `sdk.dir=` (not committed). The Gradle daemon runs with a
4 GB heap (`gradle.properties`); stop it when you are done on a small machine: `./gradlew --stop`.

## Code quality gates (CI enforces all of these)

```bash
./gradlew ktfmtCheck detekt     # formatting + static analysis; run before every commit
./gradlew ktfmtFormat           # fix formatting
./gradlew checkR8KeptNames      # the R8 keep-rule guard (builds the minified release variants)
```

- **Formatting: ktfmt**, Google style, **100 columns**, for all Kotlin and `.kts` (including
  `build-logic`). Long `//` comments are not re-wrapped by ktfmt: wrap them by hand. Java is not
  covered by ktfmt: match the neighboring Java (2-space indent, spaces inside parentheses,
  `Copyright (c) <year>. Jay R. Gindin` header on every file).
- **Static analysis: detekt** (`dev.detekt` 2.0.0-alpha series; it is pre-release because it is the
  one that supports the Kotlin 2.4 compiler). Config: `config/detekt/detekt.yml` (overrides only,
  each with a reason). **There is no baseline: fix findings, don't suppress them.** A narrow
  `@Suppress` with a reason comment is acceptable only where the rule conflicts with the
  Java-visible API or the Gradle/AGP API shape.
- Comments explain *why the code is the way it is*, in the present tense. Do not write comments that
  refer to how the code used to be ("previously", "originally", "was moved from") or to plan/task
  numbers; they rot.
- `.git-blame-ignore-revs` lists the formatting-only commits so `git blame` skips them (run
  `git config blame.ignoreRevsFile .git-blame-ignore-revs` once locally; GitHub does it by itself).
  If you land another repository-wide formatting commit, add its id *as it appears on `main`*
  (merging rewrites ids) in a follow-up.

## Tests

| Layer | Where | How to run |
|---|---|---|
| Domain tests (JUnit 5), incl. a golden table of every zman's time | `ZmanLib/src/test` (`golden/`, `upgrade/`) | `:ZmanLib:test` |
| ZmanUtils tests (kotlin-test) | `ZmanUtils/src/jvmTest` | `:ZmanUtils:jvmTest` |
| Robolectric tests (JUnit 4 via the vintage engine), incl. notification pipeline, permission logic, saved-state upgrade tests | `Zmanim/src/test`, `ZehutYosef/src/test` | `test` |
| Roborazzi screenshots (goldens in `Zmanim/screenshots`, `ZehutYosef/screenshots`) | the `*SnapshotTest` classes | `verifyRoborazziDebug`; `recordRoborazziDebug` only when a visual change is intended, and say so in the PR |
| On-device (emulator) tests | `Zmanim/src/androidTest`, `ZehutYosef/src/androidTest` | `:Zmanim:connectedDebugAndroidTest`, `:ZehutYosef:connectedBarMitzvahDebugAndroidTest`, `...SidurDebugAndroidTest` |
| The same on-device tests against the **R8-minified** build | same sources, build type `releaseCheck` | `./gradlew -Pzmanim.testBuildType=releaseCheck :Zmanim:connectedReleaseCheckAndroidTest ...` (see `.github/workflows/ci.yml`) |

Conventions and hard-won rules:

- Test classes are named `UT_*` (JVM) and `*Test` (on-device/snapshot). Write the test first when
  fixing a bug, and **mutation-check** every test you add: break the code under test once, see the
  test fail, restore.
- **Fixtures must be synthetic and come from the released code**, never from a real device or a
  real person: the saved-state fixtures under `src/test/resources/upgrade/` and the zman golden
  table were produced by running the released Java implementation (the READMEs next to them say how)
  on invented inputs. Never commit real locations, keys or user data.
- Robolectric does not simulate real window insets or real window-manager behavior, and its
  `Calendar.getInstance()` clock cannot be pinned. The edge-to-edge/ActionBar layout is verified only
  by the on-device tests (`WindowInsetsLayoutTest`).
- Beware process-wide statics in tests (for example `LocationToAddressConverter.wipeCacheOnStart`): reset them in setup, or tests become order-dependent.
- The Android Gradle plugin writes a test that skips itself with a JUnit `Assume` as a `<failure>`
  holding an `AssumptionViolatedException` in the result XML. `.github/scripts/check-instrumented-results.py`
  treats those as skips; keep it that way.
- On-device tests that wipe app state use the emulator-only guard in `AppState` and skip on physical
  devices (unless `-Pandroid.testInstrumentationRunnerArguments.allowPhysicalDevice=true`). Never
  run them against a phone with the real app installed.
- System permission dialogs: a UiAutomator tap can be dropped when the dialog's window is not
  taking input yet. Click, wait for the button to go away, click again. Poll with generous timeouts;
  CI emulators are slow.

## Release builds, versions and signing

- **Version counters**: `Zmanim/version.properties` and `ZehutYosef/version.properties` hold
  `AI_VERSION_CODE`; the build uses that **plus one** as `versionCode` and rewrites the file when an
  `assemble*`/`install*`/`bundle*` task for a variant runs (test/check tasks do not). Each Play upload
  must have a higher `versionCode` than the last released one (Zmanim: 409, ZehutYosef/BarMitzvah:
  77 at the time of writing; the counters in the repo are one *below* the next build). **Do not commit
  incidental bumps**: `jj restore Zmanim/version.properties ZehutYosef/version.properties` after a
  local release build, unless you are deliberately recording a release.
- **Signing**: release builds are signed with the Play *upload* key. Credentials are never in the
  repository: Gradle properties `gindin.signing.storeFile`, `gindin.signing.storePassword`,
  `gindin.signing.keyPassword` in `~/.gradle/gradle.properties` (aliases: `android-app` for Zmanim,
  `zehutyosef` for ZehutYosef). Without them the release build is simply unsigned, so debug builds,
  tests and CI work. See `build-logic/.../Signing.kt`. Play App Signing re-signs what users get.
- **R8** (minification + resource shrinking) is on for release builds. Keep rules are narrow and
  justified in `Zmanim/proguard-rules.pro` and `ZehutYosef/proguard-rules.pro`. The ones that matter
  most: `NotifiedZmanim` (Java-serialized to disk; class name, fields and `serialVersionUID` must not
  change) and `Zmanim$Type` (stored in that serialized set and used by name for preference keys).
  **`r8-kept-names.txt` in each app lists every name that must survive**, tied to its rule;
  `checkR8KeptNames` fails CI if R8 renames or drops one. If you add a keep rule or code that is
  reached by name (reflection, serialization, `valueOf` on stored strings), add the name to that file.
  `releaseCheck` is a debug-signed, minified build type used to run the on-device tests against R8
  output.
- Saved-data compatibility is a hard requirement: the saved location files, `NotifiedZmanim` files,
  SharedPreferences keys (they use `Enum.name()` / `Zmanim.Type.displayName`) and the location JSON
  format must keep loading across upgrades. The upgrade tests exist to enforce it.

## Version control and hosting

- Colocated **Jujutsu (`jj`) over Git**; the remote is `github.com/jgindin/zmanim`. Commit with
  `jj commit -m ...`, not `git commit`. Messages are a plain complete sentence on what changed and
  why. Use a heredoc or single quotes for messages containing backticks (double quotes make the
  shell eat them). Agents add the `Co-Authored-By:` trailer they were told to use.
- **Work on a bookmark, open a PR, and merge with "Rebase and merge" only.** Never squash and never
  create merge commits: the Java-to-Kotlin conversions are stored as a rename-only commit followed by
  a content commit so history follows the files, and squashing flattens that. `main` is protected:
  a PR is required (no approvals needed) and the `verify` check must pass on an up-to-date branch.
  (Repository settings still allow squash and merge commits; do not use them.)
- Typical flow:
  ```bash
  jj new main                              # start from the current main
  # ... edit, test ...
  jj commit -m 'Plain sentence about what and why'
  jj bookmark create my-change -r @-
  jj git push --bookmark my-change
  gh pr create --head my-change --base main --fill   # --head is needed: jj leaves git HEAD detached
  # after the PR is merged on GitHub:
  jj git fetch                              # jj recognizes the rebase-merged commits by itself
  ```
  Moving a bookmark to a rewritten commit makes `jj git push` a force-update ("move sideways"); that
  is normal after `jj rebase` and is only done by the owner of the PR.
- When `main` has moved and your branch is "out-of-date": `jj git fetch`, `jj rebase -b my-change -d main`, push.
- Optional alias the maintainer uses (in `~/.config/jj/config.toml`, not in the repo):
  `jj landed <bookmark>` checks via `gh` that the bookmark's PR is merged, fetches, abandons the
  branch's leftover commits and deletes the bookmark.
- **Never commit credentials, keystores, certificates or API keys.** `.gitignore` covers
  `*.keystore`, `*.jks`, `*.p12`, `common/*upload_cert.der` and the like. If a secret is committed
  by mistake, stop and treat it as compromised; deleting it in a follow-up commit is not enough
  because the repository is public.
- `.hg.backup/` is a local-only archive of the pre-migration Mercurial repository (ignored by git);
  ignore it.
- Dependabot opens PRs weekly (Gradle and the workflow actions). Check CI before merging, and weigh
  each against upcoming work: libraries that the Compose rewrites replace (the View-based UI
  libraries: Material 1.0, ConstraintLayout, AppCompat, MikePenz MaterialDrawer/Iconics/AboutLibraries,
  Xpece preferences) are deliberately left alone. Bumps of Robolectric/Roborazzi can change
  screenshot rendering; they must pass `verifyRoborazziDebug` with no golden changes.

## CI (`.github/workflows/ci.yml`)

| Job | Runs | Required? |
|---|---|---|
| `verify` | `ktfmtCheck detekt`, then `test verifyRoborazziDebug assembleDebug`, then `checkR8KeptNames` | **Yes** (branch protection) |
| `emulator-smoke` (API 24 and 36) | the on-device tests on an emulator, then `check-instrumented-results.py` | No (`continue-on-error`) |
| `emulator-minified` (API 36) | the same tests against the `releaseCheck` minified build | No (`continue-on-error`) |

`check-instrumented-results.py` fails when the app, or either ZehutYosef flavor, produced no results,
ran zero tests, or had failures (a failed APK install otherwise leaves a green job).

## Behavior worth knowing before you touch it

- **Edge-to-edge**: at `targetSdk` 36 content draws under the status bar. `EdgeToEdgeUtil`
  (`Zmanim/src/main/java/com/gindin/util/`) pads the main and preferences screens below the classic
  ActionBar; ZehutYosef's About screen applies system-bar insets for AboutLibraries' activity only.
- **Alerts**: `ZmanimReceiver` re-arms a ~30-minute inexact repeating check; each tick (and boot,
  time change, location update) runs `ZmanNotificationScheduler`, which sets one alarm per upcoming
  zman. Whether to *show* (notifications enabled, Shabbat/Yom Tov rules) is decided when the alarm
  fires, not when it is scheduled. Exact vs inexact depends on `canScheduleExactAlarms()`.
- **Permissions** (Android 13+/12+): the app asks, once each, for location, then notifications
  (`NotificationPermission`), then exact alarms (`ExactAlarmPermission`), never stacked, with a
  recovery row in Preferences. Android does not prompt for notifications on its own.
- **Dialogs on activities**: never `show()` a dialog on an activity that is finishing or destroyed
  (`BadTokenException`; see `DeterminingLocation` and the permission helpers).

## Working on a constrained machine

Builds are heavy. A Gradle daemon at 4 GB and an emulator at its default 4-6 GB each add up; the
kernel OOM killer has killed emulators and sessions here. Run **one emulator at a time**, headless,
with a small footprint (`emulator -avd <name> -port <even port> -memory 2560 -cores 2 -no-window
-no-audio -no-boot-anim -gpu swiftshader_indirect -no-snapshot`), stop it when the tests that need
it are done, run one heavy Gradle build at a time, and `./gradlew --stop` afterwards. Never run
instrumented tests against a physical phone that has the real app installed.

## Planning documents

- `docs/modernization/modernization-roadmap.md`: the multi-phase direction (Phase 1 foundation,
  2 location, 3 liturgical engine, 4a/4b Compose, 5 Wear OS, 6 iOS).
- `docs/modernization/phase1/phase-1-design.md` and `phase-1-implementation.md`: the design and the
  task-by-task plan for Phase 1, with a status note at the top of each task describing what was done
  and where it differs from the original steps. Each phase gets its own design and plan; keep the
  status notes current when you finish work.

## Quick answers

- Run only `:ZmanLib`'s tests: `./gradlew :ZmanLib:test`
- Check style before committing: `./gradlew ktfmtCheck detekt` (fix with `./gradlew ktfmtFormat`)
- Where version control lives and with what: `github.com/jgindin/zmanim` through colocated `jj`
  over Git; merge PRs with "Rebase and merge".
