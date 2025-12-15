package dev.gaddal.chat.data.network

import dev.gaddal.chat.domain.models.ConnectionState

/**
 * Handles errors related to network connections by providing utilities for determining
 * the connection state associated with errors, transforming exceptions, and checking
 * if errors are retriable.
 */
actual class ConnectionErrorHandler {
    /**
     * Determines the appropriate connection state based on the provided error cause.
     * Maps specific exceptions or errors to the corresponding `ConnectionState` values.
     *
     * @param cause The throwable instance representing the cause of the connection error.
     *              This could be a network-related error or a generic exception indicating an issue.
     * @return A `ConnectionState` value indicating the state corresponding to the error,
     *         such as `ERROR_NETWORK`.
     */
    actual fun getConnectionStateForError(cause: Throwable): ConnectionState {
        return ConnectionState.ERROR_NETWORK
    }

    /**
     * Transforms the given exception into another exception, typically mapped to a more meaningful or domain-specific
     * exception type to better represent the error in the application's context.
     *
     * @param exception The original exception that needs to be transformed.
     * @return A transformed exception that provides a more specific or meaningful representation of the error.
     */
    actual fun transformException(exception: Throwable): Throwable {
        return exception
    }

    /**
     * Determines whether a given error can be retried based on the nature of the provided throwable.
     *
     * @param cause The throwable instance representing the error to be evaluated.
     * @return True if the error is considered retriable, false otherwise.
     */
    actual fun isRetriableError(cause: Throwable): Boolean {
        return true
    }
}