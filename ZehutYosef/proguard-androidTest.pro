# Rules for R8's pass over the test APK of the releaseCheck build type (testProguardFiles; see build-logic
#  ReleaseCheck.kt). AGP itself keeps every test class and applies the app's mapping in that pass.

# An annotation in Error Prone (pulled in by the AndroidX test libraries) refers to javax.lang.model, which only exists
#  in the JDK's compiler. Nothing reads that annotation at runtime.
-dontwarn javax.lang.model.element.Modifier
