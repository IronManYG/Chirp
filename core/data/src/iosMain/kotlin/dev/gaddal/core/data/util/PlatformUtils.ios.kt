package dev.gaddal.core.data.util

/**
 * Utility object for interacting with platform-specific features or information.
 *
 * This object is intended to provide platform-specific implementations for functionalities,
 * enabling the application to access or interact with platform-dependent properties and behaviors.
 */
actual object PlatformUtils {
    /**
     * Retrieves the name of the operating system.
     *
     * This method provides the name of the operating system on which the application is running.
     *
     * @return The name of the operating system as a string (e.g., "IOS").
     */
    actual fun getOSName() = "IOS"
}