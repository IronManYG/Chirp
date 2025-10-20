package dev.gaddal.core.data.networking

import dev.gaddal.core.domain.util.DataError
import dev.gaddal.core.domain.util.Result
import io.ktor.client.HttpClient
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.request.url
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
 * Executes an HTTP POST request with the specified route, query parameters, and request body.
 *
 * @param Request The type of the request body to be sent.
 * @param Response The type of the response expected from the server.
 * Must inherit from `Any` and is determined at runtime using reified type parameters.
 * @param route The endpoint or route where the HTTP POST request should be sent.
 * @param queryParams A map of query parameters that will be appended to the URL. Defaults to an empty map.
 * @param body The payload of type `Request` to be included in the POST request body.
 * @param builder A lambda function that allows additional configuration of the `HttpRequestBuilder`.
 * @return A `Result` wrapper which contains the response of type `Response` on success
 * or a `DataError.Remote` on failure.
 */
suspend inline fun <reified Request, reified Response : Any> HttpClient.post(
    route: String,
    queryParams: Map<String, Any> = mapOf(),
    body: Request,
    crossinline builder: HttpRequestBuilder.() -> Unit = {}
): Result<Response, DataError.Remote> {
    return safeCall {
        post {
            url(constructRoute(route))
            queryParams.forEach { (key, value) ->
                parameter(key, value)
            }
            setBody(body)
            builder()
        }
    }
}

/**
 * Makes a GET request to the specified route using the [HttpClient].
 *
 * @param route The API route or endpoint to make the GET request.
 * @param queryParams A map of query parameters to append to the request URL. Defaults to an empty map.
 * @param builder A lambda to customize the [HttpRequestBuilder] with additional configurations. Defaults to an empty lambda.
 * @return A [Result] containing the response of type [Response] when successful, or a [DataError.Remote] in case of an error.
 */
suspend inline fun <reified Response : Any> HttpClient.get(
    route: String,
    queryParams: Map<String, Any> = mapOf(),
    crossinline builder: HttpRequestBuilder.() -> Unit = {}
): Result<Response, DataError.Remote> {
    return safeCall {
        get {
            url(constructRoute(route))
            queryParams.forEach { (key, value) ->
                parameter(key, value)
            }
            builder()
        }
    }
}

/**
 * Makes a HTTP DELETE request to the specified route, optionally including query parameters
 * and allowing for additional customization of the request through the builder.
 *
 * @param Response The expected response type, which must conform to the `Any` type constraint.
 * @param route The relative or absolute URL representing the endpoint where the DELETE request will be sent.
 * @param queryParams A map of query parameters that will be appended to the URL. Defaults to an empty map.
 * @param builder A lambda to customize the request further using the `HttpRequestBuilder` DSL.
 * @return A `Result` that contains the response of type `Response` in case of success, or a `DataError.Remote` in case of failure.
 */
suspend inline fun <reified Response : Any> HttpClient.delete(
    route: String,
    queryParams: Map<String, Any> = mapOf(),
    crossinline builder: HttpRequestBuilder.() -> Unit = {}
): Result<Response, DataError.Remote> {
    return safeCall {
        delete {
            url(constructRoute(route))
            queryParams.forEach { (key, value) ->
                parameter(key, value)
            }
            builder()
        }
    }
}

/**
 * Executes an HTTP PUT request to the specified route with the provided request body and query parameters.
 * The response is processed and encapsulated in a `Result` object.
 *
 * @param Request The type of the request body to be sent with the PUT request.
 * @param Response The type of the expected response body, which must be non-null and parsed into the specified type.
 * @param route The endpoint route where the PUT request will be directed.
 * @param queryParams A map of key-value pairs to be added as query parameters to the URL. Defaults to an empty map if no query parameters are provided
 * .
 * @param body The request body object to be sent with the PUT request.
 * @param builder A lambda function for additional configurations to the `HttpRequestBuilder`. This is optional and defaults to an empty lambda block
 * .
 *
 * @return A `Result` object containing either the successfully parsed response of type `Response` or a `DataError.Remote` in case of an error.
 */
suspend inline fun <reified Request, reified Response : Any> HttpClient.put(
    route: String,
    queryParams: Map<String, Any> = mapOf(),
    body: Request,
    crossinline builder: HttpRequestBuilder.() -> Unit = {}
): Result<Response, DataError.Remote> {
    return safeCall {
        put {
            url(constructRoute(route))
            queryParams.forEach { (key, value) ->
                parameter(key, value)
            }
            setBody(body)
            builder()
        }
    }
}

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