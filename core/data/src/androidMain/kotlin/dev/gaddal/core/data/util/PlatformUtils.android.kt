package dev.gaddal.core.data.util

/**
 * Utility object for platform-specific functionalities.
 *
 * This object provides tools to access platform-related information,
 * such as retrieving the operating system name. It uses platform-specific
 * implementations through the `actual` keyword.
 */
actual object PlatformUtils {
    /**
     * Retrieves the name of the operating system.
     *
     * This method provides the name of the operating system for the current platform
     * and may be utilized to perform platform-specific logic or actions.
     *
     * @return The name of the operating system as a string. For example, "ANDROID" for Android devices.
     */
    actual fun getOSName() = "ANDROID"
}