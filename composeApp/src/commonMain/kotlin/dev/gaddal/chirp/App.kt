package dev.gaddal.chirp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController
import dev.gaddal.chirp.navigation.DeepLinkListener
import dev.gaddal.chirp.navigation.NavigationRoot
import dev.gaddal.core.designsystem.theme.ChirpTheme
import dev.gaddal.core.presentation.util.LanguageManager
import dev.gaddal.core.presentation.util.ProvideMultilingualSupport
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {

    val languageManager = remember {
        LanguageManager(
            supportedLanguages = setOf("en", "ar"),
            defaultLanguage = "en",
            initialLanguage = null // In production: load from saved preferences
        )
    }

    val navController = rememberNavController()
    DeepLinkListener(navController)

    ChirpTheme(
        languageCode = languageManager.currentLanguage
    ) {
        // Provide language state to the composition tree
        ProvideMultilingualSupport(languageManager.currentLanguage) {
            NavigationRoot(navController)
        }
    }
}