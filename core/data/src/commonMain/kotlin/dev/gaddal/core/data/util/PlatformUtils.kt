package dev.gaddal.core.data.util

/**
 * Utility object to provide platform-specific utilities and information.
 *
 * This object is expected to be implemented separately for each platform (e.g., iOS, Android, etc.)
 * and provides methods to retrieve platform-specific details or behaviors.
 */
expect object PlatformUtils {
    /**
     * Retrieves the name of the operating system.
     *
     * @return The name of the operating system as a string.
     */
    fun getOSName(): String
}