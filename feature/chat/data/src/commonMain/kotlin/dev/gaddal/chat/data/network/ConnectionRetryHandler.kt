package dev.gaddal.chat.data.network

import kotlinx.coroutines.delay
import kotlin.math.pow

/**
 * Handles retry logic for connection-related operations. This class determines whether a retry
 * is needed for a given error and implements an exponential backoff strategy for retry delays.
 *
 * @property connectionErrorHandler Provides utilities to determine if an error is retriable
 * and assists in handling connection-related errors.
 */
class ConnectionRetryHandler(
    private val connectionErrorHandler: ConnectionErrorHandler
) {
    /**
     * Indicates whether the retry mechanism should skip the backoff delay for the current attempt.
     *
     * When set to `true`, the backoff delay will be bypassed, allowing subsequent retries to proceed
     * immediately without waiting. It is typically reset to `false` after being used to ensure the
     * backoff logic is applied during subsequent retries unless explicitly overridden again.
     */
    private var shouldSkipBackoff = false

    /**
     * Determines whether a retry should be attempted for a failed operation based on the provided error and attempt count.
     *
     * @param cause The throwable representing the error encountered in the operation.
     * @param attempt The current attempt number, typically used to limit retries or influence backoff logic.
     * @return True if the operation should be retried based on the nature of the error, false otherwise.
     */
    fun shouldRetry(cause: Throwable, attempt: Long): Boolean {
        return connectionErrorHandler.isRetriableError(cause)
    }

    /**
     * Applies a retry delay based on the current attempt number. If backoff is not skipped, it calculates a backoff delay
     * using an exponential backoff strategy and then delays execution for the calculated duration. If backoff is skipped,
     * it resets the skip flag without introducing any delay.
     *
     * @param attempt The current retry attempt number. This value is used to calculate the backoff delay.
     */
    suspend fun applyRetryDelay(attempt: Long) {
        if (!shouldSkipBackoff) {
            val delay = createBackoffDelay(attempt)
            delay(delay)
        } else {
            shouldSkipBackoff = false
        }
    }

    /**
     * Resets the retry delay mechanism by setting the `shouldSkipBackoff` flag to true.
     * This ensures that any subsequent retry attempt will skip the backoff delay.
     */
    fun resetDelay() {
        shouldSkipBackoff = true
    }

    /**
     * Calculates a backoff delay based on the given retry attempt.
     * The delay is determined using exponential backoff logic, with a capped maximum delay.
     *
     * @param attempt The current retry attempt, where the delay increases with each subsequent attempt.
     * @return The calculated delay in milliseconds, capped at a maximum value.
     */
    private fun createBackoffDelay(attempt: Long): Long {
        val delayTime = (2f.pow(attempt.toInt()) * 2000L).toLong()
        val maxDelay = 30_000L
        return minOf(delayTime, maxDelay)
    }
}