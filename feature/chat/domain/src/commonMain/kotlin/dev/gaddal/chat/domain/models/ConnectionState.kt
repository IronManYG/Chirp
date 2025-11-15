package dev.gaddal.chat.domain.models

/**
 * Represents the various states of a network or connection status, which can be used
 * to monitor and manage the connectivity within the application.
 *
 * - DISCONNECTED: Indicates that there is no active connection.
 * - CONNECTING: Indicates that the system is in the process of attempting to establish a connection.
 * - CONNECTED: Indicates that the connection has been successfully established.
 * - ERROR_NETWORK: Indicates that a network-related error has occurred.
 * - ERROR_UNKNOWN: Indicates that an unknown error has occurred during the connection process.
 */
enum class ConnectionState {
    DISCONNECTED,
    CONNECTING,
    CONNECTED,
    ERROR_NETWORK,
    ERROR_UNKNOWN
}