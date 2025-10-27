package dev.gaddal.auth.presentation.di

import dev.gaddal.auth.presentation.email_verification.EmailVerificationViewModel
import dev.gaddal.auth.presentation.register.RegisterViewModel
import dev.gaddal.auth.presentation.register_success.RegisterSuccessViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

/**
 * Dependency injection module for managing the dependency tree of ViewModels used in the authentication presentation layer.
 *
 * This module provides the following ViewModels:
 * - `RegisterViewModel`: Handles user interactions and state management for the registration screen.
 * - `RegisterSuccessViewModel`: Manages the presentation state for the registration success flow.
 * - `EmailVerificationViewModel`: Manages the state and logic for email verification processes.
 *
 * These ViewModels are registered with the dependency container, allowing them to be injected as needed
 * throughout the application using Koin.
 */
val authPresentationModule = module {
    viewModelOf(::RegisterViewModel)
    viewModelOf(::RegisterSuccessViewModel)
    viewModelOf(::EmailVerificationViewModel)
}