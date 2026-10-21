# R8 rules for the :ZehutYosef release builds (both flavors).
#
# Beyond the single rule at the end, nothing in this app needs a keep rule beyond what the AGP default
#  file (proguard-android-optimize.txt), the aapt2-generated manifest/resource rules and the
#  libraries' own consumer rules already provide. Checked, and why each needs nothing:
#  - Android-Iconics: the drawer icons are passed as IIcon enum constants (FontAwesome.Icon,
#    GoogleMaterial.Icon), which resolve their typeface directly. Only string-keyed lookups such as
#    "{gmd-help}" go through Iconics.init(), which finds fonts by reflecting over <packageName>.R$string;
#    neither this app nor MaterialDrawer/AboutLibraries uses such keys, and that lookup already finds
#    nothing in the barMitzvah flavor, whose package name differs from the R class's namespace.
#  - AboutLibraries: About passes withFields(...) and withAutoDetect(false), so it never reflects over
#    R$string or loads library classes by name. It reads the library_*/license_* strings through
#    getIdentifier(); src/main/res/raw/keep.xml keeps them explicitly so that does not depend on the
#    shrinker's mode.
#  - About.LibraryComparator is Serializable only because LibsBuilder carries it through an Intent to
#    LibsActivity in the same process, where the renamed class name is the same on both ends.
#  - Assets (section texts, fonts) are opened by literal paths, never by class names.

# The framework recreates a retained or restored platform (android.app) Fragment by loading its class by name
#  and calling its public no-arg constructor, so R8 must neither rename nor strip that constructor. Only the
#  PreferenceFragment subclass in Settings is one; the AndroidX fragments in the libraries ship their own rules.
-keepclassmembers class * extends android.app.Fragment {
  public <init>();
}
