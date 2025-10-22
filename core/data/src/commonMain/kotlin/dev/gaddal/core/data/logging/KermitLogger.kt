package dev.gaddal.core.data.logging

import co.touchlab.kermit.Logger
import dev.gaddal.core.domain.logging.ChirpLogger

/**
 * An implementation of the `ChirpLogger` interface using the `Logger` object from Kermit.
 * This logger provides methods for logging messages at various levels of severity.
 */
object KermitLogger : ChirpLogger {

    /**
     * Logs a debug message.
     *
     * @param message The message to be logged at the debug level.
     */
    override fun debug(message: String) {
        Logger.d(message)
    }

    /**
     * Logs an informational message.
     *
     * @param message The message to be logged.
     */
    override fun info(message: String) {
        Logger.i(message)
    }

    /**
     * Logs a warning message using the Logger.
     *
     * @param message The warning message to be logged.
     */
    override fun warn(message: String) {
        Logger.w(message)
    }

    /**
     * Logs an error message along with an optional throwable.
     *
     * @param message The error message to be logged.
     * @param throwable The throwable object containing the error details, or null if no throwable is provided.
     */
    override fun error(message: String, throwable: Throwable?) {
        Logger.e(message, throwable)
    }
}