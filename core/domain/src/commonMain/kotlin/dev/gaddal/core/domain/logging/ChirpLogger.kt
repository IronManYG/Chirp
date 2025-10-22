
package dev.gaddal.core.domain.logging

/**
 * A logger interface for recording log messages at various levels of severity.
 * This interface is designed to facilitate logging throughout the application, providing methods
 * to log debug, informational, warning, and error messages.
 */
interface ChirpLogger {
    /**
     * Logs a debug message.
     *
     * @param message The debug message to be logged.
     */
    fun debug(message: String)
    /**
     * Logs an informational message.
     *
     * @param message The informational message to be logged.
     */
    fun info(message: String)
    /**
     * Logs a warning message.
     *
     * @param message The warning message to be logged.
     */
    fun warn(message: String)
    /**
     * Logs an error message with an optional throwable.
     *
     * @param message The error message to log.
     * @param throwable An optional throwable to include with the error log, or null if no throwable is provided.
     */
    fun error(message: String, throwable: Throwable? = null)
}