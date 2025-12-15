package dev.gaddal.chirp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.navigation.NavController
import androidx.navigation.NavUri

/**
 * Sets up a listener for handling deep links and navigates to the corresponding destination
 * in the navigation graph using the provided [navController].
 *
 * This function ensures that external deep links are routed to the appropriate destination
 * within the app's navigation structure. It leverages the `ExternalUriHandler` to listen
 * for new URI events and handles their navigation via the `NavController`.
 *
 * @param navController The navigation controller used for managing app navigation
 * in response to deep link URIs.
 * @param onSetup A callback invoked when deep link listener setup is complete.
 */
@Composable
fun DeepLinkListener(
    navController: NavController,
    onSetup: () -> Unit
) {
    DisposableEffect(Unit) {
        ExternalUriHandler.listener = { uri ->
            navController.navigate(NavUri(uri))
        }

        onSetup()

        onDispose {
            ExternalUriHandler.listener = null
        }
    }
}