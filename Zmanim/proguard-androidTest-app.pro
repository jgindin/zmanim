# Rules added to the app's own R8 rules in the releaseCheck build type only (see build-logic ReleaseCheck.kt), so
#  that the on-device tests can run against the minified app. Nothing here may keep a name that proguard-rules.pro
#  protects (r8-kept-names.txt lists them): the tests must still fail when one of those rules is lost.

# Libraries the test APK shares with the app. AGP leaves classes the app already contains out of the test APK, so
#  AndroidJUnitRunner, ActivityScenario, Espresso and the tests themselves call the app's minified copies of these
#  libraries, which R8 has stripped down to what the app uses (the runner fails at startup without androidx.tracing
#  and kotlin). allowobfuscation: AGP applies the app's mapping to the test APK, so renamed classes are found.
-keep,allowobfuscation class androidx.tracing.** { *; }
-keep,allowobfuscation class kotlin.** { *; }
-keep,allowobfuscation class androidx.lifecycle.** { *; }
-keep,allowobfuscation class androidx.core.** { *; }
-keep,allowobfuscation class androidx.concurrent.futures.** { *; }
-keep,allowobfuscation class com.google.common.util.concurrent.ListenableFuture { *; }
-keep,allowobfuscation class androidx.recyclerview.widget.RecyclerView { *; }
-keep,allowobfuscation class androidx.recyclerview.widget.RecyclerView$Adapter { *; }

# The app's own code the tests call into to seed state and to drive the real code paths (NotifiedZmanimRestoreTest,
#  ZmanimTypeByNameTest, LocationErrorStatusTest, AppState). R8 inlines or removes these entry points in the app,
#  where nothing else calls them the same way. They keep their names as well: R8 does not record every renamed
#  static Kotlin @JvmStatic method in the mapping that AGP applies to the test APK.
-keep enum com.gindin.zmanim.android.prefs.ZmanimPrefs { *; }
-keep enum com.gindin.zmanim.android.prefs.PersonalPreferences$Prefs { *; }
-keepclassmembers class com.gindin.zmanlib.notification.NotifiedZmanim {
  public static ** manager;
  public boolean contains(com.gindin.zmanlib.zman.Zmanim$Type);
}
-keep class com.gindin.zmanlib.notification.NotifiedZmanim$Manager {
  public <init>();
  public void init(java.io.File);
  public ** get(java.util.Calendar);
}
-keep class com.gindin.zmanim.android.service.PrefsBasedZmanCallback {
  public <init>(android.content.Context);
  public *** howManyMinutesToAlertBefore(...);
  public *** getPreferredZman(...);
}
-keep class com.gindin.zmanlib.ZmanCallback {
  public abstract void scheduleAlert(...);
}
-keep class com.gindin.zmanim.android.service.DisplayNotificationService {
  public <init>();
  public static *** createIntentForService(...);
  public void onReceive(...);
}
-keep class com.gindin.zmanlib.calendar.HebrewCalendar { public static *** getForDate(int, int, int); }
-keep class com.gindin.zmanlib.zman.ZmanimFactory { public static *** get(...); }
-keep class com.gindin.zmanlib.zman.Zmanim { public *** getDefault(); }
-keep class com.gindin.zmanlib.zman.Zman { public *** getTime(); }
-keep class com.gindin.zmanlib.location.ZmanimLocation$Builder { public <init>(); public *; }
-keep class com.gindin.zmanim.android.location.LocationError { public *; }
-keep enum com.gindin.zmanim.android.location.LocationError$Code { *; }
-keep class com.google.android.gms.common.api.Status {
  public <init>(int, java.lang.String, android.app.PendingIntent);
  public int getStatusCode();
  public boolean hasResolution();
}
-keep class com.google.android.gms.common.api.ResolvableApiException {
  public <init>(com.google.android.gms.common.api.Status);
}
