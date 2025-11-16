package dev.gaddal.chat.database

import androidx.room.RoomDatabase

/**
 * Provides a platform-specific implementation for creating and configuring a
 * database instance of `ChirpChatDatabase`, utilizing Room's database builder.
 *
 * This class abstracts the process of setting up the database and ensures
 * platform compatibility by using the appropriate context or file paths based
 * on the target platform.
 *
 * Features:
 * - Supports configuration of the `ChirpChatDatabase` using Room's `databaseBuilder`.
 * - Facilitates database file location management tailored for the platform.
 *
 * Methods:
 * - `create()`: Constructs a `RoomDatabase.Builder<ChirpChatDatabase>` instance
 *   pre-configured with platform-specific details, such as database file path.
 *
 * Usage Notes:
 * This class is expected to be used during database initialization and setup.
 * The actual implementation varies based on the target platform leveraging
 * the `expect` and `actual` mechanism in Kotlin Multiplatform.
 */
expect class DatabaseFactory {
    fun create(): RoomDatabase.Builder<ChirpChatDatabase>
}