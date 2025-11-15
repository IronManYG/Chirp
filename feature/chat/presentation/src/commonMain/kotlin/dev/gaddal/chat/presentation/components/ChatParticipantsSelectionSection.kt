package dev.gaddal.chat.presentation.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.gaddal.core.designsystem.components.avatar.ChatParticipantUi
import dev.gaddal.core.designsystem.components.avatar.ChirpAvatarPhoto
import dev.gaddal.core.designsystem.theme.ChirpTheme
import dev.gaddal.core.designsystem.theme.extended
import dev.gaddal.core.designsystem.theme.titleXSmall
import dev.gaddal.core.presentation.util.DeviceConfiguration
import dev.gaddal.core.presentation.util.currentDeviceConfiguration
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ColumnScope.ChatParticipantsSelectionSection(
    selectedParticipants: List<ChatParticipantUi>,
    modifier: Modifier = Modifier,
    searchResult: ChatParticipantUi? = null
) {
    val deviceConfiguration = currentDeviceConfiguration()
    val rootHeightModifier = when (deviceConfiguration) {
        DeviceConfiguration.TABLET_PORTRAIT,
        DeviceConfiguration.TABLET_LANDSCAPE,
        DeviceConfiguration.DESKTOP -> {
            Modifier
                .animateContentSize()
                .heightIn(min = 200.dp, max = 300.dp)
        }

        else -> Modifier
            .weight(1f)
    }

    Box(
        modifier = rootHeightModifier
            .then(modifier)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            searchResult?.let {
                item {
                    ChatParticipantListItem(
                        participantUi = searchResult,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            }

            if (selectedParticipants.isNotEmpty() && searchResult == null) {
                items(
                    items = selectedParticipants,
                    key = { it.id }
                ) { participant ->
                    ChatParticipantListItem(
                        participantUi = participant,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
fun ChatParticipantListItem(
    participantUi: ChatParticipantUi,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ChirpAvatarPhoto(
            displayText = participantUi.initials,
            imageUrl = participantUi.imageUrl
        )
        Text(
            text = participantUi.username,
            style = MaterialTheme.typography.titleXSmall,
            color = MaterialTheme.colorScheme.extended.textPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ChatParticipantsSelectionSectionPreview() {
    val participants = listOf(
        ChatParticipantUi(id = "1", username = "Philipp", initials = "PH"),
        ChatParticipantUi(id = "2", username = "John", initials = "JO"),
        ChatParticipantUi(id = "3", username = "Sabrina", initials = "SA"),
        ChatParticipantUi(id = "4", username = "Cinderella", initials = "CI"),
    )
    ChirpTheme {
        Column {
            ChatParticipantsSelectionSection(
                selectedParticipants = participants,
                modifier = Modifier
                    .fillMaxWidth(),
                searchResult = participants[0]
            )
        }
    }
}
