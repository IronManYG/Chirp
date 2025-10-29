### i18n Implementation – Tracking Doc (Chirp)

This document tracks the rollout of the multilingual (i18n) plan across modules and platforms.

---

#### Scope
- Cross‑platform (Android, Desktop, iOS where available during config).
- Compose Multiplatform resources + LanguageManager + SettingsStorage.

---

#### Tasks and status

1. Settings model and storage (domain + data)
- [x] Add `AppSettings` data class (domain)
- [x] Add `SettingsStorage` interface (domain)
- [x] Add `DataStoreSettingsStorage` implementation (data)
- [x] Bind `SettingsStorage` in `CoreDataModule`
- [ ] Unit tests for `SettingsStorage` (get/set/update)

2. Language lifecycle in MainViewModel/MainState
- [x] Extend `MainState` with `currentLanguage` and `isCheckingLanguage`
- [x] Inject `SettingsStorage` + `LanguageManager` into `MainViewModel`
- [x] On init, load persisted language and apply via `LanguageManager`
- [x] Expose `changeLanguage(code)` that persists + applies
- [ ] Unit tests for `LanguageManager.validateAndNormalize`

3. App gating and provisioning
- [x] Provide a singleton `LanguageManager` via Koin
- [x] Gate content until `!isCheckingAuth && !isCheckingLanguage`
- [x] Wrap with `ChirpTheme(languageCode)` and `ProvideMultilingualSupport(languageCode)`

4. Auth flow – Language selection UX
- [x] Add `LanguageSelection` screen (first‑run gate)
- [ ] Add globe icon in Auth top bar to navigate to `LanguageSelection`
- [x] Route: start at `LanguageSelection` only when no persisted language

5. RTL + Typography
- [ ] Verify `isRtlLanguage` mapping covers all required RTL languages
- [ ] Verify `typographyForLanguage` mapping for any added scripts; add fonts if needed

6. Resources hygiene
- [ ] Audit `strings.xml` vs `strings-<lang>.xml` parity in all modules with UI strings
- [ ] Mirror plurals and arrays across locales

7. Platform nicety (optional – Android 13+)
- [ ] Enhance Android `actual changeLanguage` to set per‑app locales on API 33+ (keep `Locale.setDefault`)

8. Validation & Docs
- [ ] Manual checklist run (first‑run selection → persistence → RTL → deep links)
- [ ] Update README/docs with final i18n guide and troubleshooting

---

#### Notes
- Current supported languages: `en`, `ar`. Extend by adding locale files in each module that owns strings.
- Language is applied before UI renders to avoid flicker and ensure correct RTL and typography.
