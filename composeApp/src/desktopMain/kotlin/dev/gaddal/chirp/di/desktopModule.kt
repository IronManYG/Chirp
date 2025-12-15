package dev.gaddal.chirp.di

import dev.gaddal.chirp.ApplicationStateHolder
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

/**
 * Defines a Koin module that provides the required dependencies for the desktop application.
 *
 * This module is used for dependency injection in the Chirp application. It registers a
 * singleton instance of `ApplicationStateHolder`, which manages the overall application state.
 * The module ensures that the application state and its associated logic, such as handling
 * window-related actions, is available throughout the application.
 */
val desktopModule = module {
    singleOf(::ApplicationStateHolder)
}