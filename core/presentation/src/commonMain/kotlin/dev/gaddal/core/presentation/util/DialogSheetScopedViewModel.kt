@file:OptIn(ExperimentalUuidApi::class)

package dev.gaddal.core.presentation.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import org.koin.compose.viewmodel.koinViewModel
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * A composable that provides a lifecycle-scoped `ViewModelStoreOwner` for its content,
 * typically used for dialogs or bottom sheets.
 *
 * This utility allows a ViewModel to be scoped to the visibility of a composable, such as a dialog.
 * The ViewModel is created when the content becomes visible and is destroyed when it's hidden.
 * This is useful for complex dialogs that require their own ViewModel but should not retain state
 * when dismissed.
 *
 * It works by creating a new `ViewModelStoreOwner` for the dialog's content. This owner is managed by a
 * `ScopedStoreRegistryViewModel`, which is scoped to the parent screen or navigation graph. This setup ensures
 * that the dialog's ViewModel can survive configuration changes (on Android) as long as the dialog is visible,
 * but is cleared when the dialog is dismissed.
 *
 * EXPERIMENTAL: This approach is tailored for this project's specific needs. While it has been
 * tested for the primary use cases, there may be unhandled edge cases where a ViewModel could be
 * cleared unexpectedly. It is not a production-hardened library solution and should be used with
 * this consideration in mind.
 *
 * @param visible Controls the visibility of the content. When `true`, a scoped `ViewModelStoreOwner` is created
 * and the `content` is displayed. When `false`, the owner and its associated ViewModel are cleared.
 * @param scopeId A unique identifier for the scoped `ViewModelStore`. It defaults to a randomly generated UUID
 * that is saved across recompositions and configuration changes.
 * @param content The composable content to be displayed within the new scope. Any `koinViewModel()` call inside
 * this lambda will be scoped to the lifecycle of this dialog.
 */
@Composable
fun DialogSheetScopedViewModel(
    visible: Boolean,
    scopeId: String = rememberSaveable { Uuid.random().toString() },
    content: @Composable () -> Unit
) {
    // Retrieve the nearest ViewModelStoreOwner from the composition hierarchy (e.g., the screen, nav graph, or activity).
    // This parent owner will own the ScopedStoreRegistryViewModel.
    val parentOwner = LocalViewModelStoreOwner.current
        ?: throw IllegalStateException("No parent owner found. This composable must be used within a valid ViewModelStoreOwner scope, like a NavHost or an Activity.")

    // Get an instance of the registry ViewModel, scoped to the parent. This registry will survive
    // as long as the parent does and will manage all dialog ViewModels within it.
    val registry = koinViewModel<ScopedStoreRegistryViewModel>(
        viewModelStoreOwner = parentOwner
    )

    // State to hold the dynamically created ViewModelStoreOwner for the dialog's scope.
    var owner by remember { mutableStateOf<ViewModelStoreOwner?>(null) }

    // This effect responds to changes in visibility or scopeId.
    LaunchedEffect(visible, scopeId) {
        if (visible && owner == null) {
            // When the dialog becomes visible and an owner doesn't exist, create one.
            owner = object : ViewModelStoreOwner {
                /**
                 * Provides the [ViewModelStore] for this specific dialog scope.
                 *
                 * This property retrieves a [ViewModelStore] from the [ScopedStoreRegistryViewModel]
                 * using the unique `scopeId`. If a store for this ID doesn't exist (e.g., first time shown),
                 * a new one is created. If it does exist (e.g., after a configuration change), the
                 * existing instance is returned, preserving the ViewModel.
                 */
                override val viewModelStore: ViewModelStore
                    get() = registry.getOrCreate(scopeId)
            }
        } else if (!visible && owner != null) {
            // When the dialog is hidden and an owner exists, clear the ViewModelStore from the registry.
            // This will destroy the associated ViewModel.
            registry.clear(scopeId)
            owner = null
        }
    }

    // When the `owner` is not null (i.e., when `visible` is true), provide this new owner
    // to the composable content via CompositionLocalProvider.
    owner?.let { dialogOwner ->
        CompositionLocalProvider(LocalViewModelStoreOwner provides dialogOwner) {
            // Any composable inside this block, especially `koinViewModel()`, will now use
            // `dialogOwner` to scope the ViewModel, effectively tying the ViewModel's lifecycle
            // to the dialog's visibility.
            content()
        }
    }
}
