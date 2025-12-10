package dev.gaddal.core.data.auth

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import dev.gaddal.core.data.util.appDataDirectory
import java.io.File

/**
 * Creates a `DataStore` instance for storing `Preferences` data using a specified file path.
 * The file path is determined based on the application's data directory. If the directory
 * does not exist, it will be created before initializing the `DataStore`.
 *
 * This function manages directory creation and integrates file path determination for
 * seamless and consistent data storage.
 *
 * @return A `DataStore<Preferences>` instance configured to store data in the application's
 * data directory under a defined file name.
 */
fun createDataStore(): DataStore<Preferences> = createDataStore {
    val directory = appDataDirectory

    if (!directory.exists()) {
        directory.mkdirs()
    }

    File(directory, DATA_STORE_FILE_NAME).absolutePath
}