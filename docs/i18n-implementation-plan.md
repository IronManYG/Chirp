### i18n Implementation – Tracking Doc (Chirp)

This document tracks the rollout of the multilingual (i18n) plan across modules and platforms.

---

#### Scope
- Cross‑platform (Android, Desktop, iOS where available during config).
- Compose Multiplatform resources + LanguageManager + LocaleApplier (expect/actual) +
  SettingsStorage.

---

#### Tasks and status

1. Settings model and storage (domain + data)
- [x] Add `AppSettings` data class (domain)
- [x] Add `SettingsStorage` interface (domain)
- [x] Add `DataStoreSettingsStorage` implementation (data)
- [x] Bind `SettingsStorage` in `CoreDataModule`
- [ ] Unit tests for `SettingsStorage` (get/set/update) // Skip for now

2. Language lifecycle in MainViewModel/MainState
- [x] Extend `MainState` with `currentLanguage` and `isCheckingLanguage`
- [x] Inject `SettingsStorage` + `LanguageManager` into `MainViewModel`
- [x] On init, load persisted language and apply via `LanguageManager`
- [x] Expose `changeLanguage(code)` that persists + applies
- [ ] Unit tests for `LanguageManager.validateAndNormalize` // Skip for now

3. App gating and provisioning
- [x] Provide a singleton `LanguageManager` via Koin
- [x] Provide platform `LocaleApplier` via Koin in
  `core/presentation/.../di/CorePresentationModule.(android|ios).kt`
- [x] Gate content until `!isCheckingAuth && !isCheckingLanguage`
- [x] Wrap with `ChirpTheme(languageCode)` and `ProvideMultilingualSupport(languageCode)`

4. Auth flow – Language selection UX
- [x] Add `LanguageSelection` screen (first‑run gate)
- [x] Add globe icon in Auth top bar to navigate to `LanguageSelection`
- [x] Route: start at `LanguageSelection` only when no persisted language

5. RTL + Typography
- [x] Verify `isRtlLanguage` mapping covers all required RTL languages
- [x] Verify `typographyForLanguage` mapping for any added scripts; add fonts if needed

6. Resources hygiene
- [x] Audit `strings.xml` vs `strings-<lang>.xml` parity in all modules with UI strings
- [x] Mirror plurals and arrays across locales

7. Platform application details (Android 13+)

- [x] Implement `LocaleApplier` on Android using `AppCompatDelegate.setApplicationLocales(...)` (
  per‑app locales on API 33+) with a sensible fallback for host JVM/Desktop parity

8. Validation & Docs
- [x] Manual checklist run (first‑run selection → persistence → RTL → deep links)
- [x] Update README/docs with final i18n guide and troubleshooting

---

#### Notes
- Current supported languages: `en`, `ar`. Extend by adding locale files in each module that owns strings.
- Language is applied before UI renders to avoid flicker and ensure correct RTL and typography.
- Platform locale changes are funneled through `LocaleApplier` (expect/actual) and provided via
  platform DI modules.
