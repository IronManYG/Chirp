package dev.gaddal.core.data.networking

import dev.gaddal.core.data.BuildKonfig
import dev.gaddal.core.domain.logging.ChirpLogger
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

/**
 * A factory class responsible for creating and configuring instances of `HttpClient`.
 * This factory utilizes various features and configurations such as JSON content negotiation,
 * timeout settings, logging, WebSocket support, and default request configurations.
 *
 * @property chirpLogger An instance of `ChirpLogger` used to log HTTP client activity.
 */
class HttpClientFactory(
    private val chirpLogger: ChirpLogger
) {

    /**
     * Creates an instance of [HttpClient] configured with specified settings such as content negotiation,
     * timeouts, logging, and default request headers.
     *
     * @param engine The [HttpClientEngine] used to perform network requests.
     * @return A configured instance of [HttpClient].
     */
    fun create(engine: HttpClientEngine): HttpClient {
        return HttpClient(engine) {
            install(ContentNegotiation) {
                json(
                    json = Json {
                        ignoreUnknownKeys = true
                    }
                )
            }
            install(HttpTimeout) {
                socketTimeoutMillis = 20_000L
                requestTimeoutMillis = 20_000L
            }
            install(Logging) {
                logger = object : Logger {
                    /**
                     * Logs a debug message using the `chirpLogger` instance.
                     *
                     * @param message The message to be logged.
                     */
                    override fun log(message: String) {
                        chirpLogger.debug(message)
                    }
                }
                level = LogLevel.ALL
            }
            install(WebSockets) {
                pingIntervalMillis = 20_000L
            }
            defaultRequest {
                header("x-api-key", BuildKonfig.API_KEY)
                contentType(ContentType.Application.Json)
            }
        }
    }
}