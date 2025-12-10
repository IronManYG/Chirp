package dev.gaddal.chat.data.network

import dev.gaddal.core.domain.logging.ChirpLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.net.InetSocketAddress
import java.net.NetworkInterface
import java.net.Socket
import kotlin.coroutines.coroutineContext

/**
 * Observes and reports the network connectivity status by continuously evaluating
 * the connection state and emitting updates to the `isConnected` flow.
 *
 * The `ConnectivityObserver` class performs periodic checks to determine whether the
 * system is online by attempting to connect to a set of predefined network endpoints.
 * The results of these checks are logged and emitted to observers using a Kotlin Flow.
 *
 * @constructor Creates a `ConnectivityObserver` instance.
 * @param chirpLogger A logger used to log connectivity state changes.
 */
actual class ConnectivityObserver(
    private val chirpLogger: ChirpLogger
) {
    /**
     * A Flow that emits the current network connectivity status as a Boolean in regular intervals.
     *
     * This property continuously monitors the network connectivity state and emits:
     * - `true` if the network connection is available.
     * - `false` if the network connection is unavailable.
     *
     * Uses a polling mechanism to check the connectivity status every 5000 milliseconds (5 seconds)
     * and logs the current connectivity state using the provided logger.
     */
    actual val isConnected = flow {
        while (true) {
            val connected = isConnected()
            chirpLogger.info("Connectivity state on Desktop: $connected")
            emit(connected)
            delay(5000L)
        }
    }

    /**
     * A predefined list of target `InetSocketAddress` instances representing commonly used
     * DNS servers to test network connectivity. Each target in the list includes an IP address
     * and port number that can be used for validating network reachability.
     *
     * The list includes:
     * 1. Google's public DNS at "8.8.8.8:53".
     * 2. Cloudflare's public DNS at "1.1.1.1:53".
     * 3. OpenDNS's public DNS at "208.67.222.222:53".
     *
     * These targets are used in connectivity checks, where an attempt is made to establish
     * a socket connection to determine the availability of a network connection.
     */
    private val connectivityTargets = listOf(
        InetSocketAddress("8.8.8.8", 53),
        InetSocketAddress("1.1.1.1", 53),
        InetSocketAddress("208.67.222.222", 53),
    )

    /**
     * Checks if the system currently has an active and working network connection.
     *
     * This method verifies the presence of active network interfaces and attempts to
     * establish a connection with predefined connectivity targets using sockets.
     *
     * @return True if a valid network connection is available, false otherwise.
     */
    private suspend fun isConnected(): Boolean {
        val hasInterface = try {
            NetworkInterface.getNetworkInterfaces()
                .asSequence()
                .any { networkInterface ->
                    !networkInterface.isLoopback &&
                            networkInterface.isUp &&
                            networkInterface.inetAddresses.hasMoreElements()
                }
        } catch (_: Exception) {
            coroutineContext.ensureActive()
            false
        }

        if (!hasInterface) {
            return false
        }

        return withContext(Dispatchers.IO) {
            connectivityTargets.any { target ->
                try {
                    Socket().use {
                        it.soTimeout = 3000
                        it.connect(target)
                        true
                    }
                } catch (_: Exception) {
                    kotlin.coroutines.coroutineContext.ensureActive()
                    false
                }
            }
        }
    }
}