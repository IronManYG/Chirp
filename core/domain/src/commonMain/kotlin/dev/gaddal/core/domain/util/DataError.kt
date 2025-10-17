package dev.gaddal.core.domain.util

/**
 * Represents a sealed interface for categorizing data-related errors.
 *
 * This interface extends the `Error` type, providing a structured way of handling
 * errors that can occur in either remote or local contexts. It is divided into
 * two main categories:
 * - Remote errors: Errors originating from external sources such as network or API issues.
 * - Local errors: Errors resulting from issues within the device or application, e.g., disk constraints.
 */
sealed interface DataError: Error {
    /**
     * Represents remote error types that might occur during network or API operations.
     *
     * This enum is part of the `DataError` hierarchy and is useful for specifying
     * issues stemming from remote data sources, such as HTTP errors, connectivity
     * issues, or data serialization problems. Each value corresponds to a specific
     * kind of remote error commonly encountered in such scenarios.
     */
    enum class Remote: DataError {
        /**
         * Represents an HTTP 400 Bad Request error.
         *
         * This error occurs when the server is unable to process the request due to malformed syntax
         * or invalid data provided by the client. It typically indicates an issue with the client-side
         * input or request structure, making it impossible for the server to understand or fulfill the request.
         */
        BAD_REQUEST,
        /**
         * Represents a specific type of remote error that occurs when a request exceeds the allotted time limit.
         *
         * This error is commonly used to indicate a scenario where the client does not receive a response
         * from the server within an expected timeframe. It is typically associated with network delays
         * or server overloads that prevent timely processing of the request.
         */
        REQUEST_TIMEOUT,
        /**
         * Represents the "Unauthorized" error in the context of remote data interactions.
         *
         * This error occurs when the client is not authorized to perform the requested operation,
         * typically due to missing or invalid authentication credentials.
         *
         * It is part of the `Remote` category in the `DataError` hierarchy, which reflects errors
         * related to remote or server interactions.
         */
        UNAUTHORIZED,
        /**
         * Indicates that the request was understood by the server, but the client does not have permission
         * to access the requested resource or perform the requested action.
         *
         * This error typically corresponds to an HTTP 403 Forbidden response. Unlike `UNAUTHORIZED`, the
         * `FORBIDDEN` error implies that authorization has been verified but the client is not allowed
         * to proceed with the action.
         */
        FORBIDDEN,
        /**
         * Represents a remote error indicating that the requested resource could not be found.
         *
         * This error type is typically used when a resource, such as a file or endpoint,
         * is requested by the client but cannot be located on the server.
         */
        NOT_FOUND,
        /**
         * Represents a remote error indicating a resource conflict.
         *
         * This error occurs when a request could not be completed due to a conflict
         * with the current state of the resource. It typically corresponds to
         * HTTP 409 Conflict status code.
         */
        CONFLICT,
        /**
         * Represents an error that occurs when too many requests have been made in a short period of time.
         *
         * This error typically indicates that rate-limiting has been enforced by the remote server to prevent
         * abuse or overloading. It suggests the client should reduce the frequency of requests and may include
         * information about when further requests will be allowed.
         */
        TOO_MANY_REQUESTS,
        /**
         * Represents an error indicating that there is no internet connection available.
         *
         * This error type is part of the `Remote` enum within the `DataError` sealed interface.
         * It is typically used to signify failure cases where a network connection is required
         * but not available.
         */
        NO_INTERNET,
        /**
         * Represents an error indicating that the payload size exceeds the allowable limit for a request.
         *
         * This error typically occurs when a client sends data that is too large for the server to handle,
         * violating the server's payload size constraints. It is commonly used to signal situations where
         * requests need to be adjusted to comply with server limitations.
         */
        PAYLOAD_TOO_LARGE,
        /**
         * Represents a server-side error occurring during a remote operation.
         *
         * This error typically indicates an issue on the server that prevents
         * it from fulfilling a valid request, such as internal server errors or
         * unexpected server behavior.
         *
         * SERVER_ERROR is a type of `Remote` error under `DataError`.
         */
        SERVER_ERROR,
        /**
         * Represents a remote data error indicating that the requested service is currently unavailable.
         *
         * This error type is used to specify scenarios where the server cannot handle the request due to
         * temporary overloading or maintenance of the server. It typically maps to the HTTP 503 status code.
         */
        SERVICE_UNAVAILABLE,
        /**
         * Represents an error related to serialization or deserialization processes.
         *
         * This error is typically encountered when data cannot be properly serialized for
         * storage or transmission, or when incoming serialized data cannot be properly
         * deserialized into a usable format. It is commonly used to signify issues such as
         * invalid data format, corrupted data, or mismatched schema expectations.
         */
        SERIALIZATION,
        /**
         * Represents an unknown error within the `Remote` category of `DataError`.
         *
         * This error type typically corresponds to situations where the specific cause of the
         * error is not identifiable or does not fall under any predefined category. It can serve
         * as a fallback or default error type for handling unexpected cases in remote data operations.
         *
         * Use this error type when the nature of the failure is ambiguous or nonstandard,
         * but it still originates from a remote operation or communication.
         */
        UNKNOWN
    }

    /**
     * Represents specific types of locally scoped errors that can occur.
     *
     * This enumeration is part of the [`DataError`](DataError) hierarchy and is used to define
     * errors that originate from local systems or conditions, such as file storage or
     * resource access constraints. It is intended to provide a domain-specific way to
     * categorize and handle locally sourced errors.
     */
    enum class Local: DataError {
        /**
         * Represents a local error scenario where a disk is full, preventing further storage operations.
         *
         * This error typically occurs when there is insufficient disk space available for an operation,
         * such as saving or modifying data locally. It is part of the `Local` classification within the
         * `DataError` hierarchy, distinguishing it as a locally scoped error.
         */
        DISK_FULL,
        /**
         * Represents an error state indicating that the requested resource or data was not found.
         *
         * This error typically occurs in scenarios where a lookup or retrieval operation fails
         * to locate the specified resource in the local domain context.
         *
         * Common use cases:
         * - Handling situations where a required file or data is missing locally.
         * - Differentiating resource absence from other types of errors in results or failure handling.
         */
        NOT_FOUND,
        /**
         * Represents an unknown error scenario in the local error context.
         *
         * This error type is used to signify that an unexpected or unidentified local error has occurred.
         * It is part of the `DataError.Local` enumeration, which categorizes local error conditions.
         *
         * Usage of this error can help ensure that all local error cases are handled, even when the
         * specific nature of the error is unclear or not explicitly defined.
         */
        UNKNOWN
    }
}