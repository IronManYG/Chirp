### Internationalization (i18n) and Multilingual Support Guide (old- only as reference - dont use it)

This document explains how multilingual support is implemented in the Maktabati project, how the user’s language preference is stored and applied, and how to add new languages safely. All paths below are relative to your project modules.

---

### Overview

- Supported languages: Arabic (`ar`) and English (`en`).
- The user selects a language in Settings; the choice is persisted and observed across the app.
- On app start and whenever the language changes, the app’s locale is applied using a centralized utility to ensure correct resources, layout direction, and system behavior across Android versions.

High‑level flow:
1. Settings screen → user picks a language.
2. `SettingsViewModel` updates `Settings` and persists it via `SettingsStorage`.
3. `MainActivity` observes `state.settings.currentLanguage` and applies the locale using `LocaleUtils.updateResources(...)`.
4. Compose UI re-composes and loads localized `strings.xml` resources.

---

### Key Components

#### 1) Language enum (source of truth for supported languages)
- File: `core/domain/src/main/java/com/example/core/domain/settings/Language.kt`
- Defines allowed languages and their codes.
```kotlin
enum class Language(val code: String) {
    ARABIC("ar"),
    ENGLISH("en")
}
```

Use this when you need to refer to supported languages in domain/presentation layers.

#### 2) Locale application utility (API-level safe)
- File: `core/presentation/ui/src/main/java/com/example/core/presentation/ui/LocaleUtils.kt`
- Responsibility: Apply the chosen `Locale` across Android versions, leveraging `LocaleManager` on Android 13+ and `AppCompatDelegate` for lower versions.
```kotlin
object LocaleUtils {
    fun updateResources(context: Context, locale: Locale) {
        Locale.setDefault(locale)
        val resources = context.resources
        val configuration = Configuration(resources.configuration)
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                context.getSystemService(android.app.LocaleManager::class.java).applicationLocales =
                    LocaleList(locale)
            }
            else -> {
                configuration.setLocale(locale)
                AppCompatDelegate.setApplicationLocales(LocaleListCompat.create(locale))
            }
        }
        @Suppress("DEPRECATION")
        resources.updateConfiguration(configuration, resources.displayMetrics)
    }
}
```

Why this matters:
- Ensures correct resource loading for `strings.xml`.
- Sets proper layout direction (RTL for Arabic, LTR for English) automatically.
- Uses modern per-app locales on Android 13+ while keeping backward compatibility.

#### 3) Applying locale in the app lifecycle
- File: `app/src/main/java/com/example/maktabati/MainActivity.kt`
- Observes settings, computes a `Locale`, and applies it before showing main content. A splash gating flag (`isLanguageChecked`) ensures the UI only appears after locale is set.
```kotlin
LaunchedEffect(state.settings.currentLanguage) {
    val locale = when (state.settings.currentLanguage) {
        Language.ARABIC -> Locale("ar")
        Language.ENGLISH -> Locale("en")
        else -> Locale.getDefault()
    }
    updateResources(context, locale)
    isLanguageChecked = true
}
```
- Only after auth is checked and language applied does navigation graph render:
```kotlin
if (!state.isCheckingAuth && isLanguageChecked) {
    val navController = rememberNavController()
    RootNavGraph(
        navController = navController,
        isLoggedIn = state.isLoggedIn
    )
}
```

#### 4) Settings: user choice → persistence
- Action: `settings/presentation/.../SettingsAction.kt`
```kotlin
data class UpdateLanguage(val language: Language) : SettingsAction()
```
- ViewModel: `settings/presentation/.../SettingsViewModel.kt`
    - Updates in-memory `state.settings.currentLanguage` and persists via `SettingsStorage.set(...)`.
```kotlin
is SettingsAction.UpdateLanguage -> {
    state = state.copy(settings = state.settings.copy(currentLanguage = action.language))
    viewModelScope.launch { settingsStorage.set(state.settings) }
}
```
- Storage API (domain): `core/domain/.../SettingsStorage.kt`
    - Abstracts persistence; implementation serializes `Settings`.
- Mapping to persist language by name: `core/data/.../settings/SettingsMapper.kt`
```kotlin
currentLanguage = currentLanguage.name // to serializable
...
currentLanguage = Language.valueOf(currentLanguage) // from serializable
```

#### 5) Localized resources
- Example (Settings module):
    - English strings: `settings/presentation/src/main/res/values/strings.xml`
    - Arabic strings:  `settings/presentation/src/main/res/values-ar/strings.xml`
- Compose usage typically relies on `stringResource(R.string.some_id)` or `UiText` wrappers.
- Shared UI helpers:
    - `core/presentation/ui/UiText.kt` and related helpers like `DataErrorToText.kt` ensure that user-visible messages resolve to localized strings at render time.

#### 6) Localized previews for Compose
- File: `core/presentation/ui/PreviewAnnotations.kt`
- Annotation to preview components in both Arabic and English:
```kotlin
@Preview(showBackground = true, locale = "ar", name = "Arabic")
@Preview(showBackground = true, locale = "en", name = "English")
annotation class LocalesPreview
```
Use `@LocalesPreview` in composable previews to quickly check text and layout direction.

---

### How Runtime Language Switching Works

- When the language changes in Settings:
    1. `SettingsViewModel` updates `Settings` and persists.
    2. `MainActivity` observes `state.settings.currentLanguage`, calls `LocaleUtils.updateResources` inside `LaunchedEffect`.
    3. The app’s `Resources` update, leading to correct `strings.xml` lookups; RTL/LTR updates apply.
    4. UI re-composes; strings rendered with `stringResource(...)` or `UiText` automatically reflect the new language.

No full app restart is required due to the splash gating and Compose recomposition, but note that some edge cases might still require a manual restart if a third-party view caches resources internally (rare).

---

### Adding a New Language

1. Extend enum
    - Add the new language to `Language.kt` with its BCP-47 code:
   ```kotlin
   enum class Language(val code: String) {
       ARABIC("ar"),
       ENGLISH("en"),
       FRENCH("fr") // example
   }
   ```

2. Map in `MainActivity`
    - Update the `when` in `LaunchedEffect` to return the proper `Locale`:
   ```kotlin
   Language.FRENCH -> Locale("fr")
   ```

3. Add resources per module
    - For each module that shows user-facing text, add `res/values-<lang>/strings.xml`.
    - Example: `library/presentation/src/main/res/values-fr/strings.xml` mirroring IDs from the base `values/strings.xml`.
    - Ensure all string IDs exist across locales to prevent fallback inconsistencies.

4. RTL consideration
    - If the new language is RTL (e.g., Persian `fa`), Android will auto-switch layout direction when the locale is applied. Verify paddings/margins and iconography for mirroring.

5. Fonts and typography (optional)
    - If the new language needs specific fonts, add an entry to your typography selection in the theme setup similar to existing `Tajawal`/`Cairo`/`Roboto` options, and consider mapping language to a recommended font if desired.

6. QA checklists
    - Use `@LocalesPreview` for spot checks.
    - Run on-device with the new language selected; navigate all major screens.
    - Verify pluralization, ellipsizing, and truncation.

---

### Using Localized Strings in Compose

- Use `stringResource(R.string.some_id)` directly inside composables.
- For view models or non-Compose layers, use `UiText` abstractions so that resolution happens in the UI:
```kotlin
// in ViewModel
data class SnackBarEvent(val message: UiText)

// in UI
val text = when (val t = event.message) {
    is UiText.StringResource -> stringResource(t.resId, *t.args.toTypedArray())
    is UiText.DynamicString -> t.value
}
```
- Keep all user-visible text in `strings.xml` to benefit from runtime locale changes and translation workflows.

---

### Navigation and Recomposition Considerations

- `MainActivity` delays NavGraph rendering until locale is applied; this prevents flicker where the first frame appears in the wrong language.
- If you dynamically change the language while on deep screens, Compose recomposition will localize strings. If any screen constructs long-lived non-Compose objects with cached resources, consider re-instantiating them on language change.

---

### Testing and Validation

- Manual tests:
    - Switch between Arabic and English in Settings; verify that:
        - All screens show correct translations.
        - RTL/LTR switches appropriately.
        - App relaunch persists the language selection.
- Automated checks:
    - Unit-test mapping/persistence for `Settings` (ensuring `Language` round-trips through `SettingsSerializable`).
    - Snapshot/UI tests per locale can be added using Compose testing APIs with forced locale configuration.

---

### Troubleshooting

- Strings not changing:
    - Ensure `stringResource(...)` is used instead of hardcoded strings.
    - Confirm `MainActivity`’s `LaunchedEffect` runs after the setting changes (observe `state.settings.currentLanguage`).
    - Verify that the correct `values-<lang>/strings.xml` exists with matching IDs.

- Layout direction wrong in custom views:
    - Ensure you don’t override layout direction manually; rely on configuration updates.
    - For custom measure/layout logic, consider reading `LayoutDirection` from Compose via `LocalLayoutDirection` if needed.

- Android 13+ anomalies:
    - `LocaleManager` is used; if the device has per-app language in system settings, verify that app-level selection is respected. Generally, the app-level call should take effect immediately.

---

### Future Enhancements (optional)

- Central map from `Language` → `Locale` to avoid duplicating `when` blocks.
- Support system/default language option by persisting a sentinel value (e.g., `Language.SYSTEM`) and mapping to `Locale.getDefault()`.
- Pluralization and quantity strings via `plurals` resources.
- Internationalized dates/numbers using `java.text` / `java.time` localized formatters.

---

### Quick Reference: Files Involved

- Apply locale:
    - `core/presentation/ui/LocaleUtils.kt`
    - `app/src/main/java/com/example/maktabati/MainActivity.kt`
- User choice and persistence:
    - `settings/presentation/.../SettingsAction.kt`
    - `settings/presentation/.../SettingsViewModel.kt`
    - `core/domain/.../settings/SettingsStorage.kt`
    - `core/data/.../settings/SettingsMapper.kt`
- Resources and previews:
    - `*/presentation/src/main/res/values/strings.xml`
    - `*/presentation/src/main/res/values-ar/strings.xml`
    - `core/presentation/ui/PreviewAnnotations.kt`
    - `core/presentation/ui/UiText.kt`

---

### Would you like this saved in the repo?

If you want this guide checked into the project, tell me the preferred location and filename (for example, `docs/i18n.md` or `README-i18n.md` in the root). I can add it as a markdown file in a follow-up step.