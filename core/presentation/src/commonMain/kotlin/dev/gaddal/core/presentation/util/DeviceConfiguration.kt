package dev.gaddal.core.presentation.util

import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowSizeClass.Companion.HEIGHT_DP_EXPANDED_LOWER_BOUND
import androidx.window.core.layout.WindowSizeClass.Companion.HEIGHT_DP_MEDIUM_LOWER_BOUND
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_EXPANDED_LOWER_BOUND
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_MEDIUM_LOWER_BOUND

/**
 * Determines the current device configuration based on the window size class of the device.
 *
 * This function utilizes the adaptive window size class information to compute the
 * appropriate [DeviceConfiguration], which represents the type of device layout such as
 * mobile portrait, mobile landscape, tablet, or desktop.
 *
 * @return A [DeviceConfiguration] instance representing the current device's configuration.
 */
@Composable
fun currentDeviceConfiguration(): DeviceConfiguration {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    return DeviceConfiguration.fromWindowSizeClass(windowSizeClass)
}

/**
 * Represents device configurations based on screen orientation and size.
 *
 * This enum class is used to define different types of device setups like mobile and tablet,
 * in both portrait and landscape orientations, as well as desktop configurations.
 */
enum class DeviceConfiguration {
    /**
     * Represents the portrait orientation of a mobile device configuration.
     *
     * This configuration is determined when the device's width is below the medium width threshold
     * and its height meets or exceeds the medium height threshold.
     */
    MOBILE_PORTRAIT,
    /**
     * Represents a mobile device configuration in landscape orientation.
     *
     * This configuration is determined based on the device's window size class,
     * where the width exceeds the expanded size threshold, and the height
     * is less than the medium size threshold.
     */
    MOBILE_LANDSCAPE,
    /**
     * Represents a device configuration where the application is displayed in portrait mode on a tablet.
     *
     * This configuration is typically used when the screen dimensions are within the medium
     * and expanded bounds for width, and the height dimension meets or exceeds the expanded threshold.
     *
     * Commonly used for determining layout adjustments and UI behavior specific to tablet portrait mode.
     */
    TABLET_PORTRAIT,
    /**
     * Represents a device configuration where the device is a tablet
     * oriented in landscape mode.
     *
     * This configuration is determined by specific minimum width
     * and height constraints as defined in the `fromWindowSizeClass`
     * method of the `DeviceConfiguration` companion object.
     */
    TABLET_LANDSCAPE,
    /**
     * Represents the desktop configuration for devices.
     *
     * This configuration is typically used when the device's screen size does
     * not align with the defined bounds for mobile or tablet configurations. It is often
     * associated with devices that have significantly larger screen dimensions or unique layouts.
     */
    DESKTOP;

    /**
     * Provides a companion object for the `DeviceConfiguration` enum class.
     * This object includes utility functions related to device configuration determination.
     */
    companion object {
        /**
         * Determines the appropriate [DeviceConfiguration] based on the given [WindowSizeClass].
         *
         * @param windowSizeClass The [WindowSizeClass] object containing the minimum width and height
         * parameters used to classify devices into configurations.
         * @return A [DeviceConfiguration] corresponding to the provided [WindowSizeClass],
         * such as MOBILE_PORTRAIT, MOBILE_LANDSCAPE, TABLET_PORTRAIT, TABLET_LANDSCAPE, or DESKTOP.
         */
        fun fromWindowSizeClass(windowSizeClass: WindowSizeClass): DeviceConfiguration {
            return with(windowSizeClass) {
                when {
                    minWidthDp < WIDTH_DP_MEDIUM_LOWER_BOUND &&
                            minHeightDp >= HEIGHT_DP_MEDIUM_LOWER_BOUND -> MOBILE_PORTRAIT

                    minWidthDp >= WIDTH_DP_EXPANDED_LOWER_BOUND &&
                            minHeightDp < HEIGHT_DP_MEDIUM_LOWER_BOUND -> MOBILE_LANDSCAPE

                    minWidthDp in WIDTH_DP_MEDIUM_LOWER_BOUND..WIDTH_DP_EXPANDED_LOWER_BOUND &&
                            minHeightDp >= HEIGHT_DP_EXPANDED_LOWER_BOUND -> TABLET_PORTRAIT

                    minWidthDp >= WIDTH_DP_EXPANDED_LOWER_BOUND &&
                            minHeightDp in HEIGHT_DP_MEDIUM_LOWER_BOUND..HEIGHT_DP_EXPANDED_LOWER_BOUND -> TABLET_LANDSCAPE

                    else -> DESKTOP
                }
            }
        }
    }
}