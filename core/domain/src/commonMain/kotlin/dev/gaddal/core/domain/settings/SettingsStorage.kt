package dev.gaddal.core.domain.settings

import kotlinx.coroutines.flow.Flow

/**
 * Abstraction for persisting and observing application settings.
 * Extend with more fields in [AppSettings] and corresponding setters as needed.
 */
interface SettingsStorage {
    fun observeSettings(): Flow<AppSettings>
    suspend fun setLanguage(code: String)
    suspend fun update(transform: (AppSettings) -> AppSettings)
}
