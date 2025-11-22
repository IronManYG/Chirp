package dev.gaddal.chat.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import dev.gaddal.chat.database.dao.ChatDao
import dev.gaddal.chat.database.dao.ChatMessageDao
import dev.gaddal.chat.database.dao.ChatParticipantDao
import dev.gaddal.chat.database.dao.ChatParticipantsCrossRefDao
import dev.gaddal.chat.database.entities.ChatEntity
import dev.gaddal.chat.database.entities.ChatMessageEntity
import dev.gaddal.chat.database.entities.ChatParticipantCrossRef
import dev.gaddal.chat.database.entities.ChatParticipantEntity
import dev.gaddal.chat.database.view.LastMessageView

/**
 * Represents the main database class for the Chirp Chat application, which manages the
 * storage and retrieval of chat-related data. This class is an abstraction over RoomDatabase
 * and defines entities and DAOs required for the application's persistent storage.
 *
 * Entities:
 * - `ChatEntity`: Defines the structure for storing chat details.
 * - `ChatParticipantEntity`: Represents users participating in chats.
 * - `ChatMessageEntity`: Stores messages exchanged in the chat system.
 * - `ChatParticipantCrossRef`: Manages many-to-many relationships between chats and participants.
 *
 * View:
 * - `LastMessageView`: A database view for efficiently retrieving the last message in each chat.
 *
 * Version:
 * - The `version` parameter specifies the database schema version to manage schema migrations.
 *
 * DAOs:
 * - Provides abstract methods for accessing various data access objects (DAOs), including:
 *   - `ChatDao`: Manages CRUD operations for chats.
 *   - `ChatParticipantDao`: Handles participant-related operations.
 *   - `ChatMessageDao`: Deals with message-related data interactions.
 *   - `ChatParticipantsCrossRefDao`: Performs operations on chat-participant relationships.
 *
 * Properties:
 * - `DB_NAME`: Contains the file name of the database. It is used to reference and initialize the datastore.
 */
@Database(
    entities = [
        ChatEntity::class,
        ChatParticipantEntity::class,
        ChatMessageEntity::class,
        ChatParticipantCrossRef::class,
    ],
    views = [
        LastMessageView::class
    ],
    version = 1,
)
@ConstructedBy(ChirpChatDatabaseConstructor::class)
abstract class ChirpChatDatabase : RoomDatabase() {
    abstract val chatDao: ChatDao
    abstract val chatParticipantDao: ChatParticipantDao
    abstract val chatMessageDao: ChatMessageDao
    abstract val chatParticipantsCrossRefDao: ChatParticipantsCrossRefDao

    companion object {
        /**
         * Represents the name of the database used in the application.
         * This constant stores the database file name, including its extension.
         * It is used to identify and access the associated database file.
         */
        const val DB_NAME = "chirp.db"
    }
}