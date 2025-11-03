package dev.gaddal.chat.presentation.chat_list_detail

import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.layout.PaneScaffoldDirective
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.gaddal.core.presentation.util.DeviceConfiguration
import dev.gaddal.core.presentation.util.currentDeviceConfiguration

/**
 * Creates a `PaneScaffoldDirective` instance with no spacing between partitions.
 *
 * This method calculates the maximum horizontal and vertical partitions
 * and their corresponding spacer sizes based on the current device configuration
 * and window adaptive information. It uses predefined values for specific
 * device configurations such as mobile, tablet, and desktop, as well as
 * adjustments for window postures like tabletop mode.
 *
 * @return A `PaneScaffoldDirective` instance containing the parameters necessary
 * for defining the layout behavior of a pane scaffold. The directive includes the
 * maximum number of horizontal and vertical partitions, spacer sizes, default pane
 * preferred width, and any excluded bounds.
 */
@Composable
fun createNoSpacingPaneScaffoldDirective(): PaneScaffoldDirective {
    val configuration = currentDeviceConfiguration()
    val windowAdaptiveInfo = currentWindowAdaptiveInfo()

    val maxHorizontalPartitions = when (configuration) {
        DeviceConfiguration.MOBILE_PORTRAIT,
        DeviceConfiguration.MOBILE_LANDSCAPE,
        DeviceConfiguration.TABLET_PORTRAIT -> 1

        DeviceConfiguration.TABLET_LANDSCAPE,
        DeviceConfiguration.DESKTOP -> 2
    }

    val verticalPartitionSpacerSize: Dp
    val maxVerticalPartitions: Int

    if (windowAdaptiveInfo.windowPosture.isTabletop) {
        maxVerticalPartitions = 2
        verticalPartitionSpacerSize = 24.dp
    } else {
        maxVerticalPartitions = 1
        verticalPartitionSpacerSize = 0.dp
    }

    return PaneScaffoldDirective(
        maxHorizontalPartitions = maxHorizontalPartitions,
        horizontalPartitionSpacerSize = 0.dp,
        maxVerticalPartitions = maxVerticalPartitions,
        verticalPartitionSpacerSize = verticalPartitionSpacerSize,
        defaultPanePreferredWidth = 360.dp,
        excludedBounds = emptyList()
    )
}