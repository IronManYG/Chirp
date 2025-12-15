package dev.gaddal.core.domain.preferences

import kotlinx.coroutines.flow.Flow

/**
 * Interface for managing the user's theme preferences in the application.
 *
 * This interface provides methods to observe and update the theme preference,
 * allowing dynamic management and application of the user's preferred theme.
 * It serves as an abstraction over any implementation that manages persistent
 * or reactive theme preference data.
 */
interface ThemePreferences {
    /**
     * Observes changes to the theme preference setting.
     *
     * This method provides a flow of `ThemePreference` values that emit updates whenever
     * the user's theme preference changes. The flow is typically used to dynamically react
     * to theme changes in the application, such as updating the UI based on the selected
     * theme (e.g., LIGHT, DARK, or SYSTEM).
     *
     * @return A [Flow] that emits the current and future values of the user's theme preference.
     */
    fun observeThemePreference(): Flow<ThemePreference>

    /**
     * Updates the user's theme preference for the application.
     *
     * This function modifies the currently stored theme preference, allowing the application
     * to adapt its user interface based on the selected theme (e.g., light, dark, or system default).
     * The updated preference is intended to influence the application's appearance dynamically
     * and is typically stored for future sessions.
     *
     * @param theme The new theme preference to be applied. It should be one of the values
     *              defined in the `ThemePreference` enum: `LIGHT`, `DARK`, or `SYSTEM`.
     */
    suspend fun updateThemePreference(theme: ThemePreference)
}