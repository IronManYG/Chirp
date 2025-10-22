package dev.gaddal.core.data.networking

import dev.gaddal.core.domain.util.DataError
import dev.gaddal.core.domain.util.Result
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.statement.HttpResponse
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.ensureActive
import kotlinx.serialization.SerializationException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlin.coroutines.coroutineContext

/**
 * A platform-specific safe call method for making network requests, handling exceptions, and mapping responses.
 *
 * This function encapsulates the execution of a network request and response handling logic,
 * while managing various exceptions such as network connectivity issues, timeouts, and serialization errors.
 * In the event of an error, a corresponding failure `Result` is returned with the appropriate `DataError.Remote`.
 *
 * @param execute A suspending lambda function that performs the network request and returns an `HttpResponse`.
 * @param handleResponse A suspending lambda function that processes the `HttpResponse`
 * and returns a `Result` wrapping the successful outcome or a `DataError.Remote` type in case of failure.
 * @return A `Result` containing the successful data of type `T`,
 * or a failure with a `DataError.Remote` explaining the encountered issue.
 */
actual suspend fun <T> platformSafeCall(
    execute: suspend () -> HttpResponse,
    handleResponse: suspend (HttpResponse) -> Result<T, DataError.Remote>
): Result<T, DataError.Remote> {
    return try {
        val response = execute()
        handleResponse(response)
    } catch(e: UnknownHostException) {
        Result.Failure(DataError.Remote.NO_INTERNET)
    } catch(e: UnresolvedAddressException) {
        Result.Failure(DataError.Remote.NO_INTERNET)
    } catch(e: ConnectException) {
        Result.Failure(DataError.Remote.NO_INTERNET)
    } catch(e: SocketTimeoutException) {
        Result.Failure(DataError.Remote.REQUEST_TIMEOUT)
    } catch(e: HttpRequestTimeoutException) {
        Result.Failure(DataError.Remote.REQUEST_TIMEOUT)
    } catch(e: SerializationException) {
        Result.Failure(DataError.Remote.SERIALIZATION)
    } catch (e: Exception) {
        // Preserve structured cancellation: if the coroutine was cancelled, this will rethrow the cancellation
        coroutineContext.ensureActive()
        Result.Failure(DataError.Remote.UNKNOWN)
    }
}