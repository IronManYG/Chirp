package dev.gaddal.chirp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import dev.gaddal.auth.presentation.navigation.AuthGraphRoutes
import dev.gaddal.auth.presentation.navigation.authGraph

/**
 * Represents the composable root for the navigation graph in the application.
 *
 * This function initializes a navigation controller and sets up a `NavHost` with the provided
 * navigation graph configuration. It defines the navigation flow starting from the authentication
 * graph's entry point. The `authGraph` extension method is invoked to add the authentication-related
 * navigation destinations to the graph.
 *
 * The root navigation specifically starts from the `AuthGraphRoutes.Graph` route,
 * allowing seamless integration of authentication and potential navigation extensions.
 */
@Composable
fun NavigationRoot() {
    val navController = rememberNavController()
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