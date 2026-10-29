# Phase 2: Modern Location Architecture & Offline Resilience Implementation Plan

> **For agentic workers:** execute task by task, one PR per task, merged with "Rebase and merge" (see `AGENTS.md`). Each task's final verification step is a hard gate: if it fails, stop and fix before the next task. Steps use checkbox (`- [ ]`) syntax. **One purpose per commit and PR**: data generation, interfaces, adapters and wiring never share a change. Do not start Task 2 until the decisions in Task 0 are answered.

**Design:** `phase-2-design.md` (status Draft until the maintainer approves it).
**Goal:** Location and place resolution that works offline, as a `Flow`, with a trustworthy elevation, without changing any screen.
**Architecture:** Pure-Kotlin interfaces and decision logic in `:ZmanLib`; Android adapters in `:Zmanim`; an event-bus bridge keeps the current screens working until Phase 4a.
**Tech additions:** `kotlinx-coroutines-core` (and `-android`, `-play-services` or the Task-await bridge for `Task`), `kotlinx-serialization-json` (already in the catalog from Phase 1 if used there; otherwise added here), an HTTP client (`HttpURLConnection` from the JDK unless Task 4 finds a reason for OkHttp).

## Global constraints

- Gates on every task: `./gradlew ktfmtCheck detekt` clean (no baseline), `./gradlew test verifyRoborazziDebug assembleDebug` green with **no golden change**, `./gradlew checkR8KeptNames` green, CI green including both emulator jobs.
- Tests first for every behavior; mutation-check each new test (break the code once, watch it fail, restore).
- Fixtures are synthetic. Saved-state fixtures come from running the shipped code (see the READMEs under `*/src/test/resources/upgrade/` and `golden/`). Never commit a real user's location.
- Comments describe what the code does and why, in the present tense.
- Human-only steps are marked **REQUIRES A HUMAN**: privacy policy, Play Console, any decision in Task 0.

---

### Task 0: Resolve the open decisions (REQUIRES A HUMAN)

**Files:** edit `phase-2-design.md` Section 8 only.

- [ ] **Step 1:** Maintainer answers D1 to D6 in the design document (approach, offline data source and any community list, approximate location, network lookup and its disclosure, elevation datum, background location).
- [ ] **Step 2:** Change the design's status to "Approved" and note the answers in a revision-history line. If D1 is not B, rewrite Tasks 3 to 5 before continuing.
- [ ] **Step 3:** Commit: `jj commit -m "Record the Phase 2 design decisions"`.

---

### Task 1: Pin today's location behavior with tests

**Why first:** every later task changes code that currently has only partial coverage. Upgrade tests for `CachedLocations` and the last-known location exist (`ZmanLib/.../upgrade/UT_UpgradeSavedLocations`, `Zmanim/.../location/UT_UpgradeLastKnownLocation`), and there are tests under `Zmanim/src/test/.../location/acquirers` and `resolvers`.

**Files:** tests only, under `Zmanim/src/test/java/com/gindin/zmanim/android/location/` and `ZmanLib/src/test/java/com/gindin/zmanlib/location/`.

- [ ] **Step 1: Inventory.** List what the existing tests already pin (resolver order, cache match rule "same user name or within 1 km and 5 m", last-known age cutoff of 30 minutes, error codes). Write the gaps into the PR description.
- [ ] **Step 2: Fill the gaps** with characterization tests: the resolver chain order and the `PassthroughResolver` ending; cache hit by typed name and by proximity; time-zone override honored on a cache hit; `DynamicLocationAcquirer` outcomes for no permission, no Play Services, location services off, recent last-known, stale last-known. Use fakes for `Geocoder` and the Fused client. Where behavior is clearly a bug, pin it with a test named `...CurrentBehavior` and a comment, and fix it in a later task, not here.
- [ ] **Step 3: Verify** `./gradlew :Zmanim:testDebugUnitTest :ZmanLib:test` and the Phase gates above. Commit: `jj commit -m "Pin the current location and place-resolution behavior with tests"`.

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
- [ ] **Step 3:** Implement `InMemoryPlaceIndex` (load once, nearest by great-circle distance with a longitude-wrapped bounding pre-filter, name lookup with Unicode-normalized, case-insensitive prefix and exact match). Tests with a small synthetic file: nearest across the antimeridian and near the poles, ties, empty index, malformed asset (fails safe).
- [ ] **Step 4:** Add the attribution to the About screen data without changing layout (no golden change). Check the asset size against the 3 MB budget; if over, filter harder and document why.
- [ ] **Step 5: Verify** gates and release APK size. Commit in two PRs if the generator and the loader are reviewed separately.

---

### Task 4: Geocoder adapters and cache

**Files:** Kotlin in `Zmanim/src/main/java/com/gindin/zmanim/android/location/resolvers/` (new `.kt` files): `SystemGeocoder.kt`, `HttpGeocoder.kt`, `FileGeocodeCache.kt`; tests in `Zmanim/src/test/`.

- [ ] **Step 1: `SystemGeocoder`.** On API 33+, wrap `Geocoder.getFromLocation(..., GeocodeListener)` with `suspendCancellableCoroutine`; on API 24 to 32, call the blocking overload on `Dispatchers.IO`; `Geocoder.isPresent()` false means "not available", not an error. Tests with Robolectric's shadow geocoder for both branches.
- [ ] **Step 2: `HttpGeocoder`** (only if D4 allows an online lookup). Nominatim `reverse` and `search` endpoints, JSON via `kotlinx.serialization`, identifying `User-Agent` carrying the app id/version and a contact, 1 request per second enforced by a shared limiter, connect and read timeouts of a few seconds, base URL configurable, HTTPS only, no retry storms (one retry with backoff at most). Tests against a local fake server (`MockWebServer`) for success, 429, 403, timeout, malformed body, and rate limiting.
- [ ] **Step 3: `FileGeocodeCache`.** Reads the legacy `CachedLocations` format on first run, writes the versioned format afterwards, atomic write (temp file then rename), same match rule as today. Tests: the existing v409 fixture loads; a round trip; a truncated file is discarded safely; legacy then new format.
- [ ] **Step 4:** Compose the production chain in one factory (cache, system, offline index, HTTP) with the timeouts from the design. Do not wire it into the app yet.
- [ ] **Step 5: Verify** gates; add every class reached by name to `Zmanim/r8-kept-names.txt` with its rule if any. Commit per adapter.

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
- [ ] **Step 3: Verify** gates. Commit.

---

### Task 7: Wire it into the app behind the existing contract

**Files:** `ZmanimLocationManager.java` and the acquirers (thinned), `LocationToAddressConverter.java` (becomes a thin entry point), an event-bus bridge in Kotlin, manifest if D6 says so.

- [ ] **Step 1:** Replace `DynamicLocationAcquirer`, `StaticLocationAcquirer` and `LatitudeLongitudeLocationAcquirer` internals with calls into `LocationSource` and the production resolver chain. The events and the `LocationError` codes the screens see do not change.
- [ ] **Step 2:** Replace the resolver chain inside `LocationToAddressConverter` with the new one and stop using the blocking `Geocoder` path. Remove `JsonQueryingResolver` and the other unused resolver classes in a separate commit.
- [ ] **Step 3: Audit background location (D6).** Find every place a location is requested while the app is not in the foreground (the `PendingIntent` receiver, `ZmanimService`). If none needs `ACCESS_BACKGROUND_LOCATION`, remove it; if one does, keep it and record the use for the Play declaration.
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

- [ ] **Step 1 (REQUIRES A HUMAN):** Update the privacy policy and the Play Data Safety form for any new data sharing (coordinates to a geocoding provider), and the background-location declaration if it changed. Do this before the release containing Task 4 or 7 ships.
- [ ] **Step 2:** Update `AGENTS.md` (new location layering, the dataset and how to regenerate it, the emulator offline test) and the roadmap's Phase 2 status. Mark this plan's checkboxes.
- [ ] **Step 3:** Record the maintainer's measurements: APK size delta, cold-start time on a real device before and after, and an offline test on the phone. Add them to the PR.
- [ ] **Step 4:** Commit: `jj commit -m "Document the Phase 2 location architecture and close out the phase"`.

---

## Task order and sizing

| Task | Depends on | Size |
|---|---|---|
| 0 Decisions | none | maintainer time |
| 1 Pin behavior | none | 1 to 2 days |
| 2 Domain interfaces | 0, 1 | 1 to 2 days |
| 3 Place index and data | 0, 2 | 2 to 3 days |
| 4 Adapters and cache | 0, 2 | 3 to 4 days |
| 5 Elevation | 2, 3 | 1 to 2 days |
| 6 Fused Flow | 2 | 1 to 2 days |
| 7 Wiring | 3, 4, 5, 6 | 2 to 3 days |
| 8 Offline proof | 7 | 1 to 2 days |
| 9 Close-out | 7, 8 | 1 day plus human steps |

Tasks 3, 4 and 6 are independent after Task 2 and can be separate PRs in any order, one at a time.
