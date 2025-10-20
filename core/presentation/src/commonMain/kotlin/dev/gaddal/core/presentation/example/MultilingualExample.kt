package dev.gaddal.core.presentation.example

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import chirp.core.presentation.generated.resources.Res
import chirp.core.presentation.generated.resources.app_language
import chirp.core.presentation.generated.resources.ar
import chirp.core.presentation.generated.resources.change_lang
import chirp.core.presentation.generated.resources.en
import chirp.core.presentation.generated.resources.hello
import dev.gaddal.core.presentation.util.LanguageManager
import dev.gaddal.core.presentation.util.ProvideMultilingualSupport
import org.jetbrains.compose.resources.stringResource

/**
 * A self-contained demo showing multilingual switching (EN/AR) and RTL mirroring.
 *
 * This example demonstrates the recommended pattern using [LanguageManager]:
 * - Centralized language state management
 * - Validation of language codes before switching
 * - Automatic RTL detection
 * - Observable state that triggers recomposition
 *
 * For production apps:
 * 1. Create a LanguageManager instance at app level (e.g., in DI or remember in App())
 * 2. Optionally load saved language preference on startup
 * 3. Wrap your root composable with ProvideAppLanguage and ProvideLayoutDirectionFromLanguage
 * 4. Use manager.setLanguage() to switch languages from settings or any screen
 */
@Composable
fun MultilingualExampleDemo() {
    // In a real app, create this at app level and pass down or inject via DI
    val languageManager = remember {
        LanguageManager(
            supportedLanguages = setOf("en", "ar"),
            defaultLanguage = "en",
            initialLanguage = null // In production: load from saved preferences
        )
    }

    // Provide language state to the composition tree
    ProvideMultilingualSupport(languageManager.currentLanguage) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .safeContentPadding()
                .fillMaxSize(),
        ) {
            val displayLang = if (languageManager.currentLanguage == "ar")
                stringResource(Res.string.ar)
            else
                stringResource(Res.string.en)

            Text(stringResource(Res.string.app_language, displayLang))

            Button(onClick = {
                languageManager.setLanguage("en")
            }) {
                Text(stringResource(Res.string.en))
            }

            Button(onClick = {
                languageManager.setLanguage("ar")
            }) {
                Text(stringResource(Res.string.ar))
            }

            // Localized sample text
            Text(stringResource(Res.string.hello))

            Button(onClick = {
                // no-op placeholder to show a localized action label
            }) {
                Text(stringResource(Res.string.change_lang))
            }
        }
    }
}
