# Usage — Run, Build, Test

This page summarizes common commands for running, building, and testing Chirp across platforms.

All examples use the Gradle Wrapper. On Windows:

```
./gradlew.bat <task>
```

## Build

- Clean + build all modules:
    - `./gradlew.bat clean build`

## Android

- Assemble debug APK:
    - `./gradlew.bat :composeApp:assembleDebug`
- Assemble release (configure signing locally):
    - `./gradlew.bat :composeApp:assembleRelease`
- Bundle AAB (release):
    - `./gradlew.bat :composeApp:bundleRelease`
- Install debug on device/emulator:
    - `./gradlew.bat :composeApp:installDebug`

## iOS (macOS)

- Open `iosApp` in Xcode and run on Simulator or device.
- iOS targets are disabled on non‑macOS hosts at configuration time (expected behavior).

## Desktop (JVM)

- Run Desktop app for current OS:
    - `./gradlew.bat :composeApp:run`
- Package distribution for current OS (Compose Multiplatform):
    - `./gradlew.bat :composeApp:packageDistributionForCurrentOS`

## Useful Gradle Tasks

- Root/common:
    - `:clean` — cleans the build
    - `:build` — builds all modules
    - `:test` — runs unit tests across modules
- composeApp:
    - `:composeApp:lint` / `:composeApp:lintFix`
    - `:composeApp:assembleDebug` / `:composeApp:assembleRelease`
    - `:composeApp:run`
    - `:composeApp:packageDistributionForCurrentOS`
- Per-module listing:
    - `./gradlew.bat :<module>:tasks`

## Tests

- All unit tests:
    - `./gradlew.bat test`
- Android JVM unit tests per module:
    - Debug: `./gradlew.bat :<module>:testDebugUnitTest`
    - Release: `./gradlew.bat :<module>:testReleaseUnitTest`
- Instrumented/device tests (when configured):
    - `./gradlew.bat :<module>:connectedDebugAndroidTest`
- iOS tests (macOS only):
    - Tasks like `:<module>:iosX64Test` or `:<module>:iosSimulatorArm64Test`

Notes:

- The KMP library convention adds `commonTestImplementation(kotlin("test"))`; AGP maps `kotlin.test`
  to JUnit for Android unit tests.
- Prefer placing local JVM unit tests under `src/androidUnitTest/kotlin` to align with the KMP
  Android source set layout V2.
