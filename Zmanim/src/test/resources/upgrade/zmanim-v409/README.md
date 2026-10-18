# Saved state written by the released :Zmanim app (versionCode 409)

`files/` and `shared_prefs/` mirror the app's private data directory. Every value is invented
(public places only); nothing comes from a real device.

The bytes were produced by the released Java sources at commit `54dd5632ace7`, extracted with
`git archive 54dd5632ace7 | tar -x -C <scratch>/old-code` and run under Robolectric (SDK 33) by a
throw-away JUnit 4 test placed in that extracted tree, invoked with
`./gradlew :Zmanim:testDebugUnitTest --tests '*FixtureGenerator*'`. The test calls the app's own
writers and copies the resulting `files/` and `shared_prefs/` directories out:

| File | Written by |
| --- | --- |
| `files/LastKnownLocationProvider` | `ZmanLocationUtil.save` of `ZmanLocationUtil.fromLocation` for an `android.location.Location("fused")` fix (unresolved name, so `"name": " - - "`, and no `userName`) |
| `files/CachedLocations` | `CachedLocationResolver.cache` for four resolved locations (Albany with no `userName`, Lake Placid with a typed `userName`, Jerusalem and New York City with `"userName": ""`), which saves through the resolver's own `saveCache` |
| `files/2026_9_16.notifiedZmanim`, `2026_9_17...`, `2026_9_18...` | `NotifiedZmanim.Manager.get`, `notified`, `save`; the 16th has no zman notified |
| `shared_prefs/PERSONAL.xml` | `SharedPreferences` editor with `PersonalPreferences.Prefs` constant names and `Zmanim.Type.name()` keys |
| `shared_prefs/ZMANIM_SELECTION.xml` | `SharedPreferences` editor storing `Zmanim.Type.name` (the display name) -> the zman name, the way `ZmanimPresenterImpl.userChangedZman` stores it |
| `shared_prefs/LOCATION_MANAGEMENT.xml` | `SharedPreferences` editor with `LocationProviders` constant names; latitude and longitude text from `LatitudeLongitudeParser.toFullString(false)` |
| `shared_prefs/VERSION.xml` | `lastKnownCodeVersion` = 409 |

Android's `org.json` writes `/` as `\/` and keeps the keys in insertion order, which is why
`LastKnownLocationProvider` and `CachedLocations` contain `"America\/New_York"`.

The same `files/` content is also in `:ZmanLib`'s test resources.
