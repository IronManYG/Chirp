package dev.gaddal.auth.presentation.language

/**
 * Represents the state of the language selection UI.
 *
 * This class encapsulates the current state for the language selection process,
 * including the list of supported languages, the currently selected language code,
 * and whether the application is in the process of applying the selected language.
 *
 * @property supportedLanguages A list of all available language codes that the user can select from.
 * @property selectedCode The currently selected language code, or null if no language has been selected.
 * @property isApplying A flag indicating whether the selected language is being applied.
 */
data class LanguageSelectionState(
    val supportedLanguages: List<String> = emptyList(),
    val selectedCode: String? = null,
    val isApplying: Boolean = false
)