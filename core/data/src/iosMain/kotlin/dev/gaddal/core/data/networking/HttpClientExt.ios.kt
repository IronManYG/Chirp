package dev.gaddal.core.data.networking

import dev.gaddal.core.domain.util.DataError
import dev.gaddal.core.domain.util.Result
import io.ktor.client.engine.darwin.DarwinHttpRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.statement.HttpResponse
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.ensureActive
import kotlinx.serialization.SerializationException
import platform.Foundation.NSURLErrorCallIsActive
import platform.Foundation.NSURLErrorCannotFindHost
import platform.Foundation.NSURLErrorDNSLookupFailed
import platform.Foundation.NSURLErrorDataNotAllowed
import platform.Foundation.NSURLErrorDomain
import platform.Foundation.NSURLErrorInternationalRoamingOff
import platform.Foundation.NSURLErrorNetworkConnectionLost
import platform.Foundation.NSURLErrorNotConnectedToInternet
import platform.Foundation.NSURLErrorResourceUnavailable
import platform.Foundation.NSURLErrorTimedOut
import kotlin.coroutines.coroutineContext

/**
 * Executes a platform-specific safe HTTP operation with error handling.
 * This function attempts to execute the provided API call and process its response. It handles several common
 * exceptions, mapping them to a `Result` indicating either a successful response or an error.
 *
 * @param T The type of the data expected in a successful result.
 * @param execute A suspending lambda function that performs the HTTP request and returns an `HttpResponse`.
 * @param handleResponse A suspending lambda function that processes the `HttpResponse`
 * and returns either a successful `Result` containing data of type `T` or an error of type `DataError.Remote`.
 * @return A `Result` object either containing the successfully processed data of type `T`
 * or encapsulating a `DataError.Remote` describing the failure.
 */
actual suspend fun <T> platformSafeCall(
    execute: suspend () -> HttpResponse,
    handleResponse: suspend (HttpResponse) -> Result<T, DataError.Remote>
): Result<T, DataError.Remote> {
    return try {
        val response = execute()
        handleResponse(response)
    } catch (e: DarwinHttpRequestException) {
        handleDarwinException(e)
    } catch (e: UnresolvedAddressException) {
        Result.Failure(DataError.Remote.NO_INTERNET)
    } catch (e: HttpRequestTimeoutException) {
        Result.Failure(DataError.Remote.REQUEST_TIMEOUT)
    } catch (e: SerializationException) {
        Result.Failure(DataError.Remote.SERIALIZATION)
    } catch (e: Exception) {
        // Preserve structured cancellation: if the coroutine was cancelled, this will rethrow the cancellation
        coroutineContext.ensureActive()
        Result.Failure(DataError.Remote.UNKNOWN)
    }
}

/**
 * Handles Darwin-related HTTP request exceptions by mapping them to a corresponding remote data error.
 *
 * @param e The `DarwinHttpRequestException` containing the original NSError information.
 * @return A `Result` instance encapsulating a failure of type `DataError.Remote`, with specific errors
 * from the `NO_INTERNET` or `REQUEST_TIMEOUT` categories, or an `UNKNOWN` error if no specific mapping is found.
 */
private fun handleDarwinException(e: DarwinHttpRequestException): Result<Nothing, DataError.Remote> {
    val nsError = e.origin

    // Map only NSURLErrorDomain codes; anything else falls back to UNKNOWN
    return if (nsError.domain == NSURLErrorDomain) {
        when (nsError.code) {
            NSURLErrorNotConnectedToInternet,
            NSURLErrorNetworkConnectionLost,
            NSURLErrorCannotFindHost,
            NSURLErrorDNSLookupFailed,
            NSURLErrorResourceUnavailable,
            NSURLErrorInternationalRoamingOff,
            NSURLErrorCallIsActive,
            NSURLErrorDataNotAllowed -> {
                Result.Failure(DataError.Remote.NO_INTERNET)
            }

            NSURLErrorTimedOut -> Result.Failure(DataError.Remote.REQUEST_TIMEOUT)
            else -> Result.Failure(DataError.Remote.UNKNOWN)
        }
    } else Result.Failure(DataError.Remote.UNKNOWN)
}