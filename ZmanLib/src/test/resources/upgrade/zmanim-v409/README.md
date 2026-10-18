# Saved state written by the released :Zmanim app (versionCode 409)

`files/` is a copy of the `files/` directory in `Zmanim/src/test/resources/upgrade/zmanim-v409/`;
see the README there for how every file was produced from the released Java sources at commit
`54dd5632ace7`.

`zmanim-type-display-names.txt` lists every `Zmanim.Type` as `CONSTANT=display name`, in declaration
order, taken from that same release. The constant names are the keys of the per-zman notification
offsets in `PERSONAL.xml` and the display names are the keys of `ZMANIM_SELECTION.xml`, so neither
may change.

The binary files are generated once and committed. Regenerating them changes their bytes (a `HashSet` of enum
constants is serialized, and the cached locations are saved in `HashSet` order), so replace the whole fixture
set, here and in `:Zmanim`, from a single run. `UT_UpgradeFixturesAgree` in `:Zmanim` checks that the two copies
of `files/` are identical.
