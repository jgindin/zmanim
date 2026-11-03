# Zmanim golden table

`zmanim-golden.tsv` holds the time of every zman option of every `Zmanim.Type`, for a matrix of places and
dates. `UT_ZmanimGoldenTable` recomputes each cell with the current code and compares it with this file.

## How it was produced

The released Java implementation (git commit `54dd5632ace7`) computed every cell:

```
git archive 54dd5632ace7 | tar -x -C <scratch>/old-code
\cp -f ZmanimGoldenTable.java <scratch>/old-code/ZmanLib/src/test/java/com/gindin/zmanlib/golden/
# plus a throw-away JUnit 5 test next to it whose only statement writes the table:
#   Files.write( Paths.get( System.getenv( "GOLDEN_OUT" ) ),
#     ZmanimGoldenTable.render().getBytes( StandardCharsets.UTF_8 ) );
cd <scratch>/old-code
GOLDEN_OUT=<scratch>/golden.tsv ./gradlew :ZmanLib:test --tests '*GoldenTableGenerator*'
```

`ZmanimGoldenTable.java` (in `src/test/java/com/gindin/zmanlib/golden`) compiles unchanged against both the Java
and the Kotlin implementation. The output is identical when run with a different default JVM time zone.
Regenerating the table requires the old code at that commit; the table is not meant to be regenerated from the
current code.

## Contents

- One row per place, elevation setting and date; 84 rows, 230 zman columns. Values are exact.
- Places: New York (America/New_York), Jerusalem (Asia/Jerusalem), Seattle (America/Los_Angeles),
  Sydney (Australia/Sydney), Tromso (Europe/Oslo; several zmanim have no time around the solstices),
  Denver (America/Denver, 1609 m; rows with elevation both ignored and applied). Coordinates and elevations are
  constants in `ZmanimGoldenTable.java`.
- Dates for every place: 2025-04-12 (Erev Pesach on Shabbat), 2026-03-20 (equinox, Friday), 2026-04-01
  (Erev Pesach), 2026-04-02 (first day of Pesach), 2026-06-21 (solstice), 2026-07-23 (Tisha B'Av),
  2026-09-23 (equinox), 2026-11-07 (Shabbat), 2026-12-21 (solstice), 2028-02-29 (Gregorian leap day), plus
  the two daylight saving time changeover days of that place's time zone in 2026.
- Columns: `place`, `elevation` (`off`/`on`: whether `ZmanimFactory.useElevation` was set), `date`, then one
  column per zman option named `<Zmanim.Type constant>|<zman name>|<advanced>` in the order the app lists
  them, then `listed`: the types `HebrewDate.getZmanim` returns for that date and place.
- A zman cell is the number of milliseconds from 00:00 UTC of the row's date to the zman (negative for a
  zman before that instant), or `-` where the library returns no time.

## Changes since it was produced

- The columns of "Geonim 3.65 deg", "Geonim 5.88 deg" and "Baal HaTanya Lechumra 30 Minutes" were removed when
  those zmanim were dropped from the app, because KosherJava 3 no longer offers them. No other cell changed.
- The move to KosherJava 3.0.0 changed these cells, and the table was regenerated from the new code. The maintainer
  accepted each change (2026-11-02):
  - Chatzot moved by up to about 15 seconds because the library's solar noon calculation changed. This reaches the
    zmanim based on it: night chatzot, 3 and 2 hours before chatzot, mincha gedola 30 minutes, lechumra 30 minutes
    and Rabbi Yaakov Moshe Hillel.
  - Alot 60 minutes and the zmanim based on it, and the Rav Moshe Feinstein half-day zmanim, moved by up to about 6
    minutes in Denver with elevation off, because they now honor the elevation setting.
  - The zmanim calculated as a fraction of the day moved by up to 12 milliseconds, from rounding.
  - The Chametz zmanim have no time except on Erev Pesach, which is the only day the app lists them.
  - Rabbi Yaakov Moshe Hillel's mincha gedola gained or lost a time in 4 cells in Tromso.

  The port keeps a fixed solar radius and the old Earth radius, so sea-level sunrise and sunset did not move.
- The calculator then took the library's default radii (2026-11-02): the sun's apparent radius for the date
  instead of a fixed one, and an Earth radius of 6371.0088 km instead of 6356.9 km. Sunrise and sunset moved by up
  to about 2.5 seconds either way (the sun's apparent size changes over the year), and Denver's
  elevation-adjusted sunrise by about half a second more. The zmanim based on sunrise and sunset moved with them,
  by up to about 3.3 seconds for the zmanim calculated as a fraction of the day. Zmanim based on the sun's depth
  below the horizon did not move, and no zman gained or lost a time. The table was regenerated from the new code.

The table now comes from the library's default radii, and was last regenerated on 2026-11-02.
