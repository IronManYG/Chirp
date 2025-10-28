package dev.gaddal.auth.presentation.di

import dev.gaddal.auth.presentation.email_verification.EmailVerificationViewModel
import dev.gaddal.auth.presentation.login.LoginViewModel
import dev.gaddal.auth.presentation.register.RegisterViewModel
import dev.gaddal.auth.presentation.register_success.RegisterSuccessViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

/**
 * Dependency injection module that provides presentation-layer dependencies for authentication-related screens.
 *
 * This module includes the following ViewModels:
 * - `RegisterViewModel`: Manages the state and actions related to the user registration screen,
 *   including form validation, handling user inputs, and coordinating with the authentication service.
 * - `RegisterSuccessViewModel`: Handles the state for the registration success screen.
 * - `EmailVerificationViewModel`: Manages the logic for email verification workflows,
 *   such as tracking verification status.
 * - `LoginViewModel`: Handles the state and actions for the login screen, including user authentication logic.
 *
 * The ViewModels are registered using the dependency injection framework, making them available
 * for use within the application wherever needed.
 */
val authPresentationModule = module {
    viewModelOf(::RegisterViewModel)
    viewModelOf(::RegisterSuccessViewModel)
    viewModelOf(::EmailVerificationViewModel)
    viewModelOf(::LoginViewModel)
}