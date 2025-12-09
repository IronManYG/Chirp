package dev.gaddal.chirp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import dev.gaddal.auth.presentation.navigation.AuthGraphRoutes
import dev.gaddal.chat.presentation.navigation.ChatGraphRoutes
import dev.gaddal.chirp.navigation.DeepLinkListener
import dev.gaddal.chirp.navigation.NavigationRoot
import dev.gaddal.core.designsystem.theme.ChirpTheme
import dev.gaddal.core.presentation.util.LanguageManager
import dev.gaddal.core.presentation.util.ObserveAsEvents
import dev.gaddal.core.presentation.util.ProvideMultilingualSupport
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

/**
 * Root composable function for the application. It sets up navigation, theme, and language support.
 *
 * @param onAuthenticationChecked A callback invoked when authentication checking is complete.
 * @param viewModel The main view model that provides the application state and handles business logic.
 */
@Composable
@Preview
fun App(
    onAuthenticationChecked: () -> Unit = {},
    viewModel: MainViewModel = koinViewModel()
) {
    val navController = rememberNavController()

    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.isCheckingAuth) {
        if (!state.isCheckingAuth) {
            onAuthenticationChecked()
        }
    }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is MainEvent.OnSessionExpired -> {
                navController.navigate(AuthGraphRoutes.Graph) {
                    popUpTo(AuthGraphRoutes.Graph) {
                        inclusive = false
                    }
                }
            }
        }
    }

    val languageManager: LanguageManager = koinInject()

    ChirpTheme(
        languageCode = languageManager.currentLanguage
    ) {
        if (!state.isCheckingAuth && !state.isCheckingLanguage) {
            ProvideMultilingualSupport(languageManager.currentLanguage) {
                NavigationRoot(
                    navController = navController,
                    startDestination = if (state.isLoggedIn) {
                        ChatGraphRoutes.Graph
                    } else {
                        AuthGraphRoutes.Graph
                    },
                    startAtLanguageSelection = !state.hasChosenLanguage
                )
                DeepLinkListener(navController)
            }
        }
    }
}