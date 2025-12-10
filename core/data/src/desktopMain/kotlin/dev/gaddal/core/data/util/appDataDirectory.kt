package dev.gaddal.core.data.util

import java.io.File

/**
 * Represents the directory used for storing application-specific data files.
 *
 * The directory path varies depending on the operating system:
 * - On Windows, it corresponds to the `APPDATA` environment variable followed by `Chirp`.
 * - On macOS, it is located in the `Library/Application Support/Chirp` directory under the user's home directory.
 * - On Linux, it resides in the `.local/share/Chirp` directory under the user's home directory.
 *
 * This directory is commonly used for storing persistent data such as user preferences, databases,
 * and other application-related files.
 *
 * @return A `File` object pointing to the application's data directory. If the directory does not exist,
 * it is the caller's responsibility to create it before using it.
 */
val appDataDirectory: File
    get() {
        val userHome = System.getProperty("user.home")
        return when (currentOs) {
            DesktopOs.WINDOWS -> File(System.getenv("APPDATA"), "Chirp")
            DesktopOs.MACOS -> File(userHome, "Library/Application Support/Chirp")
            DesktopOs.LINUX -> File(userHome, ".local/share/Chirp")
        }
    }