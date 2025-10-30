# Changelog

All notable changes to this project will be documented in this file.

The format is based on Keep a Changelog, and this project adheres to Semantic Versioning.

## [0.8.0] - 2025-10-30

### Added
- Settings: Implement language persistence and global language management via `LanguageManager` ([2ac5406](https://github.com/IronManYG/Chirp/commit/2ac5406))
- Authentication: First-run language selection screen and navigation integration ([a0b0063](https://github.com/IronManYG/Chirp/commit/a0b0063))
- Authentication: Change language option across auth screens (top bar, navigation updates, new strings) ([9393fe1](https://github.com/IronManYG/Chirp/commit/9393fe1))
- i18n: Arabic translations for password-related strings ([6e02294](https://github.com/IronManYG/Chirp/commit/6e02294))

### Changed
- Documentation: Add/expand internationalization (i18n) guides and implementation plan ([4b72f04](https://github.com/IronManYG/Chirp/commit/4b72f04), [24baf1f](https://github.com/IronManYG/Chirp/commit/24baf1f), [886166a](https://github.com/IronManYG/Chirp/commit/886166a), [76aabdc](https://github.com/IronManYG/Chirp/commit/76aabdc))

[0.8.0]: https://github.com/IronManYG/Chirp/compare/627e3d0...76aabdc

## [0.7.0] - 2025-10-29

### Added
- Authentication: Forgot password flow ([5da744c](https://github.com/IronManYG/Chirp/commit/5da744c))
- Authentication: Reset password screen structure ([4ffa703](https://github.com/IronManYG/Chirp/commit/4ffa703))
- Authentication: Implement reset password flow ([58455fb](https://github.com/IronManYG/Chirp/commit/58455fb))
- UX: Clear focus on tap utility ([026bf00](https://github.com/IronManYG/Chirp/commit/026bf00))

### Fixed
- Authentication: Fix window insets by wrapping auth screens with `ChirpSnackbarScaffold` ([627e3d0](https://github.com/IronManYG/Chirp/commit/627e3d0))

[0.7.0]: https://github.com/IronManYG/Chirp/compare/1253e0e...627e3d0

## [0.6.0] - 2025-10-29

### Added
- Authentication: Login screen and navigation ([b627c86](https://github.com/IronManYG/Chirp/commit/b627c86))
- Authentication: Login flow and navigation ([47a7d0b](https://github.com/IronManYG/Chirp/commit/47a7d0b))
- Authentication: Session storage using DataStore ([dcebb67](https://github.com/IronManYG/Chirp/commit/dcebb67))
- Authentication: Automatic token refresh logic ([12544bc](https://github.com/IronManYG/Chirp/commit/12544bc))
- Authentication: Session expiration handling and Chat ViewModel updates ([1253e0e](https://github.com/IronManYG/Chirp/commit/1253e0e))
- Application: Authentication check on app startup ([0137a28](https://github.com/IronManYG/Chirp/commit/0137a28))
- Android: Splash screen and updated launcher icons ([b811e92](https://github.com/IronManYG/Chirp/commit/b811e92))

### Changed
- iOS: Configure launch screen and update app icon ([ac90ddb](https://github.com/IronManYG/Chirp/commit/ac90ddb))

[0.6.0]: https://github.com/IronManYG/Chirp/compare/7cc00e7...1253e0e

## [0.5.0] - 2025-10-27

### Added
- Authentication: Registration screen UI and logic ([8014789](https://github.com/IronManYG/Chirp/commit/8014789))
- Authentication: Input validation for registration form ([605c23c](https://github.com/IronManYG/Chirp/commit/605c23c))
- Authentication: User registration service ([3e26873](https://github.com/IronManYG/Chirp/commit/3e26873))
- Authentication: Registration flow and error handling ([ca9c7ac](https://github.com/IronManYG/Chirp/commit/ca9c7ac))
- Dependency Injection: Integrate Koin across modules ([776de34](https://github.com/IronManYG/Chirp/commit/776de34))
- Authentication: Registration success screen ([0be7f64](https://github.com/IronManYG/Chirp/commit/0be7f64))
- Authentication: Integrate navigation for authentication flow ([800426b](https://github.com/IronManYG/Chirp/commit/800426b))
- Authentication: Email verification screen ([de34a7e](https://github.com/IronManYG/Chirp/commit/de34a7e))
- Authentication: Email verification flow ([12e9eb7](https://github.com/IronManYG/Chirp/commit/12e9eb7))
- Authentication: Resend verification email functionality ([08d1b42](https://github.com/IronManYG/Chirp/commit/08d1b42))
- Authentication: Deep linking for email verification ([7cc00e7](https://github.com/IronManYG/Chirp/commit/7cc00e7))

### Fixed
- Authentication: Registration button state and conflict error handling ([e6f1b60](https://github.com/IronManYG/Chirp/commit/e6f1b60))

[0.5.0]: https://github.com/IronManYG/Chirp/compare/0b3416b...7cc00e7

## [0.4.0] - 2025-10-26

### Added
- Design System: `ChirpSnackbarScaffold` component ([0b3416b](https://github.com/IronManYG/Chirp/commit/0b3416b))
- Design System: success layout and icon components ([1d28ba7](https://github.com/IronManYG/Chirp/commit/1d28ba7))
- Design System: `ChirpAdaptiveResultLayout` component ([4c83151](https://github.com/IronManYG/Chirp/commit/4c83151))
- Design System: Adaptive authentication form layout ([9c321b8](https://github.com/IronManYG/Chirp/commit/9c321b8))
- Design System: `ChirpSurface` layout component ([a4b4eda](https://github.com/IronManYG/Chirp/commit/a4b4eda))
- Design System: `ChirpTextField` composable ([b1b067e](https://github.com/IronManYG/Chirp/commit/b1b067e))
- Design System: `ChirpFloatingActionButton` ([0c59db9](https://github.com/IronManYG/Chirp/commit/0c59db9))
- Design System: `ChirpIconButton` composable ([e5c5e88](https://github.com/IronManYG/Chirp/commit/e5c5e88))
- Design System: `ChirpButton` composable and styles ([d35545f](https://github.com/IronManYG/Chirp/commit/d35545f))

### Changed
- Design System: Refactor text fields and add icons ([a332a78](https://github.com/IronManYG/Chirp/commit/a332a78))

[0.4.0]: https://github.com/IronManYG/Chirp/compare/e21e288...0b3416b

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