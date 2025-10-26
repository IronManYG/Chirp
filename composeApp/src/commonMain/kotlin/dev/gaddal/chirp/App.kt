package dev.gaddal.chirp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import dev.gaddal.auth.presentation.register.RegisterRoot
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

    ChirpTheme(
        languageCode = languageManager.currentLanguage
    ) {
        // Provide language state to the composition tree
        ProvideMultilingualSupport(languageManager.currentLanguage) {
            RegisterRoot(
                onRegisterSuccess = {}
            )
        }
    }
}