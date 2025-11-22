package dev.gaddal.chat.data.network

/**
 * A custom exception class that represents a specific type of exception occurring on iOS
 * related to network request cancellations.
 *
 * This exception is typically thrown when a network operation is intentionally
 * canceled on an iOS platform. It allows for handling such cases distinctly from
 * other types of exceptions.
 *
 * @param message A descriptive message providing details about the exception.
 * @param cause The underlying cause of the exception, if any.
 */
class IOSNetworkCancellationException(
    message: String,
    cause: Throwable?
) : Exception(message, cause)