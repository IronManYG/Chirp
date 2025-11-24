package dev.gaddal.chat.presentation.chat_detail.components

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.flow.distinctUntilChanged

/**
 * A composable function that listens to the scroll state of a lazy list and triggers actions
 * based on pagination-related conditions.
 *
 * This function utilizes a [snapshotFlow] to observe scroll position changes. It employs
 * [rememberUpdatedState] for [itemCount], [isPaginationLoading], and [isEndReached] to ensure
 * these values are accessible within the flow without causing it to be recreated on every change.
 *
 * The [onNearTop] callback is triggered when the user scrolls within a threshold (5 items)
 * from the top of the list, provided that:
 * 1. Pagination is not currently loading.
 * 2. The end of the list has not been reached.
 * 3. The [itemCount] has increased since the last trigger (preventing duplicate triggers for the same dataset).
 *
 * @param lazyListState The state of the lazy list being observed.
 * @param itemCount The current number of items in the list. This is used to avoid race conditions
 *                  where pagination might complete (setting [isPaginationLoading] to false) but the
 *                  UI hasn't yet reflected the new items. By checking if the item count has actually
 *                  increased, we prevent redundant load triggers when scrolling rapidly to the top.
 * @param isPaginationLoading A flag that indicates if pagination is currently loading.
 * @param isEndReached A flag that indicates if the end of the list has been reached.
 * @param onNearTop A callback function triggered when scrolled near the top of the list
 *                  and pagination conditions are met.
 */
@Composable
fun PaginationScrollListener(
    lazyListState: LazyListState,
    itemCount: Int,
    isPaginationLoading: Boolean,
    isEndReached: Boolean,
    onNearTop: () -> Unit
) {
    val updatedItemCount by rememberUpdatedState(itemCount)
    val isPaginationLoading by rememberUpdatedState(isPaginationLoading)
    val isEndReached by rememberUpdatedState(isEndReached)

    var lastTriggerItemCount by remember {
        mutableIntStateOf(0)
    }

    LaunchedEffect(lazyListState) {
        snapshotFlow {
            val info = lazyListState.layoutInfo
            val total = info.totalItemsCount
            val topVisibleIndex = info.visibleItemsInfo.lastOrNull()?.index
            val remainingItems = if (topVisibleIndex != null) {
                total - topVisibleIndex - 1
            } else null

            PaginationScrollState(
                currentItemCount = updatedItemCount,
                isEligible = remainingItems != null &&
                        remainingItems <= 5 &&
                        !isPaginationLoading &&
                        !isEndReached
            )
        }
            .distinctUntilChanged()
            .collect { (itemCount, isEligible) ->
                val shouldTrigger = isEligible && itemCount > lastTriggerItemCount

                if (shouldTrigger) {
                    lastTriggerItemCount = itemCount
                    onNearTop()
                }
            }
    }
}

/**
 * Represents the snapshot of state required to determine pagination eligibility.
 *
 * This wrapper class is used within the [snapshotFlow] to bundle the current item count
 * and eligibility status. This allows [distinctUntilChanged] to filter out redundant emissions,
 * ensuring the collector logic only runs when meaningful changes occur to the scroll state or pagination availability.
 *
 * @property currentItemCount The current number of items displayed in the list, used to verify if the list has grown.
 * @property isEligible A flag indicating if pagination should potentially trigger based on scroll position (e.g., remaining items <= 5),
 *                      loading state, and whether the end is reached.
 */
data class PaginationScrollState(
    val currentItemCount: Int,
    val isEligible: Boolean
)