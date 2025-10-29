package dev.gaddal.chirp.di

import dev.gaddal.chirp.MainViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

/**
 * Koin module definition for dependency injection in the application.
 *
 * This module registers the `MainViewModel` instance, which is responsible
 * for managing the primary application state, including user authentication
 * and checking for authenticated sessions.
 *
 * The module is initialized in the application's dependency injection setup
 * to provide the necessary dependencies to the `MainViewModel`.
 */
val appModule = module {
    viewModelOf(::MainViewModel)
}