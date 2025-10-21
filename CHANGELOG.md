# Changelog

All notable changes to this project will be documented in this file.

The format is based on Keep a Changelog, and this project adheres to Semantic Versioning.

## [0.3.0] - 2025-10-21

### Added
- Dynamic typography for multilingual support with Cairo font family and language-aware font selection; updated ChirpTheme to accept optional languageCode; hoisted LanguageManager to App level; adjusted custom typography to inherit MaterialTheme font family ([e21e288](https://github.com/IronManYG/Chirp/commit/e21e288))
- Multilingual support framework with RTL handling for English and Arabic ([9ab3532](https://github.com/IronManYG/Chirp/commit/9ab3532))
  - `LanguageManager` for centralized language state management
  - Platform-specific language switching via expect/actual pattern
  - RTL layout support with automatic `LayoutDirection` switching
  - String resources for English and Arabic
  - Demo implementation with `MultilingualExampleDemo`
- Device configuration utility for adaptive layouts based on screen size and orientation ([53b46c9](https://github.com/IronManYG/Chirp/commit/53b46c9))
- Kermit logging integration with custom `ChirpLogger` interface ([90b510b](https://github.com/IronManYG/Chirp/commit/90b510b))
- Ktor HTTP client factory with JSON handling, timeouts, WebSocket support, and logging ([90b510b](https://github.com/IronManYG/Chirp/commit/90b510b))
- Type-safe HTTP client extension functions for GET, POST, PUT, and DELETE requests ([b8bdd61](https://github.com/IronManYG/Chirp/commit/b8bdd61))
- Platform-specific error handling for Ktor HTTP client (Android and iOS) ([c9d195d](https://github.com/IronManYG/Chirp/commit/c9d195d))
- Core theming infrastructure with Material 3 color schemes and extended color palettes ([d93945a](https://github.com/IronManYG/Chirp/commit/d93945a))
  - Plus Jakarta Sans typography with multiple font weights
  - `ChirpTheme` composable wrapper
- `ObserveAsEvents` utility for lifecycle-aware flow observation in Compose ([5c952f5](https://github.com/IronManYG/Chirp/commit/5c952f5))
- `UiText` utility for handling dynamic and resource-based strings with formatting support ([840d513](https://github.com/IronManYG/Chirp/commit/840d513))
- Core domain error and result handling utilities with `DataError` and `Result` types ([41f66fd](https://github.com/IronManYG/Chirp/commit/41f66fd))

### Changed
- Bumped Android Gradle Plugin (AGP) version to 8.13.0 ([01d5dee](https://github.com/IronManYG/Chirp/commit/01d5dee))
- Updated README for improved clarity and accuracy with refined platform notes and expanded setup instructions ([5af9844](https://github.com/IronManYG/Chirp/commit/5af9844))

[0.3.0]: https://github.com/IronManYG/Chirp/compare/91ad807...e21e288

## [0.2.0] - 2025-10-16

### Added
- Room convention plugin ([91ad807](https://github.com/IronManYG/Chirp/commit/91ad807))
- BuildKonfig convention plugin ([8112a5d](https://github.com/IronManYG/Chirp/commit/8112a5d))
- CMP library and feature convention plugins ([30801a0](https://github.com/IronManYG/Chirp/commit/30801a0))
- Compose Multiplatform application convention plugin ([799c21f](https://github.com/IronManYG/Chirp/commit/799c21f))
- Compose-specific Android application convention plugin ([8603775](https://github.com/IronManYG/Chirp/commit/8603775))
- Kotlin Android convention plugin ([9c9aa0b](https://github.com/IronManYG/Chirp/commit/9c9aa0b))
- Advanced developer guidelines (Chirp – Advanced Developer Guidelines) ([4560cd0](https://github.com/IronManYG/Chirp/commit/4560cd0))
- Initial CHANGELOG.md ([f167176](https://github.com/IronManYG/Chirp/commit/f167176))

### Changed
- Migrate modules to new KMP/Kotlin Android conventions ([37c6cbe](https://github.com/IronManYG/Chirp/commit/37c6cbe), [9c9aa0b](https://github.com/IronManYG/Chirp/commit/9c9aa0b))
- Overhaul README with detailed project documentation ([f64a11a](https://github.com/IronManYG/Chirp/commit/f64a11a))

### Removed
- Placeholder tests from all modules ([af5575b](https://github.com/IronManYG/Chirp/commit/af5575b))

[0.2.0]: https://github.com/IronManYG/Chirp/compare/c7f0989...91ad807

## [0.1.0] - 2025-10-15

### Added
- Initialize multi-module Kotlin Multiplatform project structure ([5c6e592](https://github.com/IronManYG/Chirp/commit/5c6e592))
- Introduce build-logic for convention plugins ([c7f0989](https://github.com/IronManYG/Chirp/commit/c7f0989))
- Configure module dependencies and clean up test configurations ([c36f89c](https://github.com/IronManYG/Chirp/commit/c36f89c))

### Changed
- Update Gradle configuration and dependencies ([4a6357e](https://github.com/IronManYG/Chirp/commit/4a6357e))

### Notes
- Initial release of the multi-module setup. Earlier meta/automation commits and merge commits are omitted from user-facing notes.

[0.1.0]: https://github.com/IronManYG/Chirp/compare/82505f6...c7f0989

## [0.0.1] - 2025-10-15

### Added
- Initial project setup and repository scaffolding ([82505f6](https://github.com/IronManYG/Chirp/commit/82505f6))
- Add initial project notes (JUNIE.md) ([f4570e4](https://github.com/IronManYG/Chirp/commit/f4570e4))

### Notes
- Omitted non user-facing commits (e.g., workflow automation and merge commits).
- This section reflects commits on main before the gradle-multi-module-setup branch.

[0.0.1]: https://github.com/IronManYG/Chirp/commit/82505f6