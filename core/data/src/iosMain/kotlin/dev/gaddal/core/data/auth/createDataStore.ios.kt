@file:OptIn(ExperimentalForeignApi::class)

package dev.gaddal.core.data.auth

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

/**
 * Creates a `DataStore` instance for persisting preferences using a predefined file path.
 *
 * The method constructs a `DataStore<Preferences>` object utilizing platform-specific
 * logic to generate the appropriate file path. It simplifies the setup of a `DataStore`
 * for preference storage by encapsulating necessary file path configurations.
 *
 * @return A `DataStore<Preferences>` instance configured to store preferences at the specified path.
 */
fun createDataStore(): DataStore<Preferences> {
    return createDataStore {
        val directory = NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null
        )
        requireNotNull(directory).path + "/$DATA_STORE_FILE_NAME"
    }
}