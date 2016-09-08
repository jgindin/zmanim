##
# Copied from https://github.com/krschultz/android-proguard-snippets
#   Version: c636c6c8448565e91e25a66f11fab3fca035c29c (Feb 27, 2016)
##

-dontwarn android.support.design.**
-keep,includedescriptorclasses class android.support.design.** { *; }
-keep interface android.support.design.** { *; }
-keep public class android.support.design.R$* { *; }