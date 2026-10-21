# R8 rules for the :Zmanim release build.
#
# The AGP default file (proguard-android-optimize.txt), the rules aapt2 generates from the manifest
#  and the layout/preference XML, and the consumer rules shipped inside the AndroidX, Material,
#  Play Services, net.xpece and kotlinx.serialization artifacts already cover those libraries and
#  every class the framework instantiates by name. The rules below cover only what this app's own
#  code reaches by name at runtime, each kept as narrow as the access it protects.
#
# Deliberately left without rules, because nothing looks them up by name:
#  - com.gindin.eventBus: handlers are keyed by java.lang.Class objects (associatedType and
#    event.javaClass), which stay consistent however R8 renames the event classes.
#  - Enum constants used as SharedPreferences keys, Intent extras and Intent actions (Zmanim.Type,
#    PersonalPreferences.Prefs, ZmanimPrefs, ZmanimService.Actions, ...): name() returns the string
#    literal passed to the enum constructor, which R8 never rewrites, and valueOf() is kept by the
#    default rules.
#  - kotlinx.serialization: only the JsonElement tree API is used, with no @Serializable classes.
#  - KosherJava: its only reflection is ZmanimFormatter.toXML()/toJSON(), reached only through
#    AstronomicalCalendar.toString(); renamed getters would change that debug text, nothing else.


# NotifiedZmanim is written with ObjectOutputStream to files in filesDir and read back on the next
#  launches, including by a newer version of the app after an upgrade. The stream records the class
#  name, the serialVersionUID and every instance field's name and type, so all of them must match
#  the source exactly or the restore throws and the user gets notified again about zmanim they
#  were already notified of. The Serializable hook methods are listed so that adding one later
#  doesn't silently break the format.
-keepnames class com.gindin.zmanlib.notification.NotifiedZmanim
-keepclassmembers class com.gindin.zmanlib.notification.NotifiedZmanim {
  static final long serialVersionUID;
  !static !transient <fields>;
  private void writeObject(java.io.ObjectOutputStream);
  private void readObject(java.io.ObjectInputStream);
  java.lang.Object writeReplace();
  java.lang.Object readResolve();
}

# The set inside NotifiedZmanim holds Zmanim.Type constants. ObjectInputStream loads the enum by
#  the class name written in the stream and then looks the constant up by name() through values(),
#  so the class name and values() must survive. The constant fields are kept too, which also stops
#  R8 from unboxing the enum into ints.
-keepnames class com.gindin.zmanlib.zman.Zmanim$Type
-keepclassmembers enum com.gindin.zmanlib.zman.Zmanim$Type {
  public static final com.gindin.zmanlib.zman.Zmanim$Type *;
  public static **[] values();
  public static ** valueOf(java.lang.String);
}

# LocationError reads ApiException's protected mStatus field with getDeclaredField("mStatus"), to
#  carry the Status of a ResolvableApiException through a Bundle. R8 otherwise renames the field
#  and the lookup quietly returns null, losing the location-settings resolution.
-keepclassmembers class com.google.android.gms.common.api.ApiException {
  com.google.android.gms.common.api.Status mStatus;
}
