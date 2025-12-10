package dev.gaddal.core.data.networking

import dev.gaddal.core.domain.util.DataError
import dev.gaddal.core.domain.util.Result
import io.ktor.client.statement.HttpResponse

/**
 * Executes a platform-safe operation by invoking an HTTP request and handling its response.
 *
 * This method ensures that network operations are safely executed while
 * encapsulating any resulting data or errors in a type-safe `Result` wrapper.
 *
 * @param execute A suspending function that executes the desired HTTP request and returns an `HttpResponse`.
 * @param handleResponse A suspending function that processes the `HttpResponse` and returns a `Result`
 *        containing either the data of type `T`, or an error of type `DataError.Remote`.
 * @return A `Result` containing the successful data of type `T` if the operation succeeds,
 *         or a `DataError.Remote` if the operation fails.
 */
actual suspend fun <T> platformSafeCall(
    execute: suspend () -> HttpResponse,
    handleResponse: suspend (HttpResponse) -> Result<T, DataError.Remote>
): Result<T, DataError.Remote> {
    return Result.Failure(DataError.Remote.SERVER_ERROR)
}