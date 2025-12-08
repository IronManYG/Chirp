package dev.gaddal.chat.data.chat

import dev.gaddal.chat.data.dto.websocket.IncomingWebSocketDto
import dev.gaddal.chat.data.dto.websocket.IncomingWebSocketType
import dev.gaddal.chat.data.dto.websocket.WebSocketMessageDto
import dev.gaddal.chat.data.mappers.toDomain
import dev.gaddal.chat.data.mappers.toEntity
import dev.gaddal.chat.data.network.KtorWebSocketConnector
import dev.gaddal.chat.database.ChirpChatDatabase
import dev.gaddal.chat.domain.chat.ChatConnectionClient
import dev.gaddal.chat.domain.chat.ChatRepository
import dev.gaddal.core.domain.auth.SessionStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn
import kotlinx.serialization.json.Json

/**
 * A WebSocket-based client implementation for managing chat connections, message streaming,
 * and message handling within a chat application.
 *
 * This class is responsible for:
 * - Establishing and maintaining a WebSocket connection.
 * - Streaming incoming chat messages and handling various WebSocket events.
 * - Sending chat messages over the WebSocket connection.
 * - Updating local data stores such as the database and session storage in response to WebSocket events.
 *
 * @property webSocketConnector The WebSocket connector used for managing the WebSocket connection.
 * @property chatRepository Handles chat-related data operations and interactions with the local repository.
 * @property database The local database instance used for storing and querying persistent data.
 * @property sessionStorage Manages session-related storage required during WebSocket interactions.
 * @property json JSON serializer/deserializer for encoding/decoding message payloads.
 * @property applicationScope The coroutine scope in which background operations and message streaming are managed.
 */
class WebSocketChatConnectionClient(
    private val webSocketConnector: KtorWebSocketConnector,
    private val chatRepository: ChatRepository,
    private val database: ChirpChatDatabase,
    private val sessionStorage: SessionStorage,
    private val json: Json,
    private val applicationScope: CoroutineScope
) : ChatConnectionClient {

    /**
     * A flow that represents a stream of incoming chat messages retrieved from a WebSocket connection,
     * processed, and transformed into domain model instances of `ChatMessage`.
     *
     * The flow operates as follows:
     * - Collects incoming WebSocket messages from `webSocketConnector.messages`.
     * - Parses each message into an appropriate `IncomingWebSocketDto` instance using `parseIncomingMessage`.
     * - Handles the parsed message using `handleIncomingMessage`.
     * - Filters for messages of type `NewMessageDto`, representing new chat messages.
     * - Fetches the corresponding `ChatMessageEntity` from the database using the message ID
     *   provided by `NewMessageDto` and converts it to the domain model `ChatMessage` using `toDomain`.
     * - Shares the resulting flow in `applicationScope` with `SharingStarted.WhileSubscribed`.
     *
     * This property enables reactive streaming of new chat messages and ensures efficient resource usage
     * by leveraging operators such as `mapNotNull`, `filterIsInstance`, and `shareIn`.
     */
    override val chatMessages = webSocketConnector
        .messages
        .mapNotNull { parseIncomingMessage(it) }
        .onEach { handleIncomingMessage(it) }
        .filterIsInstance<IncomingWebSocketDto.NewMessageDto>()
        .mapNotNull {
            database.chatMessageDao.getMessageById(it.id)?.toDomain()
        }
        .shareIn(
            applicationScope,
            SharingStarted.WhileSubscribed(5000)
        )

    /**
     * Represents the current state of the WebSocket connection.
     *
     * This property retrieves the connection state from the underlying `webSocketConnector`.
     * It allows monitoring of the connection's status, which can be used to determine
     * whether the WebSocket is connected, connecting, or disconnected.
     *
     * Possible states can include connection statuses such as connected, connecting, or disconnected,
     * depending on the implementation of `webSocketConnector.connectionState`.
     */
    override val connectionState = webSocketConnector.connectionState

    /**
     * Parses an incoming WebSocket message and converts it into a specific subtype of [IncomingWebSocketDto].
     * The message type is used to determine the appropriate subtype for deserialization.
     *
     * @param message The WebSocket message to be parsed. Contains the type and serialized payload.
     * @return An instance of [IncomingWebSocketDto] representing the parsed message, or null if the type is unsupported.
     */
    private fun parseIncomingMessage(message: WebSocketMessageDto): IncomingWebSocketDto? {
        return when (message.type) {
            IncomingWebSocketType.NEW_MESSAGE.name -> {
                json.decodeFromString<IncomingWebSocketDto.NewMessageDto>(message.payload)
            }

            IncomingWebSocketType.MESSAGE_DELETED.name -> {
                json.decodeFromString<IncomingWebSocketDto.MessageDeletedDto>(message.payload)
            }

            IncomingWebSocketType.PROFILE_PICTURE_UPDATED.name -> {
                json.decodeFromString<IncomingWebSocketDto.ProfilePictureUpdated>(message.payload)
            }

            IncomingWebSocketType.CHAT_PARTICIPANTS_CHANGED.name -> {
                json.decodeFromString<IncomingWebSocketDto.ChatParticipantsChangedDto>(message.payload)
            }

            else -> null
        }
    }

    /**
     * Processes an incoming WebSocket message and delegates it to the appropriate handler
     * based on the specific type of the message.
     *
     * @param message The incoming WebSocket message represented by an instance of [IncomingWebSocketDto].
     *                It can be one of the following types:
     *                - [IncomingWebSocketDto.ChatParticipantsChangedDto]: Indicates that chat participants have changed.
     *                - [IncomingWebSocketDto.MessageDeletedDto]: Represents the deletion of a message.
     *                - [IncomingWebSocketDto.NewMessageDto]: Represents a new message in the chat.
     *                - [IncomingWebSocketDto.ProfilePictureUpdated]: Represents a profile picture update for a user.
     */
    private suspend fun handleIncomingMessage(message: IncomingWebSocketDto) {
        when (message) {
            is IncomingWebSocketDto.ChatParticipantsChangedDto -> refreshChat(message)
            is IncomingWebSocketDto.MessageDeletedDto -> deleteMessage(message)
            is IncomingWebSocketDto.NewMessageDto -> handleNewMessage(message)
            is IncomingWebSocketDto.ProfilePictureUpdated -> updateProfilePicture(message)
        }
    }

    /**
     * Updates the local representation of a chat when the participants in the chat have changed.
     *
     * This method is triggered by a WebSocket event indicating that the list of participants
     * in a specific chat has been modified. It fetches the updated chat data from a remote source.
     *
     * @param message The WebSocket message containing the `chatId` of the chat whose participants have changed.
     */
    private suspend fun refreshChat(message: IncomingWebSocketDto.ChatParticipantsChangedDto) {
        chatRepository.fetchChatById(message.chatId)
    }

    /**
     * Deletes a chat message based on the information provided in the deleted message event.
     *
     * @param message The data transfer object containing the details of the deleted message,
     *                including its unique message identifier and the chat it belongs to.
     */
    private suspend fun deleteMessage(message: IncomingWebSocketDto.MessageDeletedDto) {
        database.chatMessageDao.deleteMessageById(message.messageId)
    }

    /**
     * Handles the processing of a new message received via WebSocket.
     *
     * This method verifies if the chat specified by the message exists in the database.
     * If the chat does not exist, it fetches the chat data from a remote source. It then
     * converts the incoming message DTO into a database entity and inserts or updates
     * the message in the database accordingly.
     *
     * @param message The data transfer object representing the new message received via WebSocket.
     */
    private suspend fun handleNewMessage(message: IncomingWebSocketDto.NewMessageDto) {
        val chatExists = database.chatDao.getChatById(message.chatId) != null
        if (!chatExists) {
            chatRepository.fetchChatById(message.chatId)
        }

        val entity = message.toEntity()
        database.chatMessageDao.upsertMessage(entity)
    }

    /**
     * Handles the update of a user's profile picture by updating the corresponding data
     * in the database and session storage.
     *
     * This method first updates the profile picture URL for the specified user in the
     * database. If authentication information is available in session storage, it also
     * updates the profile picture URL within the in-memory session data.
     *
     * @param message The WebSocket message containing the user ID and the new profile picture URL.
     *                The `newUrl` may be null if the profile picture is being removed.
     */
    private suspend fun updateProfilePicture(message: IncomingWebSocketDto.ProfilePictureUpdated) {
        database.chatParticipantDao.updateProfilePictureUrl(
            userId = message.userId,
            newUrl = message.newUrl
        )

        val authInfo = sessionStorage.observeAuthInfo().firstOrNull()
        if (authInfo != null && authInfo.user.id == message.userId) {
            sessionStorage.set(
                info = authInfo.copy(
                    user = authInfo.user.copy(
                        profilePictureUrl = message.newUrl
                    )
                )
            )
        }
    }
}