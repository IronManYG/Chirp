Chirp – Advanced Developer Guidelines (Project-Specific)

Scope
- Audience: experienced Kotlin/KMP/Compose developers.
- Focus: only project-specific details that are non-obvious or easy to forget.
- Validated on Windows with JDK 17 using Gradle 8.14.3 on 2025‑10‑16.

1) Build and Configuration
Toolchain and versions
- JDK: Java 17. Ensure your IDE and Gradle toolchain use JDK 17.
- Kotlin: 2.2.0 (gradle/libs.versions.toml -> versions.kotlin).
- AGP: 8.11.1.
- Compose Multiplatform: 1.9.0‑beta01. Compose BOM: 2025.07.00.
- KSP: 2.2.0‑2.0.2.
- Room: 2.7.2; SQLite bundle: 2.5.2.

Gradle usage
- Always use the wrapper from project root.
  - Windows examples:
    - .\gradlew.bat build
    - .\gradlew.bat :composeApp:run
    - .\gradlew.bat :composeApp:packageDistributionForCurrentOS
- Root build.gradle.kts declares plugins via the version catalog only; per‑module build files control targets and deps.
- Custom build logic lives under build-logic/convention and applies conventions via aliases in the version catalog.

Android SDK
- minSdk 26; target/compile SDK 36 (see libs.versions.toml for projectTargetSdkVersion/projectCompileSdkVersion).
- Ensure Android Studio has platform 36 + matching build tools installed.
- local.properties is managed by Android Studio; no secrets should be committed.

iOS
- iOS targets are configured in KMP; frameworks are produced from composeApp for Xcode (iosApp wrapper in repo).
- On non‑macOS machines, iOS targets will be disabled during configuration. Expect messages like “Disabled Kotlin/Native Targets … iosArm64, iosSimulatorArm64, iosX64”. This is expected and harmless for Android/Desktop work.

Desktop (JVM)
- Entry point: composeApp -> dev.gaddal.chirp.MainKt.
- Packaging: :composeApp:packageDistributionForCurrentOS.

Conventions in build-logic worth remembering
- KmpLibraryConventionPlugin adds:
  - Kotlin Multiplatform + Android library + Kotlin serialization plugins.
  - Dependencies: commonMainImplementation(kotlinx-serialization-json), commonTestImplementation(kotlin-test).
  - Android experimental property to enable Android resources in KMP command-line builds.
- RoomConventionPlugin (apply when a module uses Room) wires:
  - Plugins: com.google.devtools.ksp, androidx.room.
  - Room extension schemaDirectory to <module>/schemas.
  - Adds commonMainApi Room runtime + sqlite-bundled, and KSP deps for Android + iOS targets.

Useful commands
- Build all: .\gradlew.bat build
- Clean: .\gradlew.bat clean
- Android (debug APK for app module): .\gradlew.bat :composeApp:assembleDebug
- Desktop run: .\gradlew.bat :composeApp:run
- Desktop distribution: .\gradlew.bat :composeApp:packageDistributionForCurrentOS

2) Testing
Important: Actual tasks and source set names in this repository
- Host JVM unit tests for Android targets are exposed via standard AGP unit test tasks, not the androidHostTest task name seen in some KMP setups.
- Verified tasks under a typical library module (example: :core:domain):
  - :core:domain:testDebugUnitTest — runs local JVM unit tests for debug.
  - :core:domain:testReleaseUnitTest — runs local JVM unit tests for release.
  - :core:domain:test — aggregator for unit tests across variants.
  - :core:domain:connectedDebugAndroidTest / connectedAndroidTest — instrumented/device tests.
  - K/N iOS tests (e.g., :core:domain:iosX64Test) will only run on macOS with the corresponding toolchain.

Source set locations (Android tests)
- Local JVM unit tests (recommended for quick feedback):
  - Prefer new KMP Android source set layout: <module>/src/androidUnitTest/kotlin/…
  - Legacy Android style directory <module>/src/test/kotlin is still recognized but emits a deprecation warning. Migration hint is printed by Gradle; recommended to use androidUnitTest.
- Instrumented tests (run on device/emulator):
  - <module>/src/androidAndroidTest/kotlin or standard <module>/src/androidTest/kotlin depending on AGP/KMP wiring. The tasks listed above resolve to connectedDebugAndroidTest.

Dependencies for tests
- The KMP library convention already adds commonTestImplementation(kotlin("test")) in modules applying it.
- If using org.junit APIs directly in Android unit tests, AGP provides the mapping from kotlin.test to JUnit4. You can additionally add junit:junit if you need pure JUnit features, but kotlin("test") is usually sufficient.

Creating and running a simple test (validated walkthrough)
- Example (local JVM unit test using kotlin.test with JUnit runner):
  package dev.gaddal.core.domain
  import org.junit.Test
  import kotlin.test.assertEquals
  class MathTestJvm {
    @Test fun addition() { assertEquals(4, 2 + 2) }
  }
- Place it at: core/domain/src/androidUnitTest/kotlin/dev/gaddal/core/domain/MathTestJvm.kt
- Run just this module’s debug unit tests:
  .\gradlew.bat :core:domain:testDebugUnitTest
- Notes from validation on Windows:
  - Running :core:domain:testDebugUnitTest compiled and executed successfully.
  - If you place tests under legacy src/test/kotlin, Gradle prints a deprecation notice recommending src/androidUnitTest/kotlin. Prefer the new path going forward.

Global/aggregate test runs
- All unit tests across modules: .\gradlew.bat test
- All tests for a specific module (unit + device, if configured): .\gradlew.bat :<module>:test and connectedAndroidTest accordingly.

Instrumented/device tests
- Ensure a device/emulator is connected.
- Run: .\gradlew.bat :<module>:connectedDebugAndroidTest (or :<module>:connectedAndroidTest for an aggregate).
- Instrumentation runner is provided by the AndroidX Test libs if/when used; wire dependencies in the corresponding source set when you add such tests (libs.androidx.junit, libs.androidx.runner, libs.androidx.test.core are defined in the catalog).

iOS tests
- Tasks like :<module>:iosX64Test and :<module>:iosSimulatorArm64Test will only run on macOS with proper Xcode toolchains. On Windows/Linux these targets are disabled at configuration time; this is expected.

Troubleshooting (observed and likely scenarios)
- “Cannot locate tasks that match ':<module>:testAndroidHostTest'”:
  - This repository exposes Android unit tests via standard AGP tasks (testDebugUnitTest/testReleaseUnitTest), not testAndroidHostTest. Use the tasks listed above.
- “Deprecated 'Android Style' Source Directory … src/test/kotlin … use src/androidUnitTest/kotlin”: 
  - Move local unit tests to src/androidUnitTest/kotlin to silence the warning and align with KMP Android source set layout V2.
  - To temporarily suppress the warning: add to gradle.properties: kotlin.mpp.androidSourceSetLayoutV2AndroidStyleDirs.nowarn=true
- “Disabled Kotlin/Native Targets … iosArm64/iosSimulatorArm64/iosX64”:
  - Expected on non‑macOS machines. Ignore for Android/Desktop workflows. On macOS, ensure Xcode + command line tools are installed.
- Opt‑in warnings like “kotlinx.coroutines.ExperimentalCoroutinesApi is unresolved” during tests:
  - Ensure the module declares the appropriate kotlinx-coroutines dependency and opt‑ins where necessary. For unit tests that do not use coroutines APIs directly, these warnings are harmless.

3) Additional Development Information
Code style and structure
- Kotlin DSL for Gradle with a version catalog (libs.*). Prefer catalog aliases for dependencies and plugins.
- KMP source sets: commonMain drives APIs; androidMain/iosMain add platform specifics.
- UI via Compose Multiplatform; Desktop entry: dev.gaddal.chirp.MainKt.

Dependency Injection
- Koin is included; use the BOM pattern (libs.koin-*) where applicable. Centralize DI modules in shared code where possible.

Networking
- Ktor client declared in the catalog. Use BuildKonfig or another secure mechanism for environment endpoints; avoid hard-coded secrets.

Persistence
- Room + KSP are supported through the RoomConventionPlugin. When enabling Room in a module, apply the convention plugin (alias in the catalog) so that:
  - KSP processors are wired for Android and iOS targets.
  - Schemas are generated under <module>/schemas. Commit schemas if you need migration history.

Versions alignment and pitfalls
- Keep Kotlin, Compose Multiplatform, and AGP versions aligned. Upgrading Kotlin typically requires updating the Compose compiler plugin.
- Compose navigation must be kept aligned with Compose MPP (see comments in libs.versions.toml).

iOS integration
- Use Xcode with iosApp; frameworks are produced by composeApp. If build fails due to signing/team settings, adjust locally in iosApp — do not commit personal provisioning.

Lint and quality
- Android Lint plugin applied. Use :<module>:lint and :<module>:lintFix.

Build performance
- Enable configuration cache and Gradle build cache where appropriate. Versions are pinned in the catalog; avoid dynamic versions.

Appendix – Quick references
- List module tasks: .\gradlew.bat :<module>:tasks
- Build all: .\gradlew.bat build
- Run all unit tests: .\gradlew.bat test
- Run a single module’s unit tests (debug): .\gradlew.bat :core:domain:testDebugUnitTest
- Package desktop app: .\gradlew.bat :composeApp:packageDistributionForCurrentOS

Validation summary (for this guideline)
- A sample local JVM unit test was created and executed with :core:domain:testDebugUnitTest on Windows; it passed. The sample was removed afterward to keep the repo clean.
- Use src/androidUnitTest/kotlin for future local unit tests to align with the current KMP Android source set layout.
