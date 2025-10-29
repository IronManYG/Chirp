package dev.gaddal.core.data.auth

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toPath

/**
 * Creates a `DataStore` instance backed by the path provided by the `producePath` lambda.
 *
 * This method utilizes `PreferenceDataStoreFactory` to generate a `DataStore<Preferences>`
 * based on the file path determined from the lambda function. It is designed to abstract
 * platform-specific differences in determining file paths, allowing for flexible and reusable
 * data storage configurations.
 *
 * @param producePath A lambda function that returns the file path as a `String` where
 *                    the `DataStore` should store its data.
 * @return A `DataStore<Preferences>` instance configured to store data at the specified path.
 */
fun createDataStore(producePath: () -> String): DataStore<Preferences> {
    return PreferenceDataStoreFactory.createWithPath {
        producePath().toPath()
    }
}

/**
 * The file name used for storing DataStore preferences.
 *
 * This constant defines the name of the file where preferences will be persisted
 * across sessions, ensuring consistent access to stored data. It is used to build
 * the file path for the DataStore instance.
 *
 * In Android, this file resides in the app's files directory, while in iOS, the file path
 * is constructed to reside in the document directory of the app sandbox.
 */
internal const val DATA_STORE_FILE_NAME = "prefs.preferences_pb"