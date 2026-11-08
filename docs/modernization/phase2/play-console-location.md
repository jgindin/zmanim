# Play Console: location declarations and the target-API warning

This is preparation for the maintainer: everything the Play Console asks for, gathered on
2026-11-07 from Google's help pages and from the code. Every Play Console step here **requires a
human**.

Only Zmanim (`com.gindin.zmanim.android`) uses location. ZehutYosef (`com.gindin.barmitzvah`,
`com.gindin.zehutyosef`) requests no location permission, so none of the location declarations apply
to it.

## 1. "App must target Android 16 (API level 36) or higher" on both apps

**What the code says:** both apps have targeted API 36 since 2026-09-19 (commit `4cb44f51`, in
`build-logic/.../zmanim.android-application-conventions.gradle.kts`). A local Zmanim release APK
(4.0.410) reports `targetSdkVersion:'36'`.

**Why the warning can still show:** the policy status describes what is on Play, not what is in
the repository. Google says only non-compliant apps get the warning, so Play is looking at
something that still targets less than 36. The likely candidates:

1. **An older release still active on a testing track.** Internal, closed and open testing tracks
   keep their last release until it is replaced or the track is paused. Google's permission pages
   say these releases count, and advise pausing unused tracks. For the target-API rule, this is my
   inference: Google's target-API page does not say whether testing tracks count.
2. **Zehut Yosef has no API 36 release on Play.** The repository records Zehut Yosef 77 as released.
   It doesn't say which flavor, or which commit it was built from. Play App Signing for
   `com.gindin.barmitzvah` is still unconfirmed, which suggests that app's last upload is old.
3. **The 409 release of Zmanim never reached production.** For example, it is in review, halted,
   or in a draft.

**How to tell which** (for each app):

- **Policy and programs → Policy status:** open the issue. Its details page lists the affected
  version codes or tracks, and offers the extension form.
- **Test and release → Latest releases and bundles** (App bundle explorer): each version shows its
  target SDK. Look for anything below 36 that is still on a track.
- **Test and release → Testing** (Internal, Closed, Open): any track whose release is below 36 needs
  a new release or should be paused.

**The fix:** upload a release built from `main` to each affected track (they all target 36), or
pause the tracks you no longer use. For ZehutYosef, build each flavor that is a published app.

**Deadline:** the warning says it was enforced on Aug 30, 2026. Updates are being rejected now, so
this blocks the next release of each app.

## 2. Background location (`ACCESS_BACKGROUND_LOCATION`), Zmanim

Zmanim already ships this permission, so a background-location declaration should already be on file.
Google re-reviews it whenever a bundle on any track requests it, and a Phase 2 release changes how
location is gathered. Check it is current.

- **Where:** Policy → App content → Sensitive app permissions → Start → Location permissions.
  "Show summary" there lists the affected bundles.
- **Form:**
  - The app's main purpose.
  - The **one** location feature that needs background access, and why it can't work without it.
    Naming several features is rejected.
  - A video link, ideally 30 seconds or less.
- **Draft answers:**
  - **Main purpose:** shows halachic times (zmanim) for the user's location, and alerts before
    the zmanim the user chooses.
  - **Feature:** zman alerts. A periodic check runs while the app is closed and schedules each
    alert from the sunrise and sunset at the device's current location. Without background
    access, the alerts would fire at the times for wherever the user last opened the app. A
    traveler would be alerted at the wrong time for candle lighting or the end of Shabbat.
- **Video must show:**
  - Turning on alerts in the app.
  - The in-app disclosure (the "Access Your Location in the Background" dialog).
  - The system prompt where the user picks "Allow all the time".
  - An alert arriving while the app is closed.
  - It must be the Android app. YouTube is preferred; a Drive link to an MP4 also works.
- **Prominent disclosure** (`why_background_location.html`, shown before the system prompt):
  - It already includes "location", "when the app is closed or not in use", and the feature
    (notifications of upcoming zmanim).
  - It has no ads clause, which is right because the app has no ads.
- **Privacy policy:** the store listing and the app (`privacy.html`) must link to
  <https://info.zmanim.today/privacy.html>.

## 3. Precise location (`ACCESS_FINE_LOCATION`), Zmanim (decision D7)

- **When:**
  - Google says the declaration becomes available in Play Console in **November 2026**.
  - Compliance is mandatory from **2027-01-27**, with 30-day self-extensions available.
  - Updates that don't comply may be rejected.
- **Where:** Google has not published the menu path. It is expected next to the background-location
  declaration (Policy → App content → Sensitive app permissions), or as a new App content item.
  Once it appears, Policy status should show it as an issue to address.
- **Form:**
  - **Question 1:** which user-facing features need `ACCESS_FINE_LOCATION`. The choices are:
    Search nearby, One time location sharing, Live tracking, Location tagging by user, Automated
    location tagging, Personalization, Navigation, Area specific actions, Nearby devices, Browser
    Location Access, Network diagnostics, Other.
    - **Suggested answer:** **Other**, described as "Calculating halachic prayer times (zmanim)
      from the sun's position at the device's location, in the app and for alerts while the app
      is closed". **Area specific actions** is a possible second choice.
  - **Question 2:** why approximate location or the location button is not enough, and why ongoing
    precise access is needed. Google expects a technical justification. Draft:

    > Zmanim computes halachic times from the sun's position at the device's exact coordinates and
    > altitude, and uses those times both on screen and to schedule alerts while the app is closed.
    > Approximate location can be off by about 2 km, which moves sunrise and sunset by several
    > seconds. Some users rely on the times to the second, for example for the latest time to say
    > Shema or the start of Shabbat. Approximate location also carries no altitude, and the app's
    > elevation-adjusted sunrise and sunset use the fix's altitude, which can change those times by
    > a minute or more in hilly or high places. The Android location button grants location for one
    > session only and cannot serve the periodic background check that schedules the alerts. Users
    > who grant only approximate location can still use the app, with a warning that times may be
    > slightly off.

- **Risks, honestly stated:**
  - **Google's examples are weak support:** its examples of acceptable persistent precise use are
    navigation and fitness tracking. A few seconds of shift is a weak argument on its own; the
    altitude argument and the background alerts are the strong ones.
  - **The approximate-only warning isn't built yet:** the last sentence of the draft becomes true
    when Phase 2 ships it (decision D3, Task 6). File the declaration after that release, or remove
    the sentence.
  - **If rejected:** drop `ACCESS_FINE_LOCATION` (the design's risk table). The approximate-only
    path then becomes the only path, and elevation falls back to the place index (Section 5.5 of
    the design).

## 4. Things a reviewer may compare against

- **The privacy policy contradicts the system geocoder:**
  - `privacy.html` (last updated 2021-04-01) says location data "never leaves your device".
  - Naming a place sends its coordinates to the system geocoder, which on Play devices is Google's
    service (decision D4 says this is disclosed).
  - A reviewer comparing the two could object. Worth rewording before filing.
- **The About page credits services the app no longer uses:**
  - `about.html` credits MapQuest/Nominatim; the app no longer uses them (decision D4).
  - Not a declaration item, but the About page and the privacy policy should agree with the data
    safety form.
- **The Data safety form:**
  - It must list approximate and precise location, collected and used for app functionality.
  - It should match the declarations above.

## Sources

- Declare permissions for your app: <https://support.google.com/googleplay/android-developer/answer/9214102>
- Location permissions, minimum scope (precise location declaration): <https://support.google.com/googleplay/android-developer/answer/17033915>
- Background location: <https://support.google.com/googleplay/android-developer/answer/9799150>
- Target API level requirements: <https://support.google.com/googleplay/android-developer/answer/11926878>
