package dev.gaddal.chat.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Factory class for initializing and configuring an instance of `ChirpChatDatabase` on the Android platform.
 *
 * This class provides the implementation for creating the database using Room's database builder,
 * specifically tailored for the Android platform. It ensures the appropriate application context
 * and database file path are used to initialize the database.
 *
 * Constructor:
 * - Accepts a `Context` object required to access the application-specific file system and resources.
 *
 * Key Responsibilities:
 * - Initializes a `RoomDatabase.Builder` instance of type `ChirpChatDatabase`.
 * - Manages the database file location, ensuring it is compatible with the platform's file system.
 *
 * Methods:
 * - `create()`: Constructs and returns a pre-configured `RoomDatabase.Builder` for `ChirpChatDatabase`.
 *   This method uses the application's context to locate the database file and initialize it with
 *   Room's configuration.
 */
actual class DatabaseFactory(
    private val context: Context
) {
    /**
     * Creates a database builder for the `ChirpChatDatabase` instance.
     *
     * This method initializes a Room database builder using the application context and the
     * absolute path to the database file (`chirp.db`). It is used to construct and configure
     * the database instance for managing persistent storage of chat-related data in the application.
     *
     * @return A `RoomDatabase.Builder` instance for `ChirpChatDatabase`, allowing further
     *         configuration and customization before creating the database.
     */
    actual fun create(): RoomDatabase.Builder<ChirpChatDatabase> {
        val dbFile = context.applicationContext.getDatabasePath(ChirpChatDatabase.DB_NAME)

        return Room.databaseBuilder(
            context.applicationContext,
            dbFile.absolutePath
        )
    }
}