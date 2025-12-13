package dev.gaddal.core.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import dev.gaddal.core.domain.preferences.ThemePreference
import dev.gaddal.core.domain.preferences.ThemePreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Implementation of the `ThemePreferences` interface using Jetpack DataStore.
 *
 * This class provides methods to observe and update the user's theme preferences
 * stored in a `DataStore` instance, allowing for persistent and reactive management
 * of the theme settings. It leverages DataStore's `Preferences` API for storing
 * and retrieving the selected theme preference.
 *
 * @property dataStore The `DataStore` instance used for storing and retrieving
 *                     the user's theme preference as a key-value pair.
 */
class DataStoreThemePreferences(
    private val dataStore: DataStore<Preferences>
) : ThemePreferences {

    /**
     * Key used for storing and retrieving the theme preference in the preferences DataStore.
     *
     * This key is associated with the user's selected theme preference, which could be one of the
     * values defined in the `ThemePreference` enum: `LIGHT`, `DARK`, or `SYSTEM`. It is used
     * internally within the `DataStoreThemePreferences` class to map and manage the theme
     * preference value in persistent storage.
     */
    private val themePreferenceKey = stringPreferencesKey("theme_preference")

    /**
     * Observes changes to the user's theme preference stored in the data store.
     *
     * This method retrieves the current theme preference and continuously emits updates
     * whenever the user's theme preference changes within the data store. It defaults
     * to the `SYSTEM` preference if the stored value is unavailable or invalid.
     *
     * @return A [Flow] emitting the user's theme preference as a [ThemePreference] object.
     */
    override fun observeThemePreference(): Flow<ThemePreference> {
        return dataStore
            .data
            .map { preferences ->
                val currentPreference =
                    preferences[themePreferenceKey] ?: ThemePreference.SYSTEM.name
                try {
                    ThemePreference.valueOf(currentPreference)
                } catch (_: Exception) {
                    ThemePreference.SYSTEM
                }
            }
    }

    /**
     * Updates the user's theme preference in the persistent data store.
     *
     * This function allows the application to store a new theme preference, ensuring
     * that the user's chosen theme is retained across sessions. The preference is
     * stored in a key-value format for later retrieval or observation and can dynamically
     * affect the application's appearance.
     *
     * @param theme The new theme preference to be saved. Must be one of the values
     *              defined in the `ThemePreference` enum: `LIGHT`, `DARK`, or `SYSTEM`.
     */
    override suspend fun updateThemePreference(theme: ThemePreference) {
        dataStore.edit { preferences ->
            preferences[themePreferenceKey] = theme.name
        }
    }
}