package dev.gaddal.core.presentation.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStore

/**
 * A ViewModel responsible for creating and managing multiple `ViewModelStore` instances,
 * each identified by a unique ID.
 *
 * This class is a crucial part of scoping ViewModels to UI components like dialogs or bottom sheets
 * that don't have their own navigation back stack entry. It holds references to ViewModelStores
 * in a map, allowing them to survive configuration changes on Android (e.g., screen rotation),
 * which would otherwise reset the state of a composable.
 *
 * It is typically scoped to a parent component with a lifecycle, such as a screen, navigation graph,
 * or an activity. When this parent is destroyed, this registry ensures that all the ViewModelStores it manages
 * are also cleared, preventing memory leaks.
 *
 * NOTE: This implementation does not handle all edge cases, such as Android process death. If the
 * application process is killed by the OS while in the background, the state of this registry
 * (the `stores` map) will be lost. For this project's purposes, this is an acceptable trade-off,
 * as the user would simply need to re-open the dialog. A more robust solution might involve
 * saving the state to a `SavedStateHandle`.
 */
class ScopedStoreRegistryViewModel : ViewModel() {

    /**
     * A map that stores `ViewModelStore` instances, using a unique string ID as the key.
     *
     * This map is the core of the registry, holding the stores that in turn hold the scoped ViewModels.
     * This allows for retrieval, creation, and cleanup of ViewModel lifecycles on demand.
     */
    private val stores = mutableMapOf<String, ViewModelStore>()

    /**
     * Retrieves an existing `ViewModelStore` for the given `id`, or creates and stores a new one if it doesn't exist.
     *
     * This function is the main entry point for accessing a scoped ViewModel store.
     *
     * @param id The unique identifier for the `ViewModelStore`.
     * @return The existing or newly created `ViewModelStore` associated with the `id`.
     */
    fun getOrCreate(id: String): ViewModelStore =
        stores.getOrPut(id) { ViewModelStore() }

    /**
     * Clears the `ViewModelStore` associated with the given `id` and removes it from the registry.
     *
     * Calling this function will trigger the `onCleared()` method of any ViewModel instance
     * held by the cleared `ViewModelStore`, effectively destroying it.
     *
     * @param id The unique identifier for the store to be cleared and removed.
     */
    fun clear(id: String) {
        stores.remove(id)?.clear()
    }

    /**
     * Clears all stored `ViewModelStore` instances when this `ScopedStoreRegistryViewModel` is cleared.
     *
     * This is a critical cleanup step. It is invoked when the parent owner (e.g., a screen's ViewModel)
     * is destroyed. It iterates through all managed stores, clears each one to destroy the ViewModels
     * they contain, and then clears the map itself to prevent memory leaks.
     */
    override fun onCleared() {
        super.onCleared()
        stores.values.forEach { it.clear() }
        stores.clear()
    }
}
