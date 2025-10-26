package dev.gaddal.auth.presentation.di

import dev.gaddal.auth.presentation.register.RegisterViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

/**
 * The Koin module responsible for providing dependencies needed in the authentication presentation layer.
 *
 * This module includes definitions for the ViewModels used in features related to authentication, such
 * as `RegisterViewModel`, which handles the registration screen logic. It sets up the bindings to allow
 * Koin to resolve these ViewModels when requested in components tied to the UI layer.
 *
 * Example use case:
 * This module is typically added to the `modules` list during Koin initialization to make the associated
 * ViewModels and other dependencies available in the dependency graph. This ensures that the UI layer can
 * rely on properly configured dependencies for handling authentication workflow.
 */
val authPresentationModule = module {
    viewModelOf(::RegisterViewModel)
}