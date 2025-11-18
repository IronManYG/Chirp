package dev.gaddal.chat.domain.chat

import dev.gaddal.chat.domain.models.Chat
import dev.gaddal.core.domain.util.DataError
import dev.gaddal.core.domain.util.EmptyResult
import dev.gaddal.core.domain.util.Result

/**
 * Defines the contract for interacting with chat functionality, including methods for creating, retrieving and updating chats.
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

    /**
     * Retrieves a specific chat by its unique identifier.
     *
     * This method fetches the details of a chat identified by the provided `chatId`.
     * The result will either be a successful retrieval of the `Chat` object or
     * a failure represented by a `DataError.Remote` indicating the issue.
     *
     * @param chatId The unique identifier of the chat to be retrieved.
     * @return A `Result` containing either the requested `Chat` object or a `DataError.Remote` in case of an error.
     */
    suspend fun getChatById(chatId: String): Result<Chat, DataError.Remote>

    /**
     * Leaves the specified chat for the current user.
     *
     * This method allows the current user to exit a chat identified by the given `chatId`.
     * If the operation is successful, an empty result is returned.
     * In case of failure, a `DataError.Remote` is returned to indicate the issue.
     *
     * @param chatId The unique identifier of the chat to leave.
     * @return An `EmptyResult` which represents either successful completion or a `DataError.Remote` in case of failure.
     */
    suspend fun leaveChat(chatId: String): EmptyResult<DataError.Remote>

    /**
     * Adds participants to an existing chat.
     *
     * This method allows adding a list of users, identified by their unique user IDs,
     * to an existing chat identified by its unique chat ID. If the operation is successful,
     * the updated `Chat` object will be returned. In case of failure, a `DataError.Remote`
     * will indicate the issue.
     *
     * @param chatId The unique identifier of the chat to which participants will be added.
     * @param userIds A list of unique user IDs representing the participants to add to the chat.
     * @return A `Result` containing either the updated `Chat` object or a `DataError.Remote` indicating an error.
     */
    suspend fun addParticipantsToChat(
        chatId: String,
        userIds: List<String>
    ): Result<Chat, DataError.Remote>
}