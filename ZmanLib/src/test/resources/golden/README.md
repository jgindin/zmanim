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

- One row per place, elevation setting and date; 84 rows, 239 zman columns. Values are exact.
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
