package dev.gaddal.chat.domain.chat

import dev.gaddal.chat.domain.models.Chat
import dev.gaddal.chat.domain.models.ChatInfo
import dev.gaddal.core.domain.util.DataError
import dev.gaddal.core.domain.util.EmptyResult
import dev.gaddal.core.domain.util.Result
import kotlinx.coroutines.flow.Flow

/**
 * Interface for managing and accessing chat data.
 *
 * This repository provides methods to retrieve and fetch chat information, designed to support
 * both live data streams and one-time network operations. The usage of `Flow` allows handling of
 * real-time updates, and the `Result` type ensures reliable error handling for remote operations.
 */
interface ChatRepository {
    /**
     * Retrieves a stream of chats as a Flow.
     *
     * This method provides a reactive stream of data representing the list of chats
     * that the user is currently a participant in. It emits updates whenever there
     * are changes to the list of chats, allowing for real-time monitoring of chat
     * activity such as new messages or updated last activity timestamps.
     *
     * @return A Flow emitting a list of Chat objects, where each Chat in the list contains
     * details such as the participants, last activity timestamp, and last message.
     */
    fun getChats(): Flow<List<Chat>>

    /**
     * Retrieves detailed information about a specific chat, including the associated messages and participants.
     *
     * This method returns a `Flow` emitting updates for the chat specified by the given identifier.
     * The stream provides real-time updates whenever there are changes to the chat's data,
     * such as new messages or modifications to the chat participants.
     *
     * @param chatId The unique identifier of the chat whose information is to be retrieved.
     * @return A `Flow` emitting `ChatInfo` objects that contain the chat details and the list of associated messages.
     */
    fun getChatInfoById(chatId: String): Flow<ChatInfo>

    /**
     * Fetches an updated list of chats from a remote source.
     *
     * This method performs a network operation to retrieve the latest chat data and may
     * either return a successful result containing a list of chats or a failure result
     * with an associated error.
     *
     * @return A [Result] representing either a successful operation with a list of [Chat],
     *         or a failure with a [DataError.Remote] indicating the type of remote data error encountered.
     */
    suspend fun fetchChats(): Result<List<Chat>, DataError.Remote>

    /**
     * Fetches a specific chat by its unique identifier from a remote source.
     *
     * This method performs a network operation to retrieve information about
     * a chat identified by the given `chatId`. The operation may either succeed,
     * returning an empty successful result, or fail with an associated remote error.
     *
     * @param chatId The unique identifier of the chat to be fetched.
     * @return An [EmptyResult] indicating either a success with no payload or a failure
     *         with a [DataError.Remote] describing the error encountered.
     */
    suspend fun fetchChatById(chatId: String): EmptyResult<DataError.Remote>
}