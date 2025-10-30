package dev.gaddal.core.domain.settings

/**
 * Aggregates user-adjustable application settings.
 *
 * Extend this data class with additional fields (e.g., theme, telemetry) as needs grow.
 * If [languageCode] is null, it means the user hasn't explicitly chosen a language yet.
 */
data class AppSettings(
    val languageCode: String? = null
)
