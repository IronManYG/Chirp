package dev.gaddal.core.presentation.di

import dev.gaddal.core.presentation.util.ScopedStoreRegistryViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

/**
 * Represents the platform-specific core presentation module for dependency injection.
 *
 * This variable provides an instance of the `Module` that defines the platform-specific
 * dependencies required for the core presentation layer of the application.
 * It is expected to be implemented for each platform the application supports.
 */
expect val platformCorePresentationModule: Module

/**
 * Koin module definition for the core presentation layer of the application.
 *
 * Includes platform-specific presentation modules and provides registration for
 * `ScopedStoreRegistryViewModel`, which manages scoped ViewModelStores.
 */
val corePresentationModule = module {
    includes(platformCorePresentationModule)

    viewModelOf(::ScopedStoreRegistryViewModel)
}