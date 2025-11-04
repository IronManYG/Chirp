package dev.gaddal.chat.domain.chat

import dev.gaddal.chat.domain.models.Chat
import dev.gaddal.core.domain.util.DataError
import dev.gaddal.core.domain.util.Result

/**
 * Defines the contract for interacting with chat functionality, including the creation of new chat conversations.
 */
interface ChatService {
    /**
     * Creates a new chat with the specified participants.
     *
     * This method initiates a chat session involving the current user and a list of other users
     * identified by their unique IDs. If the operation is successful, the resulting `Chat` object
     * is returned. In case of failure, a `DataError.Remote` error is returned to indicate the issue.
     *
     * @param otherUserIds A list of unique IDs representing the users to include in the new chat session.
     * @return A `Result` containing either the successfully created `Chat` object or a `DataError.Remote` indicating an error.
     */
    suspend fun createChat(
        otherUserIds: List<String>
    ): Result<Chat, DataError.Remote>
}