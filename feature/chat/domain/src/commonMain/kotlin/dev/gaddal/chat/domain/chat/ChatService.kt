package dev.gaddal.chat.domain.chat

import dev.gaddal.chat.domain.models.Chat
import dev.gaddal.core.domain.util.DataError
import dev.gaddal.core.domain.util.Result

/**
 * Defines the contract for interacting with chat functionality, including methods for creating and retrieving chats.
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

    /**
     * Retrieves the list of existing chats for the current user.
     *
     * This method fetches all chats that the current user is a participant of,
     * along with details such as participants, last activity, and the latest message.
     * The result will either be a success containing the list of chats or a failure
     * with a remote data error.
     *
     * @return A `Result` containing either a `List` of `Chat` objects or a `DataError.Remote`
     * indicating an error during the retrieval process.
     */
    suspend fun getChats(): Result<List<Chat>, DataError.Remote>
}