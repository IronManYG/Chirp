package dev.gaddal.core.data.util

/**
 * Represents the operating systems supported by the application.
 *
 * This enum is used to identify the desktop operating system on which the application is running.
 * The supported operating systems include:
 * - WINDOWS: Represents the Windows operating system.
 * - MACOS: Represents the macOS operating system.
 * - LINUX: Represents Linux-based operating systems.
 */
enum class DesktopOs {
    WINDOWS,
    MACOS,
    LINUX
}

/**
 * Represents the current desktop operating system on which the application is running.
 *
 * This property determines the operating system by analyzing the `os.name` system property,
 * and maps it to the corresponding value in the `DesktopOs` enum.
 *
 * The mapping logic is as follows:
 * - If the operating system name contains "win", it is identified as `DesktopOs.WINDOWS`.
 * - If the operating system name contains "mac", it is identified as `DesktopOs.MACOS`.
 * - Any other operating system is identified as `DesktopOs.LINUX`.
 *
 * This property is useful for performing OS-specific operations or handling OS-related differences
 * within the application.
 */
val currentOs: DesktopOs
    get() {
        val osName = System.getProperty("os.name").lowercase()
        return when {
            osName.contains("win") -> DesktopOs.WINDOWS
            osName.contains("mac") -> DesktopOs.MACOS
            else -> DesktopOs.LINUX
        }
    }