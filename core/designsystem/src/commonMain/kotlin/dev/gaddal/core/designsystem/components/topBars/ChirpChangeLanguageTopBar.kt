@file:OptIn(ExperimentalMaterial3Api::class)

package dev.gaddal.core.designsystem.components.topBars

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * A top bar composable for displaying a title and providing an action to change the language.
 *
 * @param title The display text for the top bar, typically indicating the current language.
 * @param modifier A [Modifier] for customizing the appearance and layout of the top bar.
 * @param onChangeLanguageClick A callback invoked when the user interacts with the change language action.
 */
@Composable
fun ChirpChangeLanguageTopBar(
    title: String,
    modifier: Modifier = Modifier,
    onChangeLanguageClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 24.dp, end = 16.dp),
        horizontalArrangement = Arrangement.End
    ) {
        TextButton(onClick = onChangeLanguageClick) {
            Text(text = title)
        }
    }
}