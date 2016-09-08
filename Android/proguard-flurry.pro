##
# Copied from https://github.com/krschultz/android-proguard-snippets
#   Version: c636c6c8448565e91e25a66f11fab3fca035c29c (Feb 27, 2016)
#
# Then updated from https://github.com/flurry/flurry-adapter-mopub-android
##


## Flurry 6.5.0 specific rules ##

-keep class com.flurry.** { *; }
-dontwarn com.flurry.**
-keepattributes *Annotation*,EnclosingMethod,Signature
-keepclasseswithmembers class * {
   public <init>(android.content.Context, android.util.AttributeSet, int);
}

