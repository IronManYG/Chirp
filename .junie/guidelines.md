Chirp – Advanced Developer Guidelines

This document captures project-specific development notes to streamline onboarding, builds, testing, and ongoing maintenance for Chirp (Kotlin Multiplatform + Compose Multiplatform).

Scope
- Audience: experienced Kotlin/KMP/Compose developers.
- Focus: only project-specific details that are non-obvious or easy to forget.

1) Build and Configuration
- Toolchain
  - JDK: Java 11. The project configures Java 11 toolchains; ensure your IDE uses JDK 11 for Gradle.
  - Kotlin: 2.2.0 (see gradle/libs.versions.toml).
  - AGP: 8.11.1; Compose Multiplatform: 1.9.0-beta01; Compose BOM: 2025.07.00.
- Gradle
  - Use the wrapper: ./gradlew (macOS/Linux) or .\gradlew.bat (Windows).
  - Root build.gradle.kts only declares plugins via version catalog; module-level build files control targets and dependencies.
  - Custom build-logic: present but minimal; no custom tasks exposed at time of writing.
- Android SDK
  - Min SDK 26; Target/Compile SDK 36. Ensure Android Studio installs platform 36 + matching build tools.
  - local.properties is auto-managed by Android Studio; do not commit secrets.
- iOS
  - Frameworks built from composeApp targets. iosApp contains the Xcode project wrapper to run on Simulator/device.
- Desktop (JVM)
  - composeApp houses the desktop entry point dev.gaddal.chirp.MainKt; packaging via :composeApp:packageDistributionForCurrentOS.

Useful commands
- Build all: ./gradlew build (Windows: .\gradlew.bat build)
- Clean: ./gradlew clean
- Android (debug APK): ./gradlew :composeApp:assembleDebug
- Desktop run: ./gradlew :composeApp:run
- Desktop distribution: ./gradlew :composeApp:packageDistributionForCurrentOS
- iOS: Use Xcode with iosApp; KMP frameworks produced by composeApp.

2) Testing
KMP + Android testing in this repo uses non-default source set names created by the Android Kotlin Multiplatform plugin in each Android-enabled module.

Source sets present in modules (examples under core/* and feature/*):
- androidHostTest — JVM tests that run on the developer machine (no device/emulator). Path pattern:
  - <module>/src/androidHostTest/kotlin/…
- androidDeviceTest — Instrumented/device tests (run on an emulator/physical device). Path pattern:
  - <module>/src/androidDeviceTest/kotlin/…
- iOS tests — Native tests for iOS targets (e.g., iosX64Test) are task-driven; no test sources are currently included beyond templates.

Per-module verification tasks (discoverable via `:module:tasks`)
- Host JVM tests (Android): :<module>:testAndroidHostTest
- Device/instrumented tests (Android): :<module>:connectedAndroidTest (aggregator) and :<module>:connectedAndroidDeviceTest / :<module>:deviceAndroidTest, depending on configuration
- iOS simulator tests: :<module>:iosX64Test, :<module>:iosSimulatorArm64Test (will only run if there are tests)
- Aggregate: :<module>:allTests runs all available tests across targets for that module

Global test runs
- All host tests across modules: ./gradlew test (will depend on each module’s wiring)
- All tests for a specific module (example): ./gradlew :core:domain:testAndroidHostTest

Verified example (executed during guideline authoring)
- Command executed: .\gradlew.bat :core:domain:testAndroidHostTest
- Result: successful compilation and execution on Windows with JDK 11. This validated the androidHostTest pipeline for module core:domain.

Adding tests
- Host (recommended for quick feedback):
  1) Create a file in src/androidHostTest/kotlin under the target module, e.g., core/domain:
     core/domain/src/androidHostTest/kotlin/dev/gaddal/core/domain/MySampleTest.kt
  2) Use kotlin.test or JUnit4 annotations:
     - kotlin.test.Test + kotlin.test.assertEquals
     - or org.junit.Test + org.junit.Assert.assertEquals (see dependency notes)
  3) Run: ./gradlew :<module>:testAndroidHostTest

- Device/instrumented (when you need Android framework APIs):
  1) Place tests under src/androidDeviceTest/kotlin.
  2) Ensure a device/emulator is connected.
  3) Run: ./gradlew :<module>:connectedAndroidTest

Dependency notes for tests
- Host JVM tests may need the Kotlin test library (and optionally the JUnit mapping):
  - In the module’s kotlin { sourceSets { … } } block, ensure:
    val androidHostTest by getting {
      dependencies {
        implementation(kotlin("test"))
        implementation(kotlin("test-junit")) // maps kotlin.test to JUnit4 on JVM
      }
    }
- If you prefer pure JUnit APIs, also add junit:junit if needed. A catalog alias for classic JUnit is not defined by default; kotlin("test-junit") is generally sufficient.
- Instrumented tests can use AndroidX test libraries. The version catalog already defines:
  - libs.androidx.junit (androidx.test.ext:junit)
  - libs.androidx.runner (androidx.test:runner)
  - libs.androidx.test.core
  Add these to the appropriate source set if/when you create instrumented tests.

Creating and running a simple test (walkthrough)
- Example (host test using kotlin.test):
  package dev.gaddal.core.domain
  import kotlin.test.Test
  import kotlin.test.assertEquals
  class MathTest {
    @Test fun addition() { assertEquals(4, 2 + 2) }
  }
- Place it at core/domain/src/androidHostTest/kotlin/dev/gaddal/core/domain/MathTest.kt.
- Run just this source set for the module:
  ./gradlew :core:domain:testAndroidHostTest

Troubleshooting test setup
- Unresolved kotlin.test imports in androidHostTest
  - Ensure kotlin("test") and kotlin("test-junit") are in androidHostTest dependencies as shown above.
- Task not found errors
  - Run `./gradlew :<module>:tasks` and look for Verification tasks. The Android KMP plugin exposes tasks like testAndroidHostTest, connectedAndroidTest, allTests, iosX64Test, etc.
- Device tests failing to start
  - Verify Android SDK/platform 36, emulator/device availability, and instrumentation runner set via withDeviceTestBuilder (e.g., androidx.test.runner.AndroidJUnitRunner).

3) Additional Development Information
- Code style and structure
  - Kotlin DSL for Gradle; prefer version catalog (libs.*) for dependencies.
  - KMP source sets: commonMain drives shared APIs; androidMain/iosMain add platform specifics.
  - UI via Compose Multiplatform; desktop entry: dev.gaddal.chirp.MainKt.
- DI
  - Koin in use, pulled through a BOM (libs.koin-bom). Prefer using the BOM + module-specific artifacts.
- Networking
  - Ktor client declared; configure environment endpoints via BuildKonfig or a secure mechanism once APIs are integrated (no hard-coded secrets).
- Persistence
  - Room declared in versions; the database module exists but is minimally wired; migration/testing strategy should be added as features are implemented.
- Versions alignment and pitfalls
  - Compose Multiplatform and navigation versions must stay aligned (see libs.versions.toml comments).
  - Keep Kotlin, Compose, and AGP versions compatible; upgrading Kotlin often requires updating Compose compiler plugin.
- iOS
  - For Xcode integration, rely on Gradle-produced frameworks. If build fails due to missing signing/team settings, adjust iosApp project settings locally; do not commit personal provisioning info.
- Lint and quality
  - Android Lint plugin applied (com.android.lint). Use :<module>:lint and :<module>:lintFix.
- Build performance
  - Prefer configuration cache and Gradle build cache where feasible. Avoid dynamic versions; the catalog pins versions.

Appendix – Quick references
- List module tasks: ./gradlew :<module>:tasks
- Run all tests: ./gradlew test
- Run a single module host tests: ./gradlew :core:domain:testAndroidHostTest
- Build distributable desktop app: ./gradlew :composeApp:packageDistributionForCurrentOS

File stewardship
- This document is the only artifact added for the guidelines task. If you temporarily add test files while experimenting, remove them before committing long-term changes unless they serve a clear purpose.
