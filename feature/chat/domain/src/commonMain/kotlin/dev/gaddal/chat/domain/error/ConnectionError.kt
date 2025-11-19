package dev.gaddal.chat.domain.error

import dev.gaddal.core.domain.util.Error

/**
 * Represents errors related to network connections.
 *
 * This enum defines specific types of connection-related errors that can occur during
 * network operations. It is primarily used to represent failure scenarios when handling
 * network (e.g., WebSocket) communication in the application domain.
 *
 * Enum values:
 * - `NOT_CONNECTED`: Indicates that the device is not connected to a network or the connection
 *                     is not established when an operation is attempted.
 * - `MESSAGE_SEND_FAILED`: Indicates a failure to send a message over the network, typically
 *                          due to connectivity issues or other operational failures.
 */
enum class ConnectionError : Error {
    NOT_CONNECTED,
    MESSAGE_SEND_FAILED
}