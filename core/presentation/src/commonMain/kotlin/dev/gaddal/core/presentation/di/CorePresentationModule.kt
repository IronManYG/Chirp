package dev.gaddal.core.presentation.di

import dev.gaddal.core.presentation.util.ScopedStoreRegistryViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

/**
 * Koin module definition for the core presentation layer of the application.
 *
 * This module provides dependencies specific to the core presentation logic, such as view models.
 * Included here is the registration of `ScopedStoreRegistryViewModel`, which is responsible for
 * managing scoped `ViewModelStore` instances within the application.
 *
 * The `corePresentationModule` can be integrated into the application's dependency graph by adding
 * it to the Koin setup during initialization.
 */
val corePresentationModule = module {
    viewModelOf(::ScopedStoreRegistryViewModel)
}