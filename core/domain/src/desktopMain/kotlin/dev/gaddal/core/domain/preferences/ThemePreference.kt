package dev.gaddal.core.domain.preferences

/**
 * Represents the user's theme preference for the application.
 *
 * This enum defines the available theme configurations:
 * - `LIGHT`: Uses a light theme across the application.
 * - `DARK`: Uses a dark theme across the application.
 * - `SYSTEM`: Adapts the theme based on the system's current settings.
 *
 * Used primarily in user settings and dynamically applied to the application's UI
 * by observing or updating the theme preference.
 */
enum class ThemePreference {
    LIGHT,
    DARK,
    SYSTEM
}