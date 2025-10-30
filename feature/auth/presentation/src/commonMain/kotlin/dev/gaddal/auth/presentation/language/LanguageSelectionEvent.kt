package dev.gaddal.auth.presentation.language

/**
 * Represents events emitted during the language selection process.
 *
 * These events notify when significant actions or state changes occur
 * in the language selection flow. External observers can use these
 * events to react appropriately, such as navigating to another screen
 * after the language selection process is completed.
 */
sealed interface LanguageSelectionEvent {
    data object Completed : LanguageSelectionEvent
}