# Chirp

A Kotlin Multiplatform (KMP) application targeting Android and iOS using Compose Multiplatform. The repository is organized as a multi-module project with shared core modules and feature modules.

This README covers the stack, requirements, setup, run/build/test commands, useful Gradle scripts/tasks, environment variables, project structure, and licensing. Unknowns are explicitly marked as TODO.

## Overview
- Platforms: Android, iOS
  - Note: Desktop/JVM target is not currently configured in composeApp (no jvm/desktop source set or entry point found). See TODO in Run/Build.
- UI: JetBrains Compose Multiplatform
- Language: Kotlin (KMP)
- Dependency Injection: Koin
- Networking: Ktor Client
- Persistence: Room (with SQLite bundled) and AndroidX DataStore
- Concurrency/Time: kotlinx-coroutines, kotlinx-datetime
- Images: Coil 3
- Permissions (KMP): moko-permissions

Key toolchain versions (from gradle/libs.versions.toml):
- Kotlin: 2.2.0
- Compose Multiplatform: 1.9.0-beta01 (Compose BOM 2025.07.00)
- Android Gradle Plugin (AGP): 8.11.1
- Min SDK: 26, Target/Compile SDK: 36

## Requirements
- JDK 17 (Android target is configured with JVM target 17 via build-logic)
- Gradle Wrapper (included)
  - Windows examples use .\gradlew.bat; on macOS/Linux use ./gradlew
- Android Studio (latest stable) with Android SDK Platform 36 and matching build tools
- Xcode (for building/running iOS)

Notes
- On non-macOS machines, iOS Kotlin/Native targets will be disabled during configuration. This is expected.

## Setup
1. Clone the repository.
2. Open in Android Studio (recommended) or IntelliJ IDEA with KMP support.
3. Let Gradle sync and download dependencies.
4. Ensure local Android SDK is configured (Android Studio manages local.properties).
5. For iOS, open iosApp/iosApp.xcodeproj in Xcode when running on macOS.

## Run / Build
The application module is composeApp.

Android (assemble debug APK)
- Windows: .\gradlew.bat :composeApp:assembleDebug
- macOS/Linux: ./gradlew :composeApp:assembleDebug

Android (install/run on connected device or emulator)
- Windows: .\gradlew.bat :composeApp:installDebug
- Then launch from the device/emulator apps list.

iOS
- Open iosApp in Xcode and run on Simulator or device. Frameworks are produced from composeApp iOS targets.
- On non-macOS hosts, iOS targets are disabled at configuration time (expected and harmless).

Desktop/JVM
- TODO: Desktop run task (:composeApp:run) and entry point (e.g., dev.gaddal.chirp.MainKt) are not present. If Desktop support is desired, add a JVM/desktop target and entry point and document commands here.

Clean / Build all
- Windows: .\gradlew.bat clean build
- macOS/Linux: ./gradlew clean build

## Useful Gradle tasks (scripts)
Root/common tasks
- :clean — cleans the build
- :build — builds all modules
- :test — runs unit tests across modules

composeApp
- :composeApp:assembleDebug — builds Android debug APK
- :composeApp:assembleRelease — builds Android release APK (signing must be configured locally)

Testing tasks (see Tests section for details)
- :<module>:testDebugUnitTest — Android JVM unit tests (debug variant)
- :<module>:testReleaseUnitTest — Android JVM unit tests (release variant)
- :<module>:connectedDebugAndroidTest — instrumented tests on device/emulator (if configured)

Build logic
- Convention plugins live under build-logic/convention and are applied via the version catalog.
  - KmpLibraryConventionPlugin — sets up KMP library modules with Android + iOS targets, serialization, and common test deps.
  - CmpApplicationConventionPlugin — sets up the app module with Android + iOS targets and Compose plugins.
  - RoomConventionPlugin — wires Room (runtime/sqlite-bundled) and KSP for Android/iOS; schemas at <module>/schemas.

## Environment variables and configuration
No required environment variables are defined in the repo at this time.

Potential future configuration
- API endpoints/keys for Ktor — consider using BuildKonfig or a secure mechanism for non-secret config; avoid committing secrets.
- Android release signing — configure locally, do not commit keystores.

TODO
- Document any actual environment variables or service credentials once integrated.

## Tests
Global unit tests
- Windows: .\gradlew.bat test
- macOS/Linux: ./gradlew test

Module-specific Android JVM unit tests
- Debug: .\gradlew.bat :<module>:testDebugUnitTest
- Release: .\gradlew.bat :<module>:testReleaseUnitTest

Instrumented/device tests (when configured for a module)
- Windows: .\gradlew.bat :<module>:connectedDebugAndroidTest

iOS tests
- Tasks like :<module>:iosX64Test or :<module>:iosSimulatorArm64Test will run only on macOS with proper toolchains.

Notes
- The KMP library convention adds commonTestImplementation(kotlin("test")). AGP maps kotlin.test to JUnit on Android unit tests.

## Project structure
Modules (from settings.gradle.kts)
- composeApp — KMP application module (Android + iOS)
- core
  - core:presentation
  - core:domain
  - core:data
  - core:designsystem
- feature:auth
  - feature:auth:presentation
  - feature:auth:domain
- feature:chat
  - feature:chat:presentation
  - feature:chat:domain
  - feature:chat:data
  - feature:chat:database
- iosApp — Xcode project wrapper to run the iOS app

Notable source locations
- composeApp/src/commonMain/kotlin — shared UI/logic
- composeApp/src/androidMain — Android sources and AndroidManifest.xml
- composeApp/src/iosMain — iOS Kotlin code and interop
- iosApp/ — Swift/SwiftUI wrapper and app entry for iOS

Build and configuration conventions
- Plugins are declared via the version catalog in the root build.gradle.kts; modules apply conventions per-need.
- Reusable convention plugins: build-logic/convention (applied via aliases from the catalog).

## Multilingual Support
The app includes multilingual support with English and Arabic localization, including RTL layout handling. The implementation is documented in core/MultilingualSupport.md.

Current implementation
- ✅ Expect/actual pattern for platform-specific language switching (Android, iOS)
- ✅ LanguageManager class for centralized state management with validation
- ✅ RTL layout direction support for Arabic
- ✅ String resources in commonMain/composeResources (values/strings.xml, values-ar/strings.xml)
- ✅ Demo example in core:presentation (MultilingualExampleDemo)

Pending enhancements (TODO)
1. **Desktop/JVM support** — Add actual fun changeLanguage() implementation in desktopMain source set
   - Location: core/presentation/src/desktopMain/kotlin/dev/gaddal/core/presentation/util/Language.desktop.kt
   - Implementation: Use Locale.setDefault(Locale.of(languageCode)) or Locale(languageCode)

2. **Preference persistence** — Save and load user's language choice across app restarts
   - Recommended library: Multiplatform Settings (com.russhwolf:multiplatform-settings)
   - Provides common API backed by SharedPreferences (Android) and NSUserDefaults (iOS)
   - Store key: "app_language", default: "en"
   - Integration: Add saveLanguage() and loadLanguage() functions, call from LanguageManager

3. **Initialization logic** — Load saved language on app startup with system locale fallback
   - Create an initialization function that:
     - Loads saved language preference (if exists)
     - Falls back to system locale if no saved preference
     - Falls back to default language ("en") if system locale not supported
   - Call from App composable before creating LanguageManager
   - Example: val initialLang = loadSavedLanguage() ?: getSystemLanguage() ?: "en"

For detailed implementation guidance, see core/MultilingualSupport.md.

## License
No LICENSE file found in the repository.

TODO
- Add a LICENSE file (e.g., Apache-2.0 or MIT) and update this section.

## Changelog
See CHANGELOG.md for notable changes.
