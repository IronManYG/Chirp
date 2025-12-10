package dev.gaddal.core.data.util

/**
 * Utility object for platform-specific operations.
 *
 * Provides methods to retrieve platform-related information.
 */
actual object PlatformUtils {
    /**
     * Retrieves the name of the operating system.
     *
     * @return The name of the operating system as a string.
     */
    actual fun getOSName(): String {
        return System.getProperty("os.name")
    }
}