# R8 rules for the :ZehutYosef release builds (both flavors).
#
# There are no keep rules here because nothing in this app needs one beyond what the AGP default
#  file (proguard-android-optimize.txt), the aapt2-generated manifest/resource rules and the
#  libraries' own consumer rules already provide. Checked, and why each needs nothing:
#  - Android-Iconics: the drawer icons are passed as IIcon enum constants (FontAwesome.Icon,
#    GoogleMaterial.Icon), which resolve their typeface directly. Only string-keyed lookups such as
#    "{gmd-help}" go through Iconics.init(), which finds fonts by reflecting over <packageName>.R$string;
#    neither this app nor MaterialDrawer/AboutLibraries uses such keys, and that lookup already finds
#    nothing in the barMitzvah flavor, whose package name differs from the R class's namespace.
#  - AboutLibraries: About passes withFields(...) and withAutoDetect(false), so it never reflects over
#    R$string or loads library classes by name. It reads the library_*/license_* strings through
#    getIdentifier(); the resource shrinker's safe mode keeps all of them (the same 1681 define_/
#    library_/license_ strings are in the shrunk APK as in an unshrunk one).
#  - About.LibraryComparator is Serializable only because LibsBuilder carries it through an Intent to
#    LibsActivity in the same process, where the renamed class name is the same on both ends.
#  - Assets (section texts, fonts) are opened by literal paths, never by class names.
