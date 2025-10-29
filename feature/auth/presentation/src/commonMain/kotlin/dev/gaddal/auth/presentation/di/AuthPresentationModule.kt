package dev.gaddal.auth.presentation.di

import dev.gaddal.auth.presentation.email_verification.EmailVerificationViewModel
import dev.gaddal.auth.presentation.forgot_password.ForgotPasswordViewModel
import dev.gaddal.auth.presentation.login.LoginViewModel
import dev.gaddal.auth.presentation.register.RegisterViewModel
import dev.gaddal.auth.presentation.register_success.RegisterSuccessViewModel
import dev.gaddal.auth.presentation.reset_password.ResetPasswordViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

/**
 * Dependency injection module that provides presentation-layer dependencies for authentication-related screens.
 *
 * The ViewModels are registered using the dependency injection framework, making them available
 * for use within the application wherever needed.
 */
val authPresentationModule = module {
    viewModelOf(::RegisterViewModel)
    viewModelOf(::RegisterSuccessViewModel)
    viewModelOf(::EmailVerificationViewModel)
    viewModelOf(::LoginViewModel)
    viewModelOf(::ForgotPasswordViewModel)
    viewModelOf(::ResetPasswordViewModel)
}