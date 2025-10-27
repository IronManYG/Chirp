package dev.gaddal.chirp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import dev.gaddal.auth.presentation.navigation.AuthGraphRoutes
import dev.gaddal.auth.presentation.navigation.authGraph

/**
 * Represents the composable root for the navigation graph in the application.
 *
 * This function initializes a [NavHost] with the provided [navController] as the navigation controller.
 * It serves as the entry point for the application's navigation system, defining the navigation flow
 * starting from the authentication graph ([AuthGraphRoutes.Graph]).
 *
 * The [authGraph] extension is used to set up all authentication-related navigation destinations,
 * including registration, login, and email verification flows. Deep linking support is configured
 * within the auth graph for handling external navigation requests.
 *
 * @param navController The navigation controller that manages the app navigation.
 * @see AuthGraphRoutes
 * @see authGraph
 */
@Composable
fun NavigationRoot(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = AuthGraphRoutes.Graph
    ) {
        authGraph(
            navController = navController,
            onLoginSuccess = {

            }
        )
    }
}