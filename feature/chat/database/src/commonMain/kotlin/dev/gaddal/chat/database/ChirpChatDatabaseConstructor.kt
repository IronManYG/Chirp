package dev.gaddal.chat.database

import androidx.room.RoomDatabaseConstructor

/**
 * Provides the platform-specific construction mechanism for the `ChirpChatDatabase`.
 *
 * This object extends `RoomDatabaseConstructor` for `ChirpChatDatabase` and is responsible
 * for initializing the database instance. By utilizing Kotlin Multiplatform's `expect/actual`
 * mechanism, this class ensures compatibility across different platforms, such as Android and iOS.
 *
 * Responsibilities:
 * - Encapsulates the database initialization process tailored to the target platform.
 * - Constructs and returns an instance of `ChirpChatDatabase` to manage chat-related data.
 * - Utilizes the Room database framework for automated ORM and data persistence.
 *
 * Method Details:
 * - `initialize()`: Creates and initializes an instance of `ChirpChatDatabase`.
 *   This ensures that the database is properly configured and ready for use within the application.
 *
 * Key Features:
 * - Adapts the database construction process to platform-specific requirements.
 * - Serves as an abstraction layer for database initialization, simplifying the setup process
 *   across multiple platforms.
 */
@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object ChirpChatDatabaseConstructor : RoomDatabaseConstructor<ChirpChatDatabase> {
    override fun initialize(): ChirpChatDatabase
}