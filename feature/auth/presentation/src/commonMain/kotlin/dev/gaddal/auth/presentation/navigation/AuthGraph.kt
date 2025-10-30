package dev.gaddal.auth.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import dev.gaddal.auth.presentation.email_verification.EmailVerificationRoot
import dev.gaddal.auth.presentation.forgot_password.ForgotPasswordRoot
import dev.gaddal.auth.presentation.login.LoginRoot
import dev.gaddal.auth.presentation.register.RegisterRoot
import dev.gaddal.auth.presentation.register_success.RegisterSuccessRoot
import dev.gaddal.auth.presentation.reset_password.ResetPasswordRoot

/**
 * Configures the navigation graph for the authentication flow within the application.
 * Establishes routes and their corresponding UI components, handling navigation
 * and transitions between authentication-related destinations such as login, registration,
 * forgot password, email verification, and reset password screens.
 *
 * @param navController The NavController used to manage navigation within the app.
 * @param onLoginSuccess A callback that is invoked when the user successfully logs in.
 * @param startAtLanguageSelection Whether to start at language selection screen instead of login, defaults to false.
 */
fun NavGraphBuilder.authGraph(
    navController: NavController,
    onLoginSuccess: () -> Unit,
    startAtLanguageSelection: Boolean = false,
) {
    navigation<AuthGraphRoutes.Graph>(
        startDestination = if (startAtLanguageSelection) AuthGraphRoutes.LanguageSelection else AuthGraphRoutes.Login
    ) {
        composable<AuthGraphRoutes.LanguageSelection> {
            dev.gaddal.auth.presentation.language.LanguageSelectionRoot(
                onCompleted = {
                    navController.navigate(AuthGraphRoutes.Login) {
                        popUpTo<AuthGraphRoutes.LanguageSelection> {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }
        composable<AuthGraphRoutes.Login> {
            LoginRoot(
                onLoginSuccess = onLoginSuccess,
                onForgotPasswordClick = {
                    navController.navigate(AuthGraphRoutes.ForgotPassword)
                },
                onCreateAccountClick = {
                    navController.navigate(AuthGraphRoutes.Register) {
                        restoreState = true
                        launchSingleTop = true
                    }
                }
            )
        }
        composable<AuthGraphRoutes.Register> {
            RegisterRoot(
                onRegisterSuccess = {
                    navController.navigate(AuthGraphRoutes.RegisterSuccess(it))
                },
                onLoginClick = {
                    navController.navigate(AuthGraphRoutes.Login) {
                        popUpTo(AuthGraphRoutes.Register) {
                            inclusive = true
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
        composable<AuthGraphRoutes.RegisterSuccess> {
            RegisterSuccessRoot(
                onLoginClick = {
                    navController.navigate(AuthGraphRoutes.Login) {
                        popUpTo<AuthGraphRoutes.RegisterSuccess> {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable<AuthGraphRoutes.EmailVerification>(
            deepLinks = listOf(
                navDeepLink {
                    this.uriPattern = "https://chirp.pl-coding.com/api/auth/verify?token={token}"
                },
                navDeepLink {
                    this.uriPattern = "chirp://chirp.pl-coding.com/api/auth/verify?token={token}"
                },
            )
        ) {
            EmailVerificationRoot(
                onLoginClick = {
                    navController.navigate(AuthGraphRoutes.Login) {
                        popUpTo<AuthGraphRoutes.EmailVerification> {
                            inclusive = true
                        }
                    }
                },
                onCloseClick = {
                    navController.navigate(AuthGraphRoutes.Login) {
                        popUpTo<AuthGraphRoutes.EmailVerification> {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable<AuthGraphRoutes.ForgotPassword> {
            ForgotPasswordRoot()
        }
        composable<AuthGraphRoutes.ResetPassword>(
            deepLinks = listOf(
                navDeepLink {
                    this.uriPattern =
                        "https://chirp.pl-coding.com/api/auth/reset-password?token={token}"
                },
                navDeepLink {
                    this.uriPattern =
                        "chirp://chirp.pl-coding.com/api/auth/reset-password?token={token}"
                },
            )
        ) {
            ResetPasswordRoot()
        }
    }
}