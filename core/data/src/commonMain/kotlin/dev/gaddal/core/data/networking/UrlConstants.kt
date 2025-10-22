package dev.gaddal.core.data.networking


/**
 * Holds constants related to network URL configurations used across the application.
 */
object UrlConstants {
    /**
     * The base URL used for HTTP requests in the application.
     * This constant serves as the root endpoint for all API interactions within the networking layer.
     * It is used to construct full API routes by appending specific endpoints to this base URL.
     */
    // Note: no trailing slash to simplify route concatenation in constructRoute()
    const val BASE_URL_HTTP = "https://chirp.pl-coding.com/api"
}