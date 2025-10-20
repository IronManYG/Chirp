package dev.gaddal.core.data.networking

import dev.gaddal.core.domain.util.DataError
import dev.gaddal.core.domain.util.Result
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse

/**
 * Executes a platform-specific safe HTTP call, allowing for consistent error handling and response processing.
 *
 * This function is designed to ensure safe execution of HTTP requests and the appropriate handling of responses
 * using a provided function to process the `HttpResponse`. It utilizes a `Result` wrapper to encapsulate either
 * the successful result of type `T` or an error of type `DataError.Remote`.
 *
 * @param T The type of the successful result.
 * @param execute A suspending function responsible for executing the HTTP request and returning an `HttpResponse`.
 * @param handleResponse A suspending function responsible for processing the `HttpResponse` and converting it into a
 * `Result<T, DataError.Remote>` representation where `T` indicates a successful outcome.
 * @return A `Result` wrapping the operation's success as type `T` or an instance of `DataError.Remote` in case of failure.
 */
expect suspend fun <T> platformSafeCall(
    execute: suspend () -> HttpResponse,
    handleResponse: suspend (HttpResponse) -> Result<T, DataError.Remote>
): Result<T, DataError.Remote>

/**
 * Executes a suspending function in a safe manner, encapsulating its result in a [Result] object.
 * The method ensures that potential failures during the execution are returned as an error,
 * instead of causing an unhandled exception.
 *
 * @param T The expected type of the successful result, determined dynamically through reified type.
 * @param execute A suspending function that performs the desired operation and returns an [HttpResponse].
 * @return A [Result] object representing the outcome of the operation. If successful, the result is of type [T].
 *         If an error occurs, it returns a [DataError.Remote] encapsulating the failure details.
 */
suspend inline fun <reified T> safeCall(
    noinline execute: suspend () -> HttpResponse
): Result<T, DataError.Remote> {
    return platformSafeCall(
        execute = execute
    ) { response ->
        responseToResult(response)
    }
}

/**
 * Converts an HTTP response into a `Result` object, indicating the success or failure of the operation.
 *
 * @param T The reified type to which the response body is deserialized in the success case.
 * @param response The HTTP response to be processed and converted into a result.
 * @return A `Result` object that contains the deserialized data of type `T` in case of success,
 * or a `DataError.Remote` error in case of failure.
 */
suspend inline fun <reified T> responseToResult(response: HttpResponse): Result<T, DataError.Remote> {
    return when (response.status.value) {
        // Success range: treat any 2xx as success and attempt to deserialize the body into T
        in 200..299 -> {
            try {
                Result.Success(response.body<T>())
            } catch (e: NoTransformationFoundException) {
                // Ktor was unable to deserialize the response into T (likely missing or mismatched serializer)
                Result.Failure(DataError.Remote.SERIALIZATION)
            }
        }

        400 -> Result.Failure(DataError.Remote.BAD_REQUEST)
        401 -> Result.Failure(DataError.Remote.UNAUTHORIZED)
        403 -> Result.Failure(DataError.Remote.FORBIDDEN)
        404 -> Result.Failure(DataError.Remote.NOT_FOUND)
        408 -> Result.Failure(DataError.Remote.REQUEST_TIMEOUT)
        413 -> Result.Failure(DataError.Remote.PAYLOAD_TOO_LARGE)
        429 -> Result.Failure(DataError.Remote.TOO_MANY_REQUESTS)
        500 -> Result.Failure(DataError.Remote.SERVER_ERROR)
        503 -> Result.Failure(DataError.Remote.SERVICE_UNAVAILABLE)
        else -> Result.Failure(DataError.Remote.UNKNOWN)
    }
}

/**
 * Constructs a complete API route by appending the base URL to the provided route if necessary.
 *
 * The method checks if the input route already contains the base URL or starts with a "/"
 * and adjusts it accordingly to ensure a proper full URL is returned.
 *
 * @param route The partial or full route string to be converted into a complete URL.
 * @return A valid complete API URL string derived from the provided route.
 */
fun constructRoute(route: String): String {
    // Ensures consistent URL construction; idempotent if a full URL is already provided
    return when {
        route.contains(UrlConstants.BASE_URL_HTTP) -> route
        route.startsWith("/") -> "${UrlConstants.BASE_URL_HTTP}$route"
        else -> "${UrlConstants.BASE_URL_HTTP}/$route"
    }
}