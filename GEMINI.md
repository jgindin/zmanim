# Project Overview

This is an Android project that contains two separate applications: "Zmanim"
and "Zehut Yosef".

**Zmanim:**
The main application is "Zmanim", which displays zmanim (significant Jewish
times of the day). It uses the `kosherjava.zmanim` library for its core zmanim
calculations. It also uses Google Play Services for location to provide zmanim
for the user's current location.

**Zehut Yosef:**
The second application is "Zehut Yosef", which is a siddur (Jewish
prayer book). It has two product flavors: "barMitzvah" and "sidur". It uses the
`MaterialDrawer` library for its user interface.

The project is divided into four modules:

* `Zmanim`: The main "Zmanim" application module.
* `ZehutYosef`: The "Zehut Yosef" application module.
* `ZmanLib`: A Kotlin (JVM/Android) library module that likely contains common
  logic for both applications, wrapping the `kosherjava.zmanim` library.
* `ZmanUtils`: A simple Kotlin Multiplatform library with no external
  dependencies that likely contains utility classes used by the other modules.

The project is built with Gradle and uses Java 21.

# Building and Running

To build the project, you can use the following Gradle command:

```bash
./gradlew build
```

This will build both the "Zmanim" and "Zehut Yosef" applications.

To run the "Zmanim" application, you can install the generated APK file from
`Zmanim/build/outputs/apk/debug/Zmanim--debug-<versionName>.apk` (e.g.
`Zmanim--debug-4.0.402.apk`) on an Android device or emulator.

To run the "Zehut Yosef" application, you can install the generated APK file
from `ZehutYosef/build/outputs/apk/sidur/debug/ZehutYosef-sidur-debug.apk` or
`ZehutYosef/build/outputs/apk/barMitzvah/debug/ZehutYosef-barMitzvah-debug.apk`
on an Android device or emulator.

**Note:** The release builds of both applications require signing keys that are
not included in the repository. You will only be able to build and run the debug
versions of the applications.

# Development Conventions

* The project uses a consistent coding style, with copyright notices at the top
  of each file.
* The project is divided into modules to promote code reuse and separation of
  concerns.
* The project uses a versioning system that is managed by the
  `zmanim.android-application-conventions` convention plugin in `build-logic`
  (see `build-logic/src/main/kotlin/com/gindin/buildlogic/Versioning.kt`).
* The project uses several open-source libraries, which are listed in the
  `build.gradle` files.
* The project uses `AndroidX` libraries for modern Android development.
* The project uses `JUnit` for testing.
