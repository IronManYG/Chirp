package dev.gaddal.chat.database

import androidx.room.Room
import androidx.room.RoomDatabase
import dev.gaddal.core.data.util.appDataDirectory
import java.io.File

actual class DatabaseFactory {
    actual fun create(): RoomDatabase.Builder<ChirpChatDatabase> {
        val directory = appDataDirectory

        if (!directory.exists()) {
            directory.mkdirs()
        }

        val dbFile = File(directory, ChirpChatDatabase.DB_NAME)
        return Room.databaseBuilder(dbFile.absolutePath)
    }
}