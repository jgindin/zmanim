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

# AboutScreenTest opens the About screen the way the navigation drawer does, through About's constructor and show(),
#  which R8 may inline into the drawer's code. They keep their names too, so that the test's call does not depend on
#  the mapping AGP applies to the test APK.
-keep class com.gindin.zehutyosef.About {
  <init>();
  void show(android.content.Context);
}
