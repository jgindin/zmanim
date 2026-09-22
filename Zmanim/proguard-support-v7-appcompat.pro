##
# Copied from https://github.com/krschultz/android-proguard-snippets
#   Version: c636c6c8448565e91e25a66f11fab3fca035c29c (Feb 27, 2016)
##

-keep,includedescriptorclasses public class android.support.v7.widget.** { *; }
-keep,includedescriptorclasses public class android.support.v7.internal.widget.** { *; }
-keep,includedescriptorclasses public class android.support.v7.internal.view.menu.** { *; }
-keep,includedescriptorclasses public class android.support.v7.view.menu.** { *; }

-keep,includedescriptorclasses public class * extends android.support.v4.view.ActionProvider {
    public <init>(android.content.Context);
}
