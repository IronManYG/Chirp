# Internationalization (i18n) & RTL

Chirp ships with multilingual support and right-to-left (RTL) handling. Validated locales: English (
`en`) and Arabic (`ar`).

## What’s Implemented

- Centralized language state via `LanguageManager` (validation, normalization, and state) in
  `core/presentation`.
- Platform locale application via `LocaleApplier` (expect/actual) injected into `LanguageManager`:
    - Android: `AppCompatDelegate.setApplicationLocales(...)` for API 33+ with fallback (
      `Language.android.kt`).
    - iOS: updates `AppleLanguages` in `NSUserDefaults` (`Language.ios.kt`).
- RTL layout direction via `ProvideMultilingualSupport(languageCode)` which sets
  `LocalLayoutDirection` for RTL languages (`ar`, `fa`, `he`, `ur`). See
  `core/presentation/.../Rtl.kt`.
- Typography mapping with Arabic-script friendly `Cairo` for Arabic-like languages;
  `PlusJakartaSans` otherwise (`core/designsystem/.../Type.kt`, `typographyForLanguage`).
- Language lifecycle: `MainViewModel` loads persisted language on startup and applies it before UI
  renders; exposes `changeLanguage(code)`.
- DI: Platform DI provides `LocaleApplier` in
  `core/presentation/.../di/CorePresentationModule.(android|ios).kt`.

## Resources Layout

- Per module: `src/commonMain/composeResources/values/strings.xml`
- Localized variants: `values-<lang>` (e.g., `values-ar/strings.xml`)

Examples:

- `core/presentation/src/commonMain/composeResources/values/strings.xml`
- `core/presentation/src/commonMain/composeResources/values-ar/strings.xml`
- `feature/auth/presentation/src/commonMain/composeResources/values/strings.xml`
- `feature/auth/presentation/src/commonMain/composeResources/values-ar/strings.xml`

## Add a New Locale

1. Mirror base strings in each module that owns UI strings: create `values-<lang>` and copy keys 1:
   1.
2. Keep plurals/arrays in parity across locales.
3. If RTL (e.g., `fa`, `he`, `ur`), ensure `isRtlLanguage` includes the primary code.
4. If Arabic script, typography will switch to `Cairo` automatically via `typographyForLanguage`.
5. Add language labels to any selection UI.

## Runtime Behavior

- On app start, `MainViewModel` reads `SettingsStorage` and calls
  `LanguageManager.setLanguage(initCode)`; this applies the locale and updates state.
- Root UI wraps content with `ChirpTheme(languageCode)` and
  `ProvideMultilingualSupport(languageCode)`.
- In-app switching uses `MainViewModel.changeLanguage(code)` to persist and apply immediately.

## Android Per‑App Locales

- Implemented via `AppCompatDelegate.setApplicationLocales(...)` on API 33+, with fallback retained
  for parity. See also `docs/i18n-implementation-plan.md` if present.

## Troubleshooting

- Layout direction doesn’t flip: ensure `ProvideMultilingualSupport(languageCode)` wraps your root
  and the code is a normalized primary tag (e.g., `ar`).
- Fonts look off for Arabic: verify `typographyForLanguage(languageCode)` is used and Cairo fonts
  are present in generated resources.
- Strings don’t change after switching: confirm `LanguageManager.setLanguage(code)` returns `true` (
  supported) and `SettingsStorage.setLanguage(code)` is called.
- Test opt-in warnings (e.g., coroutines) are harmless unless directly using those APIs; add
  deps/opt-ins as needed.
