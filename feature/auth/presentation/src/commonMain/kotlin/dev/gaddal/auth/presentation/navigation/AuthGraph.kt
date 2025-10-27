package dev.gaddal.auth.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import dev.gaddal.auth.presentation.register.RegisterRoot
import dev.gaddal.auth.presentation.register_success.RegisterSuccessRoot

/**
 * Defines the authentication navigation graph.
 *
 * This function sets up the navigation graph structure for the authentication flow, including
 * destinations for user registration and registration success. It starts from the `Register`
 * destination and transitions to the `RegisterSuccess` destination when the registration process
 * is completed successfully.
 *
 * @param navController The NavController used to manage navigation within the app.
 * @param onLoginSuccess A callback that is invoked when the user successfully logs in.
 */
fun NavGraphBuilder.authGraph(
    navController: NavController,
    onLoginSuccess: () -> Unit,
) {
    navigation<AuthGraphRoutes.Graph>(
        startDestination = AuthGraphRoutes.Register
    ) {
        composable<AuthGraphRoutes.Register> {
            RegisterRoot(
                onRegisterSuccess = {
                    navController.navigate(AuthGraphRoutes.RegisterSuccess(it))
                }
            )
        }
        composable<AuthGraphRoutes.RegisterSuccess> {
            RegisterSuccessRoot()
        }
    }
}