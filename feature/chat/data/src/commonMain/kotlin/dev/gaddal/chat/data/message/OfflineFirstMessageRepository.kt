package dev.gaddal.chat.data.message

import dev.gaddal.chat.database.ChirpChatDatabase
import dev.gaddal.chat.domain.message.MessageRepository
import dev.gaddal.chat.domain.models.ChatMessageDeliveryStatus
import dev.gaddal.core.data.database.safeDatabaseUpdate
import dev.gaddal.core.domain.util.DataError
import dev.gaddal.core.domain.util.EmptyResult
import kotlin.time.Clock

/**
 * An implementation of the `MessageRepository` interface that adheres to the Offline-First
 * strategy for managing message-related operations. This repository prioritizes database
 * updates for chat messages, ensuring local persistence even without network connectivity.
 *
 * @param database The database instance of `ChirpChatDatabase` used for accessing and updating
 * chat-related data, such as delivery statuses.
 */
class OfflineFirstMessageRepository(
    private val database: ChirpChatDatabase
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
}