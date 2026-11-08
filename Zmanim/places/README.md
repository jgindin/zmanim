# The bundled place index

`Zmanim/src/main/assets/places/places.tsv` lets the app name a place, and find a typed one, with no
network connection. It is generated from GeoNames by the `:Zmanim:generatePlaceIndex` task and
committed, so builds never download anything. The format is described in
`build-logic/.../places/PlaceIndexData.kt`.

## Source and license

- **Data:** GeoNames `cities15000` (every place with a population over 15,000, and every capital),
  plus `countryInfo.txt` for the country names, from <https://download.geonames.org/export/dump/>.
- **Downloaded:** 2026-11-07. The checksums are in `geonames-pins.tsv`.
- **License:** Creative Commons Attribution 4.0 (<https://creativecommons.org/licenses/by/4.0/>).
  GeoNames provides the data "as is", without any warranty of accuracy or completeness.
- **Attribution** (on the About screen, in `NOTICE`, and in the asset's second line): "Place data
  from GeoNames (geonames.org), licensed under CC BY 4.0 (creativecommons.org/licenses/by/4.0).
  Filtered and converted for this app."

## What the generator changes

- **Columns kept:** name, country, latitude, longitude, elevation, and time zone. Places are written
  in descending order of population, so the larger of two places with the same name comes first.
- **Elevation:** GeoNames' `elevation` column (meters, measured) when it has one, else its `dem`
  column (the average of the roughly 90 m terrain cell), else unknown. `-9999` in `dem` means "no
  data". In the 2026-11-07 data, out of 34,205 places:
  - 4,495 take `elevation`
  - 29,654 take `dem`
  - 56 have `dem` "no data" and no elevation, so their elevation is unknown
- **Overrides:** `place-overrides.tsv` (owned by the maintainer) replaces the country, name or time
  zone of specific GeoNames records. Each row also carries the record's GeoNames name, and the
  generator fails if that no longer matches. The generator also fails if an override would move a
  place into a country shown as "Palestine" or "Palestinian Territory". Every run prints each record
  GeoNames assigns to `PS` and what the overrides make of it, and writes the same list to
  `Zmanim/build/places/ps-review.tsv` for review.
- **Jewish-community flag:** always 0 until there is a curated list.

## Regenerating

```bash
./gradlew :Zmanim:generatePlaceIndex
```

- **With the current pins:** the run reuses the files in `Zmanim/build/places/downloads` when their
  checksums match, and downloads them otherwise.
- **When GeoNames has published newer data:** the download fails with the new checksum, because
  GeoNames rebuilds the files daily under the same URLs. To move to the newer data:
  1. Put the new checksum in `geonames-pins.tsv`, and update the download date here and in the pins
     file.
  2. Rerun the task.
  3. Review the printed `PS` list and the diff of the asset, and update the counts above.
