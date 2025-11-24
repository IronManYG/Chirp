package dev.gaddal.core.domain.util

import kotlinx.coroutines.ensureActive
import kotlin.coroutines.coroutineContext

/**
 * A utility class to manage pagination logic for asynchronous data loading.
 * It handles states such as the current key for fetching data, prevents duplicate
 * requests, and provides hooks for success, error handling, and loading state updates.
 *
 * @param Key The type representing the pagination key.
 * @param Item The type of the individual items being paginated.
 * @property initialKey The initial key to start fetching data.
 * @property onLoadUpdated A callback to be invoked with the updated loading state.
 * @property onRequest A function to perform the data-fetching operation given the next key.
 * @property getNextKey A function to determine the next pagination key based on the loaded items.
 * @property onError A callback to handle errors that occur during the loading process.
 * @property onSuccess A callback to handle successful data fetches, passing the items and new key.
 */
class Paginator<Key, Item>(
    private val initialKey: Key,
    private val onLoadUpdated: (Boolean) -> Unit,
    private val onRequest: suspend (nextKey: Key) -> Result<List<Item>, DataError>,
    private val getNextKey: suspend (List<Item>) -> Key,
    private val onError: suspend (Throwable?) -> Unit,
    private val onSuccess: suspend (items: List<Item>, newKey: Key) -> Unit
) {
    /**
     * Tracks the current key being used for pagination.
     *
     * This variable holds the current state of the pagination key, which is used
     * to load the next set of items in a paginated data flow. It is initialized
     * with `initialKey` and is updated dynamically as the pagination process progresses.
     *
     * The value of `currentKey` is reset to `initialKey` whenever the `reset` function
     * of the containing paginator is invoked. It also serves as a safeguard to prevent
     * repeated requests for the same data by comparing it with `lastRequestKey`.
     */
    private var currentKey = initialKey

    /**
     * A flag indicating whether a request operation is currently in progress.
     *
     * This variable is used to prevent concurrent requests and to ensure that
     * only one request is being processed at any given time. When set to `true`,
     * it signifies that a request is actively being handled. Once the request is
     * completed, the value is reset to `false`.
     */
    private var isMakingRequest = false

    /**
     * Tracks the key associated with the most recent request made during pagination.
     *
     * This variable is used to ensure that duplicate requests are not made for the same key.
     * It is updated whenever a new request is initiated and compared against the current key
     * to decide whether a subsequent request should proceed.
     *
     * A `null` value indicates that no request has been made yet or that the paginator
     * has been reset.
     */
    private var lastRequestKey: Key? = null

    /**
     * Loads the next set of items in a paginated manner, updating the state and
     * handling success or failure scenarios.
     *
     * This method uses the current key to request the next batch of items while
     * preventing concurrent requests. It manages the loading state, processes
     * the retrieved data, and calculates the next key for subsequent requests.
     *
     * Key responsibilities:
     * - Ensures that no duplicate or concurrent requests are issued.
     * - Updates the loading state via `onLoadUpdated`.
     * - Fetches data using the `onRequest` lambda.
     * - Handles successful responses with `onSuccess`.
     * - Handles errors or exceptions with `onError`.
     *
     * Behavior:
     * - Exits early if a request is already in progress (`isMakingRequest`) or if
     *   the current key matches the last successfully requested key.
     * - Resolves the next key using the `getNextKey` lambda after successful data loading.
     * - Restores state and resets the loading flag in the event of an error or upon completion.
     *
     * Exceptions:
     * - In case of an operation being cancelled, the coroutine's active state is
     *   checked to ensure correct continuation handling.
     */
    suspend fun loadNextItems() {
        if (isMakingRequest) {
            return
        }

        if (currentKey != null && currentKey == lastRequestKey) {
            return
        }

        isMakingRequest = true
        onLoadUpdated(true)

        try {
            onRequest(currentKey)
                .onSuccess { items ->
                    val newKey = getNextKey(items)
                    onSuccess(items, newKey)
                    lastRequestKey = currentKey

                    currentKey = newKey
                }
                .onFailure { error ->
                    onError(DataErrorException(error))
                }
        } catch (e: Exception) {
            coroutineContext.ensureActive()

            onError(e)
        } finally {
            onLoadUpdated(false)
            isMakingRequest = false
        }
    }

    /**
     * Resets the paginator to its initial state by:
     * - Reverting `currentKey` to the value of `initialKey`.
     * - Clearing the `lastRequestKey` to be `null`.
     *
     * This function is typically used when restarting the pagination process,
     * ensuring that prior state or progress is discarded.
     */
    fun reset() {
        currentKey = initialKey
        lastRequestKey = null
    }
}