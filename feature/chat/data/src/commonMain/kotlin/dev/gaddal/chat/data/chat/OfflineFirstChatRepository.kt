package dev.gaddal.chat.data.chat

import dev.gaddal.chat.data.mappers.toDomain
import dev.gaddal.chat.data.mappers.toEntity
import dev.gaddal.chat.data.mappers.toLastMessageView
import dev.gaddal.chat.database.ChirpChatDatabase
import dev.gaddal.chat.database.entities.ChatWithParticipants
import dev.gaddal.chat.domain.chat.ChatRepository
import dev.gaddal.chat.domain.chat.ChatService
import dev.gaddal.chat.domain.models.Chat
import dev.gaddal.core.domain.util.DataError
import dev.gaddal.core.domain.util.Result
import dev.gaddal.core.domain.util.onSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Repository implementation for managing and accessing chat data using an offline-first approach.
 *
 * This class provides methods for fetching and retrieving chat data while maintaining synchronization
 * between local and remote data sources. It leverages the underlying `ChatService` for remote operations
 * and the `ChirpChatDatabase` for local storage and querying. The combination ensures that the repository
 * provides reliable and up-to-date chat information even in offline scenarios.
 *
 * The repository ensures data consistency by merging results from the remote service with the local
 * database, caching the relevant data for future use.
 *
 * @property chatService The service responsible for handling remote chat operations, such as fetching chats.
 * @property db The local database managing chat entities, participants, messages, and their relationships.
 */
class OfflineFirstChatRepository(
    private val chatService: ChatService,
    private val db: ChirpChatDatabase
) : ChatRepository {

    /**
     * Retrieves a Flow that emits a list of chats.
     *
     * This function fetches the chat data from the local database. It maps the retrieved data
     * to a domain model representation, ensuring that only chats with active participants
     * are included in the result. The emitted list of chats is automatically updated
     * whenever the underlying database state changes.
     *
     * @return A Flow emitting a list of `Chat` objects, each representing a chat with its participants,
     * last activity timestamp, and potentially the last message.
     */
    override fun getChats(): Flow<List<Chat>> {
        return db.chatDao.getChatsWithActiveParticipants()
            .map { chatWithParticipantsList ->
                chatWithParticipantsList.map { it.toDomain() }
            }
    }

    /**
     * Fetches the list of chats and synchronizes them with the local database.
     *
     * This method retrieves the chats from the remote `ChatService` and processes them into the appropriate
     * domain entities. The chats, their participants, and the last messages are mapped and then updated
     * in the local database, ensuring that the stored data remains consistent with the remote source.
     *
     * @return A `Result` object containing either a list of `Chat` entities if the fetch operation
     *         is successful, or a `DataError.Remote` object in case of a failure.
     */
    override suspend fun fetchChats(): Result<List<Chat>, DataError.Remote> {
        return chatService
            .getChats()
            .onSuccess { chats ->
                val chatsWithParticipants = chats.map { chat ->
                    ChatWithParticipants(
                        chat = chat.toEntity(),
                        participants = chat.participants.map { it.toEntity() },
                        lastMessage = chat.lastMessage?.toLastMessageView()
                    )
                }

                db.chatDao.upsertChatsWithParticipantsAndCrossRefs(
                    chats = chatsWithParticipants,
                    participantDao = db.chatParticipantDao,
                    crossRefDao = db.chatParticipantsCrossRefDao,
                    messageDao = db.chatMessageDao
                )
            }
    }
}