# Chirp

A Kotlin Multiplatform (KMP) application targeting Android and iOS using Compose Multiplatform. The repository is organized as a multi-module project with shared core modules and feature modules.

This README covers the stack, requirements, setup, run/build/test commands, useful Gradle tasks, environment variables, project structure, and licensing. Unknowns are explicitly marked as TODO.

## Overview
- Platforms: Android, iOS
  - Note: Desktop/JVM target is not configured in `composeApp` (no desktop source set or entry point found). See TODO in Run/Build.
- UI: JetBrains Compose Multiplatform
- Language: Kotlin (KMP)
- Dependency Injection: Koin
- Networking: Ktor Client
- Persistence: Room (with SQLite bundled) and AndroidX DataStore
- Concurrency/Time: kotlinx-coroutines, kotlinx-datetime
- Images: Coil 3
- Permissions (KMP): moko-permissions

Key toolchain versions (from `gradle/libs.versions.toml`):
- Kotlin: 2.2.0
- Compose Multiplatform: 1.9.0-beta01 (Compose BOM 2025.07.00)
- Android Gradle Plugin (AGP): 8.13.0
- KSP: 2.2.0-2.0.2
- Room: 2.7.2; SQLite bundle: 2.5.2
- Min SDK: 26, Target/Compile SDK: 36

## Requirements
- JDK: Java 17. Ensure your IDE and Gradle toolchain use JDK 17.
- Gradle Wrapper (included)
  - On Windows use `.\gradlew.bat`; on macOS/Linux use `./gradlew`.
- Android Studio with Android SDK Platform 36 and matching build tools
- Xcode (for building/running iOS on macOS)

Notes
- On non‑macOS machines, iOS Kotlin/Native targets will be disabled during configuration. This is expected and harmless for Android work.

## Setup
1. Clone the repository.
2. Open in Android Studio (recommended) or IntelliJ IDEA with KMP support.
3. Let Gradle sync and download dependencies.
4. Ensure local Android SDK is configured (Android Studio manages `local.properties`).
5. For iOS, open `iosApp/iosApp.xcodeproj` in Xcode when running on macOS.

## Entry points
- Android: `composeApp/src/androidMain/kotlin/dev/gaddal/chirp/MainActivity.kt` hosts the `App()` composable.
- Shared UI root: `composeApp/src/commonMain/kotlin/dev/gaddal/chirp/App.kt`.
- iOS: Kotlin frameworks are produced from `composeApp`; the `iosApp` Xcode project wraps and launches the UI.

## Run / Build
The application module is `:composeApp`.

Android (assemble debug APK)
- Windows: `.\gradlew.bat :composeApp:assembleDebug`
- macOS/Linux: `./gradlew :composeApp:assembleDebug`

Android (install/run on connected device or emulator)
- Windows: `.\gradlew.bat :composeApp:installDebug`
- Then launch from the device/emulator apps list.

iOS
- Open `iosApp` in Xcode and run on Simulator or device. Frameworks are produced from `composeApp` iOS targets.
- On non‑macOS hosts, iOS targets are disabled at configuration time (expected and harmless).

Desktop/JVM
- TODO: No Desktop target/tasks are present (e.g., `:composeApp:run`, `:composeApp:packageDistributionForCurrentOS`). If Desktop support is desired, add a JVM/desktop target and entry point and document commands here.

Clean / Build all
- Windows: `.\gradlew.bat clean build`
- macOS/Linux: `./gradlew clean build`

## Useful Gradle tasks
Root/common tasks
- `:clean` — cleans the build
- `:build` — builds all modules
- `:test` — runs unit tests across modules

composeApp
- `:composeApp:assembleDebug` — builds Android debug APK
- `:composeApp:assembleRelease` — builds Android release APK (signing must be configured locally)

Testing tasks (see Tests section for details)
- `:<module>:testDebugUnitTest` — Android JVM unit tests (debug variant)
- `:<module>:testReleaseUnitTest` — Android JVM unit tests (release variant)
- `:<module>:connectedDebugAndroidTest` — instrumented tests on device/emulator (if configured)

Build logic
Convention plugins live under `build-logic/convention` and are applied via the version catalog (see `gradle/libs.versions.toml`). Relevant plugin IDs registered in `build-logic`:
- `dev.gaddal.convention.android.application` — Android app basics (namespace, packaging, build types, versioning from the catalog); delegates to shared Kotlin/Android config.
- `dev.gaddal.convention.android.application.compose` — Adds Compose build features and BOM/tooling to Android app modules.
- `dev.gaddal.convention.cmp.application` — KMP app with Android + iOS targets, Compose plugins; Android target JVM 17; iOS targets configured as static frameworks.
- `dev.gaddal.convention.kmp.library` — KMP library with Android + iOS targets, Kotlin serialization, common test deps; sets Android `resourcePrefix` and enables Android resources in KMP CLI builds; configures compiler opt-ins.
- `dev.gaddal.convention.cmp.library` — Adds Compose deps/plugins for KMP libraries (UI, Foundation, Material3, Material Icons); debug tooling on Android.
- `dev.gaddal.convention.cmp.feature` — Convenience plugin to set up a Compose-enabled KMP feature library (composes `kmp.library` + Compose plugins/deps).
- `dev.gaddal.convention.buildkonfig` — Wires BuildKonfig with package name derived from project path and expects an `API_KEY` in `local.properties`.
- `dev.gaddal.convention.room` — Wires Room + KSP across Android/iOS (runtime + sqlite-bundled; KSP for Android and iOS targets); sets `schemas` dir per module.

Notable conventions and helpers
- Android/Kotlin settings: Java/Kotlin toolchains target JVM 17; core library desugaring enabled with `android-desugarJdkLibs`.
- KMP Android target: `jvmTarget = 17` via `configureAndroidTarget`.
- KMP compiler flags: `-Xexpect-actual-classes`, opt-ins for `kotlin.RequiresOptIn` and `kotlin.time.ExperimentalTime`.
- Namespaces/resource prefixes: library modules derive `namespace` and `resourcePrefix` from Gradle path using helpers in `PathUtil.kt`.
  - `pathToPackageName()` converts `:core:domain` -> `dev.gaddal.core.domain`.
  - `pathToResourcePrefix()` converts `:feature:chat:data` -> `feature_chat_data_`.
- iOS framework names:
  - App plugin (`cmp.application`): static frameworks with baseName `ComposeApp`.
  - Library plugin (`kmp.library`): baseName derived from module path via `pathToFrameworkName()` (camel-cased).
- Compose BOM/tooling: app and Compose-enabled modules import the BOM; debug-only UI tooling and previews added for Android.

## Environment variables and configuration
No global environment variables are required for a basic build.

BuildKonfig (per-module, optional)
- Some modules apply `dev.gaddal.convention.buildkonfig`. When applied, you MUST define `API_KEY` in `local.properties` at the repo root, or the build will fail.
  - Example entry in `local.properties` (do not commit this file):
    - `API_KEY=your_value_here`
  - Generated package name for BuildKonfig is derived from the module path via `pathToPackageName()`.

Potential future configuration
- API endpoints/keys for Ktor — consider using BuildKonfig or another secure mechanism for non‑secret config; avoid committing secrets.
- Android release signing — configure locally, do not commit keystores.

TODO
- Document any additional environment variables or service credentials once integrated.

## Tests
Global unit tests
- Windows: `.\gradlew.bat test`
- macOS/Linux: `./gradlew test`

Module‑specific Android JVM unit tests
- Debug: `.\\gradlew.bat :<module>:testDebugUnitTest`
- Release: `.\\gradlew.bat :<module>:testReleaseUnitTest`

Instrumented/device tests (when configured for a module)
- Windows: `.\\gradlew.bat :<module>:connectedDebugAndroidTest`

iOS tests
- Tasks like `:<module>:iosX64Test` or `:<module>:iosSimulatorArm64Test` will run only on macOS with proper toolchains.

Notes
- The KMP library convention adds `commonTestImplementation(kotlin("test"))`. AGP maps `kotlin.test` to JUnit on Android unit tests.
- Some modules may still use legacy `src/test/kotlin`; Gradle may print a deprecation notice recommending `src/androidUnitTest/kotlin`. Prefer the new path going forward.

## Project structure
Modules (from `settings.gradle.kts`)
- `composeApp` — KMP application module (Android + iOS)
- core
  - `core:presentation`
  - `core:domain`
  - `core:data`
  - `core:designsystem`
- feature:auth
  - `feature:auth:presentation`
  - `feature:auth:domain`
- feature:chat
  - `feature:chat:presentation`
  - `feature:chat:domain`
  - `feature:chat:data`
  - `feature:chat:database`
- `iosApp` — Xcode project wrapper to run the iOS app

Notable source locations
- `composeApp/src/commonMain/kotlin` — shared UI/logic
- `composeApp/src/androidMain` — Android sources and `AndroidManifest.xml`
- `composeApp/src/iosMain` — iOS Kotlin code and interop
- `iosApp/` — Swift/SwiftUI wrapper and app entry for iOS

Build and configuration conventions
- Plugins are declared via the version catalog in the root `build.gradle.kts`; modules apply conventions per need.
- Reusable convention plugins: `build-logic/convention` (applied via aliases from the catalog).

## Multilingual support
The app includes multilingual support with English and Arabic localization, including RTL layout handling.

Current implementation
- Expect/actual pattern for platform‑specific language switching (Android, iOS)
- `LanguageManager` class for centralized state management with validation
- RTL layout direction support for Arabic
- String resources in `commonMain/composeResources` (values/strings.xml, values-ar/strings.xml)
- Example usage in `composeApp/src/commonMain/kotlin/dev/gaddal/chirp/App.kt`

TODO
- Add developer documentation for multilingual support (e.g., `core/MultilingualSupport.md`) and link it here.
- Persist user language preference across app restarts (e.g., Multiplatform Settings), then update this section with details.

## License
No LICENSE file found in the repository.

TODO
- Add a LICENSE file (e.g., Apache‑2.0 or MIT) and update this section.

## Changelog
See `CHANGELOG.md` for notable changes.
