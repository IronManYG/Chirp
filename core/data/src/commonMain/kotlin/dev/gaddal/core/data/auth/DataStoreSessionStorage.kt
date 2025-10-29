package dev.gaddal.core.data.auth

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import dev.gaddal.core.data.dto.AuthInfoSerializable
import dev.gaddal.core.data.mappers.toDomain
import dev.gaddal.core.data.mappers.toSerializable
import dev.gaddal.core.domain.auth.AuthInfo
import dev.gaddal.core.domain.auth.SessionStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

/**
 * An implementation of [SessionStorage] that utilizes Android Jetpack DataStore for persisting
 * and managing authentication-related session data.
 *
 * This class provides methods to observe and update the stored [AuthInfo] in a reactive manner.
 * The session data is serialized and deserialized using Kotlinx Serialization.
 *
 * @property dataStore An instance of DataStore to persist preferences.
 */
class DataStoreSessionStorage(
    private val dataStore: DataStore<Preferences>
) : SessionStorage {

    /**
     * Key used for accessing and storing authentication information in the preferences data store.
     *
     * This key is utilized to map the `AuthInfo` serialized data into the `Preferences` object
     * and retrieve it when needed. It is a constant identifier ensuring consistency in
     * storage and retrieval operations within the `DataStoreSessionStorage` class.
     */
    private val authInfoKey = stringPreferencesKey("KEY_AUTH_INFO")

    /**
     * JSON configuration object used for serialization and deserialization of data.
     *
     * This instance is configured to ignore unknown keys found in the JSON payload,
     * ensuring that extra or unexpected data fields in the input JSON do not cause errors.
     *
     * It is utilized for encoding and decoding operations within the implementation
     * of the `SessionStorage` interface, specifically in methods that deal with
     * storing and retrieving `AuthInfo` objects.
     */
    private val json = Json {
        ignoreUnknownKeys = true
    }

    /**
     * Observes changes to the authentication information stored in the data store.
     *
     * This method retrieves and decodes the serialized authentication information
     * stored under a specific key in the preferences, returning it as a stream of
     * `AuthInfo` objects. If no serialized data is found, it returns `null`.
     *
     * @return A `Flow` emitting the current `AuthInfo` or `null` if no data is available.
     */
    override fun observeAuthInfo(): Flow<AuthInfo?> {
        return dataStore.data.map { preferences ->
            val serializedJson = preferences[authInfoKey]
            serializedJson?.let {
                json.decodeFromString<AuthInfoSerializable>(it).toDomain()
            }
        }
    }

    /**
     * Updates the stored authentication information in the DataStore.
     *
     * If the provided [info] is null, the authentication information is removed from the DataStore.
     * Otherwise, the [info] is serialized and persisted. This method ensures that any changes
     * to the authentication data are reflected in the storage.
     *
     * @param info The authentication information to be saved or `null` to clear the stored authentication data.
     */
    override suspend fun set(info: AuthInfo?) {
        if (info == null) {
            dataStore.edit {
                it.remove(authInfoKey)
            }
            return
        }

        val serialized = json.encodeToString(info.toSerializable())
        dataStore.edit { prefs ->
            prefs[authInfoKey] = serialized
        }
    }
}