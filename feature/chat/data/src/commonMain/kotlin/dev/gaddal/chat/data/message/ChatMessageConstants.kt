package dev.gaddal.chat.data.message

/**
 * Constants used for chat message operations.
 *
 * This object contains constant values that define configuration or fixed parameters
 * relevant to operations related to chat message retrieval or processing.
 */
object ChatMessageConstants {
    /**
     * The number of chat messages to be fetched per page during pagination.
     *
     * This constant is used to limit the number of messages retrieved in a single request
     * or batch operation, ensuring consistent and manageable data size for paging
     * through chat messages.
     */
    const val PAGE_SIZE = 20
}