package dev.gaddal.chat.data.chat

import dev.gaddal.chat.data.mappers.toDomain
import dev.gaddal.chat.data.mappers.toEntity
import dev.gaddal.chat.data.mappers.toLastMessageView
import dev.gaddal.chat.database.ChirpChatDatabase
import dev.gaddal.chat.database.entities.ChatInfoEntity
import dev.gaddal.chat.database.entities.ChatParticipantEntity
import dev.gaddal.chat.database.entities.ChatWithParticipants
import dev.gaddal.chat.domain.chat.ChatRepository
import dev.gaddal.chat.domain.chat.ChatService
import dev.gaddal.chat.domain.models.Chat
import dev.gaddal.chat.domain.models.ChatInfo
import dev.gaddal.core.domain.util.DataError
import dev.gaddal.core.domain.util.EmptyResult
import dev.gaddal.core.domain.util.Result
import dev.gaddal.core.domain.util.asEmptyResult
import dev.gaddal.core.domain.util.onSuccess
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.supervisorScope

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
     * Retrieves a flow of chats converted into the domain model, each consisting of filtered active participants
     * and their last message.
     *
     * This method accesses the local database to fetch chat data, including participants and last messages,
     * and processes it asynchronously to include only active participants for each chat. The resulting list of chats
     * is transformed into the `Chat` domain model.
     *
     * @return A Flow emitting a list of `Chat` entities, where each entity represents a chat with filtered active participants
     *         and its associated last message.
     */
    override fun getChats(): Flow<List<Chat>> {
        return db.chatDao.getChatsWithParticipants()
            .map { allChatsWithParticipants ->
                supervisorScope {
                    allChatsWithParticipants
                        .map { chatWithParticipants ->
                            async {
                                ChatWithParticipants(
                                    chat = chatWithParticipants.chat,
                                    participants = chatWithParticipants
                                        .participants
                                        .onlyActive(chatWithParticipants.chat.chatId),
                                    lastMessage = chatWithParticipants.lastMessage
                                )
                            }
                        }
                        .awaitAll()
                        .map { it.toDomain() }
                }
            }
    }

    /**
     * Retrieves detailed information about a specific chat.
     *
     * This method fetches and transforms chat data from the database, including the chat's details,
     * active participants, and associated messages with senders. The data is emitted as a Flow
     * of `ChatInfo` objects, which reflect the current state of the underlying database.
     * Only active participants are considered in the result. The transformation involves mapping
     * local database entities to the application's domain model.
     *
     * @param chatId The unique identifier of the chat for which information is to be retrieved.
     * @return A Flow emitting `ChatInfo` objects, each containing the chat's details, active participants,
     *         and associated messages with senders.
     */
    override fun getChatInfoById(chatId: String): Flow<ChatInfo> {
        return db.chatDao.getChatInfoById(chatId)
            .filterNotNull()
            .map { chatInfo ->
                ChatInfoEntity(
                    chat = chatInfo.chat,
                    participants = chatInfo
                        .participants
                        .onlyActive(chatInfo.chat.chatId),
                    messagesWithSenders = chatInfo.messagesWithSenders
                )
            }
            .map { it.toDomain() }
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

    /**
     * Fetches a chat by its unique identifier and updates the local database with the retrieved data.
     *
     * This method retrieves the chat details from the remote `ChatService` and stores them in the local database.
     * It inserts or updates the chat, its participants, and the relationships between them using DAO operations.
     *
     * @param chatId The unique identifier of the chat to be fetched.
     * @return An `EmptyResult` object indicating either a successful completion or a `DataError.Remote`
     *         in case the fetch operation fails.
     */
    override suspend fun fetchChatById(chatId: String): EmptyResult<DataError.Remote> {
        return chatService
            .getChatById(chatId)
            .onSuccess { chat ->
                db.chatDao.upsertChatWithParticipantsAndCrossRefs(
                    chat = chat.toEntity(),
                    participants = chat.participants.map { it.toEntity() },
                    participantDao = db.chatParticipantDao,
                    crossRefDao = db.chatParticipantsCrossRefDao
                )
            }
            .asEmptyResult()
    }

    /**
     * Creates a new chat with the specified users and persists it in the local database.
     *
     * This method interacts with the remote `ChatService` to create a new chat involving the provided
     * user identifiers. If successful, the chat and its participants are stored locally, ensuring data
     * consistency between the remote service and the local database.
     *
     * @param otherUserIds A list of user IDs representing the participants to include in the new chat.
     * @return A `Result` containing the created `Chat` object if the creation is successful, or a
     *         `DataError.Remote` instance in case of a failure.
     */
    override suspend fun createChat(otherUserIds: List<String>): Result<Chat, DataError.Remote> {
        return chatService
            .createChat(otherUserIds)
            .onSuccess { chat ->
                db.chatDao.upsertChatWithParticipantsAndCrossRefs(
                    chat = chat.toEntity(),
                    participants = chat.participants.map { it.toEntity() },
                    participantDao = db.chatParticipantDao,
                    crossRefDao = db.chatParticipantsCrossRefDao
                )
            }
    }

    /**
     * Leaves the specified chat by its unique identifier.
     *
     * This method interacts with the remote `ChatService` to leave the given chat.
     * If the operation is successful, the chat is removed from the local database.
     *
     * @param chatId The unique identifier of the chat to be left.
     * @return An `EmptyResult` object indicating either a successful completion or a `DataError.Remote`
     *         in case the leave operation fails.
     */
    override suspend fun leaveChat(chatId: String): EmptyResult<DataError.Remote> {
        return chatService
            .leaveChat(chatId)
            .onSuccess {
                db.chatDao.deleteChatById(chatId)
            }
    }

    /**
     * Filters the list of chat participants to include only those who are active within a specified chat.
     *
     * This method compares the participants in the list against the active participants retrieved
     * from the database for the given chat ID. It returns a list containing only the participants
     * whose user IDs are present in the active participants list.
     *
     * @param chatId The unique identifier of the chat for which active participants are determined.
     * @return A list of `ChatParticipantEntity` objects containing only the active participants
     *         within the specified chat.
     */
    private suspend fun List<ChatParticipantEntity>.onlyActive(chatId: String): List<ChatParticipantEntity> {
        val activeParticipantIds = db
            .chatDao
            .getActiveParticipantsByChatId(chatId)
            .first()
            .map { it.userId }

        return this.filter { it.userId in activeParticipantIds }
    }
}