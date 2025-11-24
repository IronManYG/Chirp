package dev.gaddal.chat.data.message

import dev.gaddal.chat.data.dto.websocket.OutgoingWebSocketDto
import dev.gaddal.chat.data.dto.websocket.WebSocketMessageDto
import dev.gaddal.chat.data.mappers.toDomain
import dev.gaddal.chat.data.mappers.toEntity
import dev.gaddal.chat.data.mappers.toWebSocketDto
import dev.gaddal.chat.data.network.KtorWebSocketConnector
import dev.gaddal.chat.database.ChirpChatDatabase
import dev.gaddal.chat.domain.message.ChatMessageService
import dev.gaddal.chat.domain.message.MessageRepository
import dev.gaddal.chat.domain.models.ChatMessage
import dev.gaddal.chat.domain.models.ChatMessageDeliveryStatus
import dev.gaddal.chat.domain.models.MessageWithSender
import dev.gaddal.chat.domain.models.OutgoingNewMessage
import dev.gaddal.core.data.database.safeDatabaseUpdate
import dev.gaddal.core.domain.auth.SessionStorage
import dev.gaddal.core.domain.util.DataError
import dev.gaddal.core.domain.util.EmptyResult
import dev.gaddal.core.domain.util.Result
import dev.gaddal.core.domain.util.onFailure
import dev.gaddal.core.domain.util.onSuccess
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlin.time.Clock

/**
 * An implementation of the `MessageRepository` interface that adheres to the Offline-First
 * strategy for managing message-related operations. This repository prioritizes database
 * updates for chat messages, ensuring local persistence even without network connectivity.
 *
 * @param database The database instance of `ChirpChatDatabase` used for accessing and updating
 * chat-related data, such as delivery statuses.
 * @param chatMessageService The service responsible for fetching chat messages from remote sources.
 */
class OfflineFirstMessageRepository(
    private val database: ChirpChatDatabase,
    private val chatMessageService: ChatMessageService,
    private val sessionStorage: SessionStorage,
    private val json: Json,
    private val webSocketConnector: KtorWebSocketConnector,
    private val applicationScope: CoroutineScope
) : MessageRepository {

    /**
     * Updates the delivery status of a chat message in the local database.
     *
     * This method modifies the status of a message identified by its unique ID,
     * setting it to the specified delivery status along with the current timestamp.
     *
     * @param messageId The unique identifier of the message whose delivery status is being updated.
     * @param status The new delivery status to be applied, represented by the `ChatMessageDeliveryStatus` enum.
     * @return An `EmptyResult` indicating success or failure of the operation.
     *         In case of failure, it returns a `DataError.Local`.
     */
    override suspend fun updateMessageDeliveryStatus(
        messageId: String,
        status: ChatMessageDeliveryStatus
    ): EmptyResult<DataError.Local> {
        return safeDatabaseUpdate {
            database.chatMessageDao.updateDeliveryStatus(
                messageId = messageId,
                status = status.name,
                timestamp = Clock.System.now().toEpochMilliseconds()
            )
        }
    }

    /**
     * Fetches a list of chat messages for a specified chat and optional pagination cursor.
     *
     * This method interacts with a remote service to retrieve messages associated with the given
     * chat ID. If provided, the `before` parameter specifies the cursor for pagination to fetch
     * older messages. Upon successful retrieval, the messages are cached in the local database,
     * with optional synchronization for the most recent messages when `before` is not set.
     *
     * @param chatId The unique identifier of the chat for which messages are being fetched.
     * @param before An optional cursor indicating the point before which messages should be fetched.
     *               If null, the most recent messages are fetched.
     * @return A `Result` containing either a list of `ChatMessage` objects on success,
     *         or a `DataError` on failure.
     */
    override suspend fun fetchMessages(
        chatId: String,
        before: String?
    ): Result<List<ChatMessage>, DataError> {
        return chatMessageService
            .fetchMessages(chatId, before)
            .onSuccess { messages ->
                return safeDatabaseUpdate {
                    database.chatMessageDao.upsertMessagesAndSyncIfNecessary(
                        chatId = chatId,
                        serverMessages = messages.map { it.toEntity() },
                        pageSize = ChatMessageConstants.PAGE_SIZE,
                        shouldSync = before == null // Only sync for most recent page
                    )
                    messages
                }
            }
    }

    /**
     * Sends a new outgoing message in a chat.
     *
     * This method handles the process of sending a message by converting it to the appropriate
     * WebSocket DTO, persisting it locally with a "sending" status, and transmitting it via
     * WebSocket. In case of transmission failure, the delivery status is updated to "failed".
     *
     * @param message The `OutgoingNewMessage` instance containing the details of the message to be sent.
     *                Includes the chat ID, message ID, and content of the message.
     * @return An `EmptyResult` which signifies success or failure of the operation. In case of failure,
     *         a `DataError` is returned describing the nature of the error.
     */
    override suspend fun sendMessage(message: OutgoingNewMessage): EmptyResult<DataError> {
        return safeDatabaseUpdate {
            val dto = message.toWebSocketDto()

            val localUser = sessionStorage.observeAuthInfo().first()?.user
                ?: return Result.Failure(DataError.Local.NOT_FOUND)

            val entity = dto.toEntity(
                senderId = localUser.id,
                deliveryStatus = ChatMessageDeliveryStatus.SENDING
            )
            database.chatMessageDao.upsertMessage(entity)

            return webSocketConnector
                .sendMessage(dto.toJsonPayload())
                .onFailure { error ->
                    applicationScope.launch {
                        database.chatMessageDao.updateDeliveryStatus(
                            messageId = entity.messageId,
                            timestamp = Clock.System.now().toEpochMilliseconds(),
                            status = ChatMessageDeliveryStatus.FAILED.name
                        )
                    }.join()
                }
        }
    }

    /**
     * Retries sending a message identified by its unique ID.
     *
     * This method attempts to resend a message that previously failed to send. It updates the delivery
     * status of the message in the local database to "sending" and then attempts to transmit the message
     * via WebSocket. If the resend fails, the delivery status is updated to "failed".
     *
     * @param messageId The unique identifier of the message to be retried.
     * @return An `EmptyResult` object indicating success or failure of the operation. On failure, the
     *         result contains a `DataError` providing details of the error.
     */
    override suspend fun retryMessage(messageId: String): EmptyResult<DataError> {
        return safeDatabaseUpdate {
            println("Message ID retry $messageId")
            val message = database.chatMessageDao.getMessageById(messageId)
                ?: return Result.Failure(DataError.Local.NOT_FOUND)

            database.chatMessageDao.updateDeliveryStatus(
                messageId = messageId,
                timestamp = Clock.System.now().toEpochMilliseconds(),
                status = ChatMessageDeliveryStatus.SENDING.name
            )

            val outgoingNewMessage = OutgoingWebSocketDto.NewMessage(
                chatId = message.chatId,
                messageId = messageId,
                content = message.content
            )
            return webSocketConnector
                .sendMessage(outgoingNewMessage.toJsonPayload())
                .onFailure { error ->
                    applicationScope.launch {
                        database.chatMessageDao.updateDeliveryStatus(
                            messageId = messageId,
                            timestamp = Clock.System.now().toEpochMilliseconds(),
                            status = ChatMessageDeliveryStatus.FAILED.name
                        )
                    }.join()
                }
        }
    }

    /**
     * Retrieves a flow of messages for a specific chat, mapping them to their domain representation.
     *
     * This method fetches all messages associated with the provided chat ID from the local database.
     * The messages are then transformed into a list of domain models, including the message details,
     * sender information, and their delivery status.
     *
     * @param chatId The unique identifier of the chat whose messages are to be retrieved.
     * @return A Flow emitting a list of MessageWithSender domain objects representing the messages and their associated sender information.
     */
    override fun getMessagesForChat(chatId: String): Flow<List<MessageWithSender>> {
        return database
            .chatMessageDao
            .getMessagesByChatId(chatId)
            .map { messages ->
                messages.map { it.toDomain() }
            }
    }

    /**
     * Converts the `OutgoingWebSocketDto.NewMessage` instance into a JSON payload string.
     *
     * This method serializes the `NewMessage` object into a JSON string
     * by wrapping its data into a `WebSocketMessageDto` object, which provides
     * the type and payload structure required for WebSocket communication.
     *
     * @return A JSON string representing the serialized `WebSocketMessageDto` with the encapsulated `NewMessage` details.
     */
    private fun OutgoingWebSocketDto.NewMessage.toJsonPayload(): String {
        val webSocketMessage = WebSocketMessageDto(
            type = type.name,
            payload = json.encodeToString(this)
        )
        return json.encodeToString(webSocketMessage)
    }
}