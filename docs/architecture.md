# Architecture

Chirp is a Kotlin Multiplatform (KMP) application with a modular structure. UI is built with Compose
Multiplatform, business logic is shared, and platform specifics are added per target where needed.

## High-Level Design

- UI: Compose Multiplatform (shared UI in `commonMain`, Android/iOS/Desktop specifics in platform
  source sets)
- DI: Koin (BOM via version catalog aliases)
- Networking: Ktor Client
- Persistence: Room (sqlite-bundled) + AndroidX DataStore
- Concurrency/Time: kotlinx-coroutines, kotlinx-datetime

## Build Conventions

Custom Gradle convention plugins live under `build-logic/convention` and are applied via version
catalog aliases. Highlights:

- `dev.gaddal.convention.kmp.library` — KMP library + Android library + Kotlin serialization, common
  test deps, Android resources in KMP builds.
- `dev.gaddal.convention.cmp.application` — KMP app with Android + iOS targets, Compose plugins;
  Android target JVM 17; static iOS frameworks.
- `dev.gaddal.convention.cmp.library` — Compose deps/plugins for KMP libraries.
- `dev.gaddal.convention.cmp.feature` — Convenience plugin for Compose-enabled feature libraries.
- `dev.gaddal.convention.buildkonfig` — Wires BuildKonfig; expects `API_KEY` in `local.properties`
  with package name derived from module path.
- `dev.gaddal.convention.room` — Wires Room + KSP for Android and iOS targets; configures `schemas`
  directory.

Other conventions and helpers:

- Toolchains: Java/Kotlin target JVM 17; desugaring via `android-desugarJdkLibs`.
- Namespaces/resource prefixes derived from Gradle path: see helpers in `PathUtil.kt`.
- iOS framework names derived from module path; app baseName `ComposeApp`.
- Compose BOM/tooling imported centrally; Android debug UI tooling enabled where applicable.

## Dependency Injection (Koin)

- DI modules are declared in shared code where possible.
- Platform DI provides expected/actual implementations for platform services (e.g., locale
  application for i18n).

## Navigation

- Shared navigation root in
  `composeApp/src/commonMain/kotlin/dev/gaddal/chirp/navigation/NavigationRoot.kt`.
- Feature nav graphs (e.g., auth) are composed within the root.

## Resources & Localization

- Compose Multiplatform resources under `src/commonMain/composeResources/values/` per module.
- Localized variants under `values-<lang>` (e.g., `values-ar`).
- See `docs/internationalization.md` for implementation details, RTL handling, and typography.

## BuildKonfig

Modules applying the BuildKonfig convention require an `API_KEY` in `local.properties`. The
generated package name is derived from the Gradle path. Missing keys will fail the build for those
modules.

## Room Schemas

- `schemaDirectory` is configured to `<module>/schemas`.
- Commit schemas for reliable migration testing.
