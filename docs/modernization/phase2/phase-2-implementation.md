# Phase 2: Modern Location Architecture & Offline Resilience Implementation Plan

> **For agentic workers:** execute task by task, one PR per task, merged with "Rebase and merge" (see `AGENTS.md`). Each task's final verification step is a hard gate: if it fails, stop and fix before the next task. Steps use checkbox (`- [ ]`) syntax. **One purpose per commit and PR**: data generation, interfaces, adapters and wiring never share a change. Task 0 is done (answers recorded in the design, Section 8), and the maintainer approved the design on 2026-11-05.

**Design:** `phase-2-design.md` (approved 2026-11-05).
**Goal:** Location and place resolution that works offline, as a `Flow`, with a trustworthy elevation, without changing any screen.
**Architecture:** Pure-Kotlin interfaces and decision logic in `:ZmanLib`; Android adapters in `:Zmanim`; an event-bus bridge keeps the current screens working until Phase 4a.
**Tech additions:** `kotlinx-coroutines-core` (and `-android`, `-play-services` or the Task-await bridge for `Task`). No HTTP client and no database (decisions D1 and D4).

## Global constraints

- Gates on every task: `./gradlew ktfmtCheck detekt` clean (no baseline), `./gradlew test verifyRoborazziDebug assembleDebug` green with **no golden change**, `./gradlew checkR8KeptNames` green, CI green including both emulator jobs.
- Tests first for every behavior; mutation-check each new test (break the code once, watch it fail, restore).
- Fixtures are synthetic. Saved-state fixtures come from running the shipped code (see the READMEs under `*/src/test/resources/upgrade/` and `golden/`). Never commit a real user's location.
- Comments describe what the code does and why, in the present tense.
- Human-only steps are marked **REQUIRES A HUMAN**: privacy policy, Play Console, any decision in Task 0.

---

### Task 0: Resolve the open decisions (done 2026-10-28)

D1 to D6 are answered in `phase-2-design.md` Section 8. Remaining human items from them: approve the final design document; review the list of places tagged `PS` that Task 3 prints and say which belong in `place-overrides.tsv`.

---

### Task 0A: Upgrade KosherJava from 2.5.0 to 3.0.0 (prerequisite, not location work)

**Why here:** this is a library upgrade with no connection to location. It runs before Task 1 only so that the changes expected to alter the zman golden table land alone, before the location work and its "no golden change" gate. It is its own PR (more than one, per the steps below); no location change shares a commit with it. It can move out of Phase 2 unchanged.

**The target is 3.1.0, in two steps.** This task lands on 3.0.0, and Task 0C bumps to 3.1.0 (decision, 2026-11-02). 3.1.0 was released on 2026-11-02 and reached Maven Central on 2026-11-03. It adds to the 3.0.0 API without removing any public method of the calendar, `GeoLocation` or calculator classes, but it changes polar-day results and no switch keeps the old ones, so each golden change keeps one cause.

**What 3.0.0 changes for us** (found by comparing the 3.0.0 source and release notes with every KosherJava member `:ZmanLib` calls; this is a rewrite of the API, not a set of renames):

- **API model:** `ComplexZmanimCalendar` is now `ComprehensiveZmanimCalendar`. Zmanim return `Instant` (not `Date`), shaos zmaniyos return `Duration`, the day is set with `LocalDate` (there is no `getCalendar()`), and `GeoLocation` takes a `ZoneId` (there is no `timeZone`). `ZmanimCalculator` (which subclasses the library class and calls `date.syncCalendar(getCalendar())`) and the `Date`-returning `toCalendar` helper are the center of the work. The minimum Java is 11; `java.time` needs core library desugaring at `minSdk` 24 (to confirm in Step 1).
- **Pure renames, same zman** (per the release notes): `getAlos{60,72,90,96,120}` and `getTzais{60,72,90,96,120}` gain `Minutes`; `getTzais` becomes `getTzaisGeonim8Point5Degrees`; `getSofZmanShmaMGA` and `getSofZmanTfilaMGA` become `...MGA72Minutes`; `getMinchaGedola`, `getMinchaKetana`, `getPlagHamincha` gain `GRA`; `getMinchaGedolaGreaterThan30` becomes `getMinchaGedolaGRAGreaterThan30`; `getChatzos` becomes `getChatzosHayom`; `getSofZmanTfilahAteretTorah` becomes `getSofZmanTfilaAteretTorah`; the four `getBainHasmashosRT...` become `getBainHashmashosRT...`; `getPlagAlos16Point1ToTzaisGeonim7Point083Degrees` and `getSofZmanShmaAlos16Point1ToTzaisGeonim7Point083Degrees` gain `Degrees` after `16Point1`. `getFixedLocalChatzos` becomes `getFixedLocalChatzosHayom`, and the notes say its implementation changed, so the golden table decides whether it is a pure rename.
- **Removed:** `getTzaisGeonim3Point65Degrees`, `getTzaisGeonim5Point88Degrees` (dropped as too early) and `getMinchaGedolaBaalHatanyaGreaterThan30`. Decision (2026-11-01): drop these zmanim.
- **Renamed and the time changes** (the library says the old values were too early): `getTzaisGeonim4Point37Degrees` becomes `...4Point42Degrees`, and `getTzaisGeonim4Point61Degrees` becomes `...4Point66Degrees`. Decision (2026-11-01): this task keeps the old times by computing 4.37 and 4.61 degrees in `ZmanimCalculator` with `getSunsetOffsetByDegrees`; Task 0B switches to the library's methods and deletes the custom ones.
- **Defaults that change times without changing a name:** sunrise and sunset now use the sun's apparent radius for the date by default (`AstronomicalCalculator.useApparentSolarRadius`), which moves every sunrise and sunset by about a second or two. The `getSofZman*Chametz*` zmanim return null on any day but Erev Pesach. 3.0.0 also changed the default Earth radius from 6356.9 km to 6371.0088 km, which moves elevation-adjusted times by up to half a second. Decision (2026-11-01 and 2026-11-02): this task sets `useApparentSolarRadius` to false and the Earth radius to 6356.9 km so sunrise and sunset stay those of 2.5.0; Task 0B removes both overrides.
- **Changes with no switch, accepted by the maintainer (2026-11-02):** solar noon moved by up to about 15 seconds, which moves chatzot and the zmanim based on it; Alot 60 minutes and the Rav Moshe Feinstein half-day zmanim now honor the elevation setting, which moves them by up to about 6 minutes in elevated places with elevation off; zmanim calculated as a fraction of the day move by up to 12 ms from rounding; Rabbi Yaakov Moshe Hillel's mincha gedola gains or loses a time in polar cells. The Chametz zmanim return null away from Erev Pesach, the only day the app lists them.

- [x] **Step 1: Survey.** On a branch, set the version and compile. Confirm the table above against the compiler, and whether desugaring is needed (an `Instant` call on API 24 to 25 fails at run time without it; `emulator-smoke (24)` exercises that). Check whether the Chametz zmanim returning null outside Erev Pesach changes any golden cell or any screen; if it does, stop and ask the maintainer. Write the findings in the PR.
- [x] **Step 2:** The decisions above are made (drop the three removed zmanim; keep the old 4.37 and 4.61 times; solar radius off). Check how the saved zman selection and notified-zman upgrade tests (`UT_UpgradeZmanimTypeNames`, `UT_UpgradeNotifiedZmanim`) treat a type that no longer exists, and add a test if they do not already cover it.
- [x] **Step 3:** Port `ZmanimCalculator`, `toCalendar` and the `Zman` implementations to the new API with no behavior change: keep the public `Zmanim.Type`/`Zman` surface, keep returning `Calendar`/`Date` to callers, and keep the saved location JSON format.
- [x] **Step 4:** Remove the dropped zmanim, add the custom 4.37 and 4.61 degree calculations, and turn the apparent solar radius off, each in its own commit.
- [x] **Step 4a: Tell the users who had a removed zman.** Saved selections are stored per zman type as the zman's name (`PrefsBasedZmanCallback` reads `ZMANIM_SELECTION` by `type.displayName`), so the dropped zmanim are recognized by name: "Geonim 3.65 deg", "Geonim 5.88 deg" and "Baal HaTanya Lechumra 30 Minutes". In `AppUpgrader.handle` (`Zmanim/src/main/java/com/gindin/zmanim/android/AppUpgrader.java`), when `lastKnownCodeVersion` is below the version code of the release that ships this task, check the selection prefs (and the alert/notified-zman settings, which `UT_UpgradeNotifiedZmanim` covers) for any of those names; if one is found, add a note id to the `showUpgradeNotes` call naming the zman(s) removed and what the app shows instead. Users who never selected them see nothing. First find out what the app shows today for a saved name it no longer recognizes, make that fallback explicit and tested (the same setting the type would have by default), and word the message to match. Add the string to `values/strings.xml` the way `upgrade_from_279` is, with English placeholders in any translated `values-*` folders like the other upgrade notes. Tests: a Robolectric test of `AppUpgrader` with a saved removed name (message shown, setting falls back), with a kept name (no message), and with a fresh install (no message).
- [x] **Step 5: Golden table.** With the old radii set, the differences must be exactly the accepted ones above. This task is the one exception to "the table is never re-recorded": review every difference against that list, then regenerate the table and the expected tables in the tests from the new code in a dedicated commit, and list the changes in `golden/README.md`. Anything outside the list stops the task.
- [x] **Step 6: Verify** the Phase gates (the golden table may change only as Step 5 allows), plus `emulator-smoke (24)` for the `java.time` question. Dependabot PR #40 is already closed. Commit: `jj commit -m "Upgrade KosherJava to 3.0.0"` (removals, the custom zmanim, the upgrade note, the port, and the regenerated tables in separate commits).

---

### Task 0B: Adopt the library's new defaults and zmanim (the next PR after 0A)

**Why separate:** these two changes deliberately move times that users see. Task 0A proves the port changed nothing; this task changes times on purpose, one reviewed step at a time, so each golden change has one cause.

- [x] **Step 1: Apparent solar radius and Earth radius.** Remove both Task 0A overrides, so the calculator uses the library's defaults. Regenerate the golden cells from the new code in this commit only, review the diff, and record in the PR the range of the change (expected: a second or two on sunrise, sunset and the zmanim derived from them, nothing larger). Update `golden/README.md` to say the table is now produced by the library's default radii and when it was last regenerated.
- [x] **Step 2: Library methods for the 4.42 and 4.66 degree zmanim.** In `TzaitHacochavim.kt` call `getTzaisGeonim4Point42Degrees` and `getTzaisGeonim4Point66Degrees` and delete the custom 4.37 and 4.61 degree calculations from `ZmanimCalculator`. Decide, and write in the PR, how the zman's displayed name and the saved type name change (the saved selection and notified-zman upgrade tests must pass; keep the old saved names working if a user has them selected). Regenerate only those two columns, in a separate commit from Step 1. Decision (2026-11-02): the zmanim are shown as "Geonim 4.42 deg" and "Geonim 4.66 deg"; a saved "Geonim 4.37 deg" or "Geonim 4.61 deg" selection moves to the new name on upgrade (`ZmanimSelectionPrefsManager.renameZmanim`), and users who had one are told.
- [x] **Step 3: Verify** the Phase gates. The only golden differences are the ones explained by Steps 1 and 2. Commit each step on its own.

---

### Task 0C: Bump KosherJava to 3.1.0

**Why separate:** 3.1.0 was not on Maven Central when Task 0A landed. This needs only that it is published; it can land before or after Task 0B, but not in the same PR.

**What 3.1.0 adds on top:** zmanim that used to return an invalid time on very short polar days return null (`getMinchaGedola30Minutes`, `getSofZmanShma3HoursBeforeChatzos`, `getSofZmanTfila2HoursBeforeChatzos`, `getFixedLocalChatzosHayom` and the half-day zmanim based on it, the Ateret Torah and Ahavat Shalom mincha ketana and plag zmanim, and the Yereim bain hashmashos zmanim), `getMinchaGedolaGreaterThan30` and `getMinchaGedolaAhavatShalom` fall back to the regular mincha gedola in the same case, and `NOAACalculator` reports events it used to miss when the sun only just reaches them. Calculators now cache sunrise, sunset, noon and midnight (keyed on every input, cleared by the setters). `GeoLocation` gains optional horizon elevations, which this task does not use.

- [x] **Step 1:** Set the version. Close any Dependabot PR for 3.1.0 as superseded.
- [x] **Step 2:** Compare the golden table with the new code. Every changed cell must be in a polar row (Tromso) and match a 3.1.0 change listed above, and a zman that loses its time must show as "no time" on the screen rather than crash. Anything else stops the task. Regenerate those cells, record them in the PR and in `golden/README.md`.
- [x] **Step 3: Verify** the Phase gates. Commit: `jj commit -m "Upgrade KosherJava to 3.1.0"`.

---

### Task 1: Pin today's location behavior with tests

**Status (2026-11-05):** done. The tests drive the real classes. A Mockito test dependency stands in for the static Play Services entry points and the final `Geocoder`, and the work sent to `ZmanimService` is read the way the screens receive it (Robolectric at API 24, where `JobIntentService` starts the service directly). Behavior pinned as `...CurrentBehavior`, to fix on purpose in later tasks:

- An approximate-only grant is reported as `NO_PERMISSIONS` (Task 6, D3).
- When the geocoder cannot name a device fix (for example offline), the fix comes back resolved under its coordinates, because a bundle round trip turns the unnamed location's coordinates into its name; that name is cached and wins over the geocoder for that area afterwards (Tasks 4 and 7).
- A geocoded fix takes the address's coordinates instead of keeping the fix's own.
- The elevation stored for a typed place (`PLACE_ELEVATION`) is never read; a typed place is at sea level (Task 5).
- A hit by typed name changes the time zone of the cached entry itself; a hit by proximity cannot carry a custom time zone, because a different time zone counts as a significant change.
- A latitude or longitude of exactly 0 counts as no coordinates, and copying an unnamed location makes its coordinates its name.
- `addressToLocationName` never searches the last address line and keeps the space before the postal code.
- The entered-coordinates acquirer swaps "Invalid latitude" and "Invalid longitude", and an elevation that is not a number throws.

**Why first:** every later task changes code that currently has only partial coverage. Upgrade tests for `CachedLocations` and the last-known location exist (`ZmanLib/.../upgrade/UT_UpgradeSavedLocations`, `Zmanim/.../location/UT_UpgradeLastKnownLocation`), and there are tests under `Zmanim/src/test/.../location/acquirers` and `resolvers`.

**Files:** tests only, under `Zmanim/src/test/java/com/gindin/zmanim/android/location/` and `ZmanLib/src/test/java/com/gindin/zmanlib/location/`.

- [x] **Step 1: Inventory.** List what the existing tests already pin (resolver order, cache match rule "same user name or within 1 km and 5 m", last-known age cutoff of 30 minutes, error codes). Write the gaps into the PR description.
- [x] **Step 2: Fill the gaps** with characterization tests: the resolver chain order and the `PassthroughResolver` ending; cache hit by typed name and by proximity; time-zone override honored on a cache hit; `DynamicLocationAcquirer` outcomes for no permission, no Play Services, location services off, recent last-known, stale last-known. Use fakes for `Geocoder` and the Fused client. Where behavior is clearly a bug, pin it with a test named `...CurrentBehavior` and a comment, and fix it in a later task, not here.
- [x] **Step 3: Verify** `./gradlew :Zmanim:testDebugUnitTest :ZmanLib:test` and the Phase gates above. Commit: `jj commit -m "Pin the current location and place-resolution behavior with tests"`.

---

### Task 2: Domain interfaces and coroutine dependency

**Files:**
- Modify: `gradle/libs.versions.toml` (add `kotlinx-coroutines` and `kotlinx-coroutines-test`), `ZmanLib/build.gradle.kts`
- Create under `ZmanLib/src/main/kotlin/com/gindin/zmanlib/location/`: `LocationSource.kt`, `LocationState.kt`, `PlaceResolver.kt`, `GeocodeCache.kt`, `PlaceIndex.kt`, `ElevationPolicy.kt`
- Create fakes under `ZmanLib/src/test/`

- [ ] **Step 1:** Add the dependencies via the version catalog; keep `explicitApi()` satisfied.
- [ ] **Step 2:** Define the types from the design (Sections 5.1 to 5.3): `LocationSource.locations(request): Flow<LocationState>`, sealed `LocationState` (`Resolving`, `Resolved`, `Unavailable(reason, lastKnown)`), `UnavailableReason` (no permission, approximate-only, services off, no Play Services, timeout), `PlaceResolver` with `suspend fun fromName` / `fromCoordinates` returning a result type that distinguishes "not found" from "failed", `GeocodeCache`, `PlaceIndex` (`nearest(lat, lon)`, `byName(text)`), `ElevationPolicy`.
- [ ] **Step 3:** Implement `ResolverChain(resolvers)` in `:ZmanLib` with the order and failure semantics of Design 5.2 (a source that fails is skipped; the first success wins; the last resort never changes coordinates). Test it with fakes, including cancellation and per-source timeouts (`kotlinx-coroutines-test`).
- [ ] **Step 4: Verify** gates. Commit: `jj commit -m "Add the location domain interfaces and the resolver chain"`.

---

### Task 3: Offline place index and its data generator

**Files:**
- Create: a generator under `build-logic/` or `tools/places/` (decide in the PR; prefer a Gradle task so the asset is reproducible), `Zmanim/src/main/assets/places/` (output), `ZmanLib/.../location/PlaceIndexFormat.kt`, `ZmanLib/.../location/InMemoryPlaceIndex.kt`
- Modify: the About/open-source attribution screen's data, `LICENSES` or a `NOTICE` file

- [ ] **Step 1: Confirm the data terms (REQUIRES A HUMAN sign-off).** Read the current GeoNames readme and the file's columns (license version, elevation and DEM columns). Record the exact text of the attribution required. If D2 chose additional community data, record its license too.
- [ ] **Step 2:** Write the generator: download pinned files by URL and checksum, filter, convert to a compact binary or TSV format with a version header, and write it to the assets directory. The output file is checked in (so builds do not need the network) together with its README: source, date, license, how to regenerate.
- [ ] **Step 2b: Country names (D2).** Add `place-overrides.tsv` (GeoNames id, country code, display country) owned by the maintainer, applied by the generator. The generator prints every source record whose country is `PS` for review (**REQUIRES A HUMAN** to choose which are overridden to Israel) and fails if an overridden record would still display "Palestine" or "Palestinian Territory". Add a test that Jerusalem, Tel Aviv, Haifa, Be'er Sheva and Eilat are "Israel" in the generated asset.
- [ ] **Step 2c: Elevation columns (D5).** Inspect the real file: how many records have `elevation`, how many have `dem`, whether any use the "no data" sentinel. Record the counts in the README and make the loader prefer `elevation`, then `dem`, then unknown.
- [ ] **Step 3:** Implement `InMemoryPlaceIndex` (load once, nearest by great-circle distance with a longitude-wrapped bounding pre-filter, name lookup with Unicode-normalized, case-insensitive prefix and exact match). Tests with a small synthetic file: nearest across the antimeridian and near the poles, ties, empty index, malformed asset (fails safe).
- [ ] **Step 4:** Add the attribution to the About screen data without changing layout (no golden change). Check the asset size against the 3 MB budget; if over, filter harder and document why.
- [ ] **Step 5: Verify** gates and release APK size. Commit in two PRs if the generator and the loader are reviewed separately.

---

### Task 4: Geocoder adapters and cache

**Files:** Kotlin in `Zmanim/src/main/java/com/gindin/zmanim/android/location/resolvers/` (new `.kt` files): `SystemGeocoder.kt`, `FileGeocodeCache.kt`; tests in `Zmanim/src/test/`.

- [ ] **Step 1: `SystemGeocoder`.** On API 33+, wrap `Geocoder.getFromLocation(..., GeocodeListener)` with `suspendCancellableCoroutine`; on API 24 to 32, call the blocking overload on `Dispatchers.IO`; `Geocoder.isPresent()` false means "not available", not an error. Tests with Robolectric's shadow geocoder for both branches.
- [ ] **Step 2: `FileGeocodeCache`.** Reads the legacy `CachedLocations` format on first run, writes the versioned format afterwards, atomic write (temp file then rename), same match rule as today. Tests: the existing v409 fixture loads; a round trip; a truncated file is discarded safely; legacy then new format.
- [ ] **Step 3:** Compose the production chain in one factory (cache, system, offline index) with the timeouts from the design. Do not wire it into the app yet.
- [ ] **Step 4: Verify** gates; add every class reached by name to `Zmanim/r8-kept-names.txt` with its rule if any. Commit per adapter.

---

### Task 5: Elevation policy

**Files:** `ZmanLib/.../location/ElevationPolicy.kt` (implementation), a small geoid grid asset only if the spike below says it is needed.

- [ ] **Step 1: Confirm the datum.** Read the current Android documentation for `Location.getAltitude()` and `getMslAltitudeMeters()` and record the result in the PR. (The design assumes the first is height above the WGS 84 ellipsoid and the second is above mean sea level, available from API 34.)
- [ ] **Step 2: Spike (time-boxed to half a day).** For API 24 to 33, decide whether a coarse bundled geoid correction gives an elevation within a few meters in several well-known places, or whether to fall back to the place index's elevation. Write the result and the chosen approach in the PR; delete the spike code that is not kept.
- [ ] **Step 3:** Implement the policy in the order of Design 5.5 with tests for each source, for "elevation not enabled" (value ignored by the factory as today), and for negative or NaN input (maps to unknown, as `ZmanimLocation.Builder` already does).
- [ ] **Step 4: Verify** gates. Commit.

---

### Task 6: Fused location as a Flow

**Files:** `Zmanim/.../location/acquirers/FusedLocationSource.kt` (new), tests.

- [ ] **Step 1:** Implement `LocationSource` over `FusedLocationProviderClient` with `callbackFlow`: emit a recent last-known location first, then the one-shot high-accuracy request, handle the settings-resolution failure by emitting `Unavailable(servicesOff, resolution)`, cancel updates when the collector goes away. Permission handling per D3.
- [ ] **Step 2:** Tests with a fake client: recent last-known, stale last-known, null last-known, settings failure, collector cancellation removes updates, approximate-only grant.
- [ ] **Step 3: Approximate-location warning (D3).** Find how the app shows a non-blocking message today (`messageDisplay/MessageDisplayer`) and use it to tell the user that an approximate-only grant can move times by a few seconds. Shown only in that case, and not again for the same grant once dismissed (store the flag in the personal preferences like the other "asked" flags). Add a Robolectric test for the message, and a new snapshot golden for that state only if the message is visible in the screen; no existing golden may change.
- [ ] **Step 4: Verify** gates. Commit.

---

### Task 7: Wire it into the app behind the existing contract

**Files:** `ZmanimLocationManager.java` and the acquirers (thinned), `LocationToAddressConverter.java` (becomes a thin entry point), an event-bus bridge in Kotlin, manifest if D6 says so.

- [ ] **Step 1:** Replace `DynamicLocationAcquirer`, `StaticLocationAcquirer` and `LatitudeLongitudeLocationAcquirer` internals with calls into `LocationSource` and the production resolver chain. The events and the `LocationError` codes the screens see do not change.
- [ ] **Step 2:** Replace the resolver chain inside `LocationToAddressConverter` with the new one and stop using the blocking `Geocoder` path. Remove `JsonQueryingResolver` and the other unused resolver classes in a separate commit.
- [ ] **Step 3: Background location (D6: required).** Keep `ACCESS_BACKGROUND_LOCATION`. Write down each background path that needs the location (the periodic alarm check via `ZmanimReceiver` and `ZmanNotificationScheduler`, the `PendingIntent` receiver) in the PR so the Play Console declaration can be checked against it, and add a test that these paths still obtain a location under an approximate-only grant (fake client). Keep `ACCESS_FINE_LOCATION` (D7) and list in the PR where precise location is used, so the precise-location declaration can be checked against it too.
- [ ] **Step 4:** Existing upgrade and snapshot tests pass unchanged; `verifyRoborazziDebug` shows no golden change.
- [ ] **Step 5: Verify** gates. Commit in small steps.

---

### Task 8: End-to-end offline proof

**Files:** new tests under `Zmanim/src/androidTest/`, CI step if needed.

- [ ] **Step 1:** Emulator test with the network disabled (airplane mode or no data) and an injected mock location: typed bundled city resolves with name, time zone and elevation; coordinates get a "near" name; nothing returns an unnamed location. Uses the emulator-only guard in `AppState` like the other tests.
- [ ] **Step 2:** Run the suite against the `releaseCheck` minified build (`-Pzmanim.testBuildType=releaseCheck`) so R8 cannot break asset loading or serialization.
- [ ] **Step 3:** Upgrade check: install the v409 fixture state, upgrade, confirm saved location and cache survive (extend the existing upgrade-state test).
- [ ] **Step 4: Verify** CI: `emulator-smoke` (24 and 36) and `emulator-minified`. Commit.

---

### Task 9: Disclosure, docs, and close-out

- [ ] **Step 1 (REQUIRES A HUMAN):** Confirm no disclosure change is needed (no new third party or data collected; the background-location declaration is unchanged). If Task 7 turned out to change what is collected, update the privacy policy and Data Safety form before release.
- [ ] **Step 1a (REQUIRES A HUMAN, due 2027-01-27, does not wait for this phase):** File the Play Console declaration for `ACCESS_FINE_LOCATION` with the justification in D7, and confirm the background-location declaration still matches (D6). If Play rejects it, raise dropping `ACCESS_FINE_LOCATION` as a separate change.
- [ ] **Step 2:** Update `AGENTS.md` (new location layering, the dataset and how to regenerate it, the emulator offline test) and the roadmap's Phase 2 status. Mark this plan's checkboxes.
- [ ] **Step 3:** Record the maintainer's measurements: APK size delta, cold-start time on a real device before and after, and an offline test on the phone. Add them to the PR.
- [ ] **Step 4:** Commit: `jj commit -m "Document the Phase 2 location architecture and close out the phase"`.

---

## Task order and sizing

| Task | Depends on | Size |
|---|---|---|
| 0 Decisions | none | maintainer time |
| 0A KosherJava 3.0.0 | none | 4 to 6 days, plus the Step 2 decisions |
| 0B New library defaults | 0A | 1 to 2 days |
| 0C KosherJava 3.1.0 | 0A, and 3.1.0 on Maven Central | half a day |
| 1 Pin behavior | 0A, 0B (ordering only; 0C need not wait) | 1 to 2 days |
| 2 Domain interfaces | 0, 1 | 1 to 2 days |
| 3 Place index and data | 0, 2 | 2 to 3 days |
| 4 Adapters and cache | 0, 2 | 3 to 4 days |
| 5 Elevation | 2, 3 | 1 to 2 days |
| 6 Fused Flow | 2 | 1 to 2 days |
| 7 Wiring | 3, 4, 5, 6 | 2 to 3 days |
| 8 Offline proof | 7 | 1 to 2 days |
| 9 Close-out | 7, 8 | 1 day plus human steps |

Tasks 3, 4 and 6 are independent after Task 2 and can be separate PRs in any order, one at a time.
