# Installation & Setup

This guide helps you get Chirp running on your machine with the correct toolchain and IDE
configuration.

## Prerequisites

- JDK: Java 17 (ensure your IDE and Gradle toolchain use JDK 17)
- Gradle: Use the wrapper included in the repository
    - Windows: `./gradlew.bat`
    - macOS/Linux: `./gradlew`
- Android Studio with Android SDK Platform 36 and matching build tools
- Xcode (macOS only) for building/running iOS

## Clone and Open

1. Clone the repository.
2. Open in Android Studio (recommended) or IntelliJ IDEA with KMP support.
3. Let Gradle sync and download dependencies.

Notes:

- On non‑macOS machines, iOS Kotlin/Native targets will be disabled during configuration. This is
  expected and harmless for Android/Desktop work.
- `local.properties` is managed by Android Studio; do not commit secrets.

## First Build

Windows examples:

- Clean + build all: `./gradlew.bat clean build`
- List tasks for a module: `./gradlew.bat :composeApp:tasks`

## Android Setup

- Ensure SDK Platform 36 and required build tools are installed in Android Studio.
- Debug APK: `./gradlew.bat :composeApp:assembleDebug`
- Install on device/emulator: `./gradlew.bat :composeApp:installDebug`

## iOS Setup (macOS)

- Open `iosApp` in Xcode and run on Simulator or device.
- Frameworks are produced from `composeApp` iOS targets.

## Desktop (JVM)

- Run: `./gradlew.bat :composeApp:run`
- Package distribution (current OS): `./gradlew.bat :composeApp:packageDistributionForCurrentOS`

## Optional: BuildKonfig

Some modules apply a BuildKonfig convention requiring an `API_KEY` in `local.properties` at the repo
root:

```
API_KEY=your_value_here
```

If missing, the build will fail for those modules. See docs/architecture.md for details.
