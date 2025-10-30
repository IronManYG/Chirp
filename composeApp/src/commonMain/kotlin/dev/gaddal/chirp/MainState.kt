package dev.gaddal.chirp

/**
 * Represents the state of the main application, including user authentication status,
 * language initialization, and related gates before rendering content.
 */
data class MainState(
    val isLoggedIn: Boolean = false,
    val isCheckingAuth: Boolean = true,
    val isCheckingLanguage: Boolean = true,
    val currentLanguage: String = "en",
    val hasChosenLanguage: Boolean = false
)