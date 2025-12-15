package dev.gaddal.chirp

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.application
import dev.gaddal.chirp.deeplink.DesktopDeepLinkHandler
import dev.gaddal.chirp.di.desktopModule
import dev.gaddal.chirp.di.initKoin
import dev.gaddal.chirp.navigation.ExternalUriHandler
import dev.gaddal.chirp.theme.rememberAppTheme
import dev.gaddal.chirp.windows.ChirpWindow
import org.koin.compose.koinInject

/**
 * The entry point of the Chirp application.
 *
 * This function initializes the Koin dependency injection framework using the `desktopModule` and
 * sets up the application's UI and state management. It manages the lifecycle and behavior of
 * application windows using composables, reacts to window-related events such as adding or closing
 * windows, and ensures proper underlying application state synchronization.
 *
 * Function workflow:
 * - Initializes Koin with specified modules.
 * - Creates the composable application using `application {}`.
 * - Manages the application state using `ApplicationStateHolder` and observes its state through
 *   `collectAsState()`.
 * - Ensures the application exits when no windows remain open using `exitApplication()`.
 * - Iterates over active windows in the state and creates a `ChirpWindow` for each with
 *   corresponding lifecycle handlers (e.g. `onCloseRequest`, `onAddWindowClick`).
 *
 * @param args Command-line arguments passed to the application.
 */
fun main(args: Array<String>) {
    initKoin {
        modules(desktopModule)
    }

    DesktopDeepLinkHandler.setup()

    val initialDeepLink = args.firstOrNull {
        val cleanedDeepLink = it.trim('"')

        DesktopDeepLinkHandler.supportedUriPatterns.any { it.matches(cleanedDeepLink) }
    }?.trim('"')

    application {
        val applicationStateHolder = koinInject<ApplicationStateHolder>()
        val applicationState by applicationStateHolder.state.collectAsState()
        val windows = applicationState.windows

        var canReceiveDeepLink by remember {
            mutableStateOf(false)
        }

        LaunchedEffect(canReceiveDeepLink) {
            if (canReceiveDeepLink && initialDeepLink != null) {
                ExternalUriHandler.onNewUri(initialDeepLink)
            }
        }

        LaunchedEffect(windows) {
            if (windows.isEmpty()) {
                exitApplication()
            }
        }

        val appTheme = rememberAppTheme(applicationState.themePreference)

        for (window in windows) {
            key(window.id) {
                ChirpWindow(
                    appTheme = appTheme,
                    onCloseRequest = {
                        applicationStateHolder.onWindowCloseRequest(window.id)
                    },
                    onAddWindowClick = applicationStateHolder::onAddWindowClick,
                    onFocusChanged = { focused ->
                        applicationStateHolder.onWindowFocusChanged(window.id, focused)
                    },
                    onDeepLinkListenerSetup = {
                        canReceiveDeepLink = true
                    }
                )
            }
        }

        ChirpTrayMenu(
            state = applicationState.trayState,
            themePreferenceFromAppSettings = applicationState.themePreference,
            onThemePreferenceClick = applicationStateHolder::onThemePreferenceClick
        )
    }
}