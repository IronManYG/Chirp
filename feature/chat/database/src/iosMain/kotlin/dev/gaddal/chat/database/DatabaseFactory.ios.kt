@file:OptIn(ExperimentalForeignApi::class)

package dev.gaddal.chat.database

import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

/**
 * Platform-specific implementation of the `DatabaseFactory` for iOS.
 *
 * This class provides the necessary configuration to initialize and build an instance
 * of `ChirpChatDatabase` tailored for the iOS platform. It abstracts the details for
 * setting up the database file location in iOS-specific directories and enables Room
 * to construct the database instance.
 *
 * Key Responsibilities:
 * - Provides a factory method to create and configure a `RoomDatabase.Builder` for
 *   initializing the `ChirpChatDatabase`.
 * - Manages the database file path, ensuring compatibility with the iOS file system.
 *
 * Methods:
 * - `create()`: Builds and returns a pre-configured `RoomDatabase.Builder` instance
 *   that utilizes the iOS-specific database file path.
 *
 * Intended Usage:
 * This class is employed during the setup and initialization phase of the database
 * on the iOS platform. It ensures the database file is located within the proper
 * document directory and is ready for use with Room's configuration settings.
 */
actual class DatabaseFactory {
    /**
     * Creates a builder for the `ChirpChatDatabase` Room database, pointing to the database
     * file located in the document directory on the iOS platform. This builder can be used to
     * configure and initialize the database.
     *
     * @return a `RoomDatabase.Builder` configured for creating and accessing the `ChirpChatDatabase`.
     */
    actual fun create(): RoomDatabase.Builder<ChirpChatDatabase> {
        val dbFile = documentDirectory() + "/${ChirpChatDatabase.DB_NAME}"

        return Room.databaseBuilder(dbFile)
    }

    /**
     * Retrieves the path to the document directory on the iOS platform.
     *
     * This method uses the `NSFileManager` to locate the directory
     * associated with storing documents specific to the app's user domain.
     *
     * @return The absolute path of the document directory as a non-null string.
     * Throws an exception if the directory path cannot be determined.
     */
    private fun documentDirectory(): String {
        val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null
        )

        return requireNotNull(documentDirectory?.path)
    }
}