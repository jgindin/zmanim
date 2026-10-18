# Saved state written by the released BarMitzvah app (versionCode 77)

`shared_prefs/com.gindin.barmitzvah_preferences.xml` is the default shared preferences file of the
`barMitzvah` flavor (`applicationId` `com.gindin.barmitzvah`). The values are invented.

It was produced by the released Java sources at commit `54dd5632ace7`, extracted with
`git archive 54dd5632ace7 | tar -x -C <scratch>/old-code` and run under Robolectric (SDK 33) by a
throw-away JUnit 4 test placed in that extracted tree, invoked with
`./gradlew :ZehutYosef:testBarMitzvahDebugUnitTest --tests '*PrefsFixtureGenerator*'`. The test
edits `PreferenceManager.getDefaultSharedPreferences` using the `Settings.PreferenceKey` keys
(`pref_font_choice`, `pref_font_size`, `pref_keep_screen_on`, `pref_full_screen_on`) and copies the
resulting file out.
