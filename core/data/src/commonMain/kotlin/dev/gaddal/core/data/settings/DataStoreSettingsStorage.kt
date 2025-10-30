package dev.gaddal.core.data.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import dev.gaddal.core.domain.settings.AppSettings
import dev.gaddal.core.domain.settings.SettingsStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

/**
 * Implementation of [SettingsStorage] backed by a DataStore for persisting and observing application settings.
 *
 * This class provides functionality to store, retrieve, and observe application settings, including the ability to
 * set specific values or update them using a transformation.
 *
 * @constructor Creates an instance with the provided DataStore for managing preferences.
 * @property dataStore The DataStore instance used for storing and retrieving preferences.
 */
class DataStoreSettingsStorage(
    private val dataStore: DataStore<Preferences>
) : SettingsStorage {

    private val keyLanguage = stringPreferencesKey("KEY_APP_LANGUAGE")

    override fun observeSettings(): Flow<AppSettings> =
        dataStore.data.map { prefs ->
            AppSettings(
                languageCode = prefs[keyLanguage] // null means not chosen yet
            )
        }

    override suspend fun setLanguage(code: String) {
        dataStore.edit { prefs ->
            prefs[keyLanguage] = code
        }
    }

    override suspend fun update(transform: (AppSettings) -> AppSettings) {
        val current = observeSettings().firstOrNull() ?: AppSettings()
        val next = transform(current)
        dataStore.edit { prefs ->
            val lang = next.languageCode
            val curr = current.languageCode
            if (lang != curr) {
                if (lang == null) {
                    prefs.remove(keyLanguage)
                } else {
                    prefs[keyLanguage] = lang
                }
            }
        }
    }
}
