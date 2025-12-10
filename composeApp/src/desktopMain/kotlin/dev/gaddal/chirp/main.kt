package dev.gaddal.chirp

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import dev.gaddal.chirp.di.initKoin

/**
 * The entry point of the Chirp application.
 *
 * This method sets up the main application window with the specified title and close action. It initializes
 * the application's UI by invoking the root composable function `App`.
 *
 * The application window is created and managed using Jetpack Compose's Compose for Desktop library.
 * The `Window` function sets up the primary window, including its title and the behavior when the window is closed.
 */
fun main() {
    initKoin()
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "Chirp"
        ) {
            App()
        }
    }
}