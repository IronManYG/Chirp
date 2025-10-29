package dev.gaddal.core.data.auth

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

/**
 * Creates a `DataStore` instance to persist preferences in the application's storage directory.
 *
 * This method uses the context's `filesDir` to determine the storage path for the DataStore,
 * ensuring that all preferences data is securely stored within the app's private storage area.
 *
 * @param context The application context used to access the file storage directory.
 * @return A `DataStore<Preferences>` instance configured to store and manage preferences data.
 */
fun createDataStore(context: Context): DataStore<Preferences> {
    return createDataStore {
        context.filesDir.resolve(DATA_STORE_FILE_NAME).absolutePath
    }
}