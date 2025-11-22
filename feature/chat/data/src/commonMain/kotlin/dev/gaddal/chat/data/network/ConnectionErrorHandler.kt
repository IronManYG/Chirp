package dev.gaddal.chat.data.network

import dev.gaddal.chat.domain.models.ConnectionState

/**
 * Handles errors related to network connections by providing utilities for determining
 * the connection state associated with errors, transforming exceptions, and checking
 * if errors are retriable.
 */
expect class ConnectionErrorHandler {
    /**
     * Determines the appropriate connection state based on the provided error cause.
     * Maps specific exceptions or errors to corresponding `ConnectionState` values.
     *
     * @param cause The throwable instance representing the cause of the connection error.
     *              This could be a specific exception indicating a known or unknown issue.
     * @return A `ConnectionState` value indicating the state corresponding to the error,
     *         such as `ERROR_NETWORK` or `ERROR_UNKNOWN`.
     */
    fun getConnectionStateForError(cause: Throwable): ConnectionState

    /**
     * Transforms the given exception into another exception, typically mapped to a more meaningful or domain-specific
     * exception type to better represent the error in the application's context.
     *
     * @param exception The original exception to be transformed.
     * @return A transformed exception that provides a more specific or meaningful representation of the error.
     */
    fun transformException(exception: Throwable): Throwable

    /**
     * Determines whether a given error can be retried based on the nature of the provided throwable.
     *
     * @param cause The throwable instance representing the error to be evaluated.
     * @return True if the error is considered retriable, false otherwise.
     */
    fun isRetriableError(cause: Throwable): Boolean
}