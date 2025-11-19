package dev.gaddal.chat.domain.message

import dev.gaddal.chat.domain.models.ChatMessageDeliveryStatus
import dev.gaddal.core.domain.util.DataError
import dev.gaddal.core.domain.util.EmptyResult

/**
 * Defines a repository interface for managing chat messages, particularly for handling their delivery statuses.
 *
 * This interface is typically used to abstract the data operations related to chat messages,
 * allowing for an implementation-independent approach to manage these operations in a system.
 */
interface MessageRepository {
    /**
     * Updates the delivery status of a chat message specified by the messageId.
     *
     * This method is responsible for changing the delivery status of a message
     * in the underlying data source, facilitating status tracking for messages
     * sent or received in a conversation.
     *
     * @param messageId The unique identifier of the message whose delivery status is to be updated.
     * @param status The new delivery status to be assigned to the message. It should be one of
     * the values defined in the `ChatMessageDeliveryStatus` enum.
     * @return An `EmptyResult` indicating the success or failure of the operation. On failure,
     * it contains an instance of `DataError.Local` to represent the specific local error encountered.
     */
    suspend fun updateMessageDeliveryStatus(
        messageId: String,
        status: ChatMessageDeliveryStatus
    ): EmptyResult<DataError.Local>
}