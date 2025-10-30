package dev.gaddal.auth.presentation.language

/**
 * Represents user actions for selecting and confirming a language within the language selection screen.
 *
 * The implementing actions include:
 * - Selecting a specific language code.
 * - Confirming the selected language.
 */
sealed interface LanguageSelectionAction {
    data class OnSelect(val code: String) : LanguageSelectionAction
    data object OnConfirmClick : LanguageSelectionAction
}