package dev.gaddal.chat.presentation.chat_detail.components

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import dev.gaddal.chat.presentation.model.MessageUi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged

/**
 * Observes the state of a lazy list and provides notifications for showing or hiding
 * a message banner, based on the scrolling position and user interaction.
 *
 * @param lazyListState The state of the lazy list, providing information on visible items
 * and scroll progress.
 * @param messages A list of messages to display within the lazy list, represented by [MessageUi].
 * @param isBannerVisible A flag indicating whether the message banner is currently visible.
 * @param onShowBanner A callback triggered to show the message banner. It provides the index
 * of the topmost visible item where the banner should appear.
 * @param onHide A callback triggered to hide the message banner.
 */
@Composable
fun MessageBannerListener(
    lazyListState: LazyListState,
    messages: List<MessageUi>,
    isBannerVisible: Boolean,
    onShowBanner: (topVisibleItemIndex: Int) -> Unit,
    onHide: () -> Unit,
) {
    val isBannerVisibleUpdated by rememberUpdatedState(isBannerVisible)

    LaunchedEffect(lazyListState, messages) {
        snapshotFlow {
            val info = lazyListState.layoutInfo
            val visibleItems = info.visibleItemsInfo
            val total = info.totalItemsCount

            val oldestVisibleMessageIndex = visibleItems.maxOfOrNull { it.index } ?: -1

            val isAtOldestMessages = oldestVisibleMessageIndex >= total - 1
            val isAtNewestMessages = visibleItems.any { it.index == 0 }
            MessageBannerScrollState(
                oldestVisibleMessageIndex = oldestVisibleMessageIndex,
                isScrollInProgress = lazyListState.isScrollInProgress,
                isAtEdgeOfList = isAtOldestMessages || isAtNewestMessages
            )
        }
            .distinctUntilChanged()
            .collect { (oldestVisibleIndex, isScrollInProgress, isAtEdgeOfList) ->
                val shouldShowBanner = isScrollInProgress &&
                        !isAtEdgeOfList &&
                        oldestVisibleIndex >= 0

                when {
                    shouldShowBanner -> onShowBanner(oldestVisibleIndex)
                    !shouldShowBanner && isBannerVisibleUpdated -> {
                        delay(1000L)
                        onHide()
                    }
                }
            }
    }
}

/**
 * Represents the state of the message banner's scrolling in a chat view.
 *
 * @property oldestVisibleMessageIndex The index of the oldest message currently visible in the list.
 * @property isScrollInProgress Indicates whether the user is actively scrolling the list.
 * @property isAtEdgeOfList States if the scroll position is at the edge of the list (e.g., top or bottom).
 */
data class MessageBannerScrollState(
    val oldestVisibleMessageIndex: Int,
    val isScrollInProgress: Boolean,
    val isAtEdgeOfList: Boolean
)