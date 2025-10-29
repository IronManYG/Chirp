package dev.gaddal.core.domain.settings

/**
 * Aggregates user-adjustable application settings.
 *
 * Extend this data class with additional fields (e.g., theme, telemetry) as needs grow.
 */
data class AppSettings(
    val languageCode: String = "en"
)
