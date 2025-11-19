package dev.gaddal.chat.data.network

import dev.gaddal.chat.domain.models.ConnectionState
import kotlinx.coroutines.CancellationException
import platform.Foundation.NSError
import platform.Foundation.NSURLErrorDomain
import platform.Foundation.NSURLErrorNetworkConnectionLost
import platform.Foundation.NSURLErrorNotConnectedToInternet
import platform.Foundation.NSURLErrorTimedOut

/**
 * Handles error processing related to network connections.
 * This class provides utilities for interpreting and transforming network-related exceptions,
 * and determining the retriability of errors. It is designed for use in iOS platforms
 * where exceptions related to the network layer may need to be handled specifically.
 */
actual class ConnectionErrorHandler {
    /**
     * Determines the connection state based on the provided error or exception.
     *
     * The function inspects the given error to identify a specific network-related issue
     * or an unknown cause. It utilizes extracted information about the error and maps it
     * to a predefined `ConnectionState`.
     *
     * @param cause The `Throwable` instance representing the error or exception from which
     * the connection state is to be determined.
     *
     * @return A `ConnectionState` value indicating the determined connection state. This could
     * be one of the following:
     * - `ConnectionState.ERROR_NETWORK`: If the error represents a network-related issue,
     *   such as no internet connection, connection lost, or timeout.
     * - `ConnectionState.ERROR_UNKNOWN`: If the error cause is unknown or not explicitly
     *   related to a network issue.
     */
    actual fun getConnectionStateForError(cause: Throwable): ConnectionState {
        val nsError = extractNsError(cause)

        return if (nsError != null) {
            when (nsError.code) {
                NSURLErrorNotConnectedToInternet,
                NSURLErrorNetworkConnectionLost,
                NSURLErrorTimedOut -> ConnectionState.ERROR_NETWORK

                else -> ConnectionState.ERROR_UNKNOWN
            }
        } else if (cause is IOSNetworkCancellationException) {
            ConnectionState.ERROR_NETWORK
        } else ConnectionState.ERROR_UNKNOWN
    }

    /**
     * Transforms the given exception based on its type and cause. If the exception is a `CancellationException`
     * and its cause matches specific network-related patterns, it is transformed into an `IOSNetworkCancellationException`.
     * Otherwise, the original exception is returned.
     *
     * @param exception The exception to be evaluated and potentially transformed.
     * @return The transformed exception if specific conditions are met, or the original exception otherwise.
     */
    actual fun transformException(exception: Throwable): Throwable {
        if (exception is CancellationException) {
            val cause = exception.cause ?: return exception
            val isDarwinException = cause.message?.contains("DarwinHttpRequestException") == true
            val isConnectionLostException =
                cause.message?.contains("NSURLErrorDomain Code=-1005") == true
            val isNotConnectedException =
                cause.message?.contains("NSURLErrorDomain Code=-1009") == true

            if (isDarwinException || isConnectionLostException || isNotConnectedException) {
                return IOSNetworkCancellationException(
                    message = "Network connection lost (extracted from cancellation)",
                    cause = cause
                )
            }
        }

        return exception
    }

    /**
     * Determines whether the provided error can be classified as retriable.
     * A retriable error indicates that the operation causing the error
     * can be safely retried.
     *
     * @param cause The throwable instance representing the error that occurred.
     * @return `true` if the error is retriable (e.g., related to network connectivity
     * issues such as lack of internet connection, network connection lost, or timeout),
     * `false` otherwise.
     */
    actual fun isRetriableError(cause: Throwable): Boolean {
        if (cause is IOSNetworkCancellationException) {
            return true
        }

        return when (extractNsError(cause)?.code) {
            NSURLErrorNotConnectedToInternet,
            NSURLErrorNetworkConnectionLost,
            NSURLErrorTimedOut -> true

            else -> false
        }
    }

    /**
     * Extracts the `NSError` from the given `Throwable`, if present. If the cause of the Throwable
     * or the Throwable itself is an instance of `NSError`, it will be returned. If neither
     * directly qualifies, the method will attempt to convert the Throwable and its cause to
     * an `NSError` using the `toNSError()` method.
     *
     * @param cause The Throwable from which to attempt to extract an `NSError`.
     * @return The extracted `NSError` if one exists, otherwise `null`.
     */
    private fun extractNsError(cause: Throwable): NSError? {
        val throwableCause = cause.cause
        if (throwableCause is NSError) {
            return throwableCause
        }

        if (cause is NSError) {
            return cause
        }

        val exceptionNsError = cause.toNSError()
        val causeNsError = cause.cause?.toNSError()

        return exceptionNsError ?: causeNsError
    }

    /**
     * Converts the current `Throwable` instance to an `NSError` if applicable, based on its message content.
     * Specifically, it checks for patterns indicating a "Not Connected to Internet" or "Network Connection Lost" error.
     *
     * @return An `NSError` instance representing the error if the message matches known patterns,
     *         or null if no corresponding `NSError` can be generated.
     */
    private fun Throwable.toNSError(): NSError? {
        return message?.let { message ->
            when {
                message.contains(NSURLErrorNotConnectedToInternetPattern) ->
                    return NSError.errorWithDomain(
                        domain = NSURLErrorDomain,
                        code = NSURLErrorNotConnectedToInternet,
                        userInfo = null
                    )

                message.contains(NSURLErrorNetworkConnectionLostPattern) ->
                    return NSError.errorWithDomain(
                        domain = NSURLErrorDomain,
                        code = NSURLErrorNetworkConnectionLost,
                        userInfo = null
                    )

                else -> null
            }
        }
    }

    /**
     * Companion object for the ConnectionErrorHandler class.
     * Contains constants used for pattern matching specific NSError messages
     * related to network connectivity issues.
     */
    companion object {
        /**
         * A pattern string that represents the error domain and code for the
         * "Not Connected to the Internet" network error on iOS. This constant is
         * used to match or identify error messages corresponding to the
         * `NSURLErrorDomain` with the `NSURLErrorNotConnectedToInternet` code.
         *
         * This pattern is primarily utilized for error handling and mapping
         * network-related issues to appropriate application-specific error states.
         */
        private val NSURLErrorNotConnectedToInternetPattern =
            "Error Domain=${NSURLErrorDomain} Code=${NSURLErrorNotConnectedToInternet}"

        /**
         * A constant representing the error pattern for network connection losses in the
         * NSError error domain.
         *
         * This pattern is used to identify errors specifically related to lost network connections
         * (e.g., when the network is interrupted or disconnected unexpectedly). The error corresponds
         * to the `NSURLErrorNetworkConnectionLost` code in the `NSURLErrorDomain`.
         *
         * It serves as a matchable string pattern for handling network-related errors on iOS platforms.
         */
        val NSURLErrorNetworkConnectionLostPattern =
            "Error Domain=${NSURLErrorDomain} Code=${NSURLErrorNetworkConnectionLost}"
    }
}