package com.dracula.socialnetworktwitch.feature_chat.presentation.chat

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dracula.socialnetworktwitch.R
import com.dracula.socialnetworktwitch.core.presentation.components.StandardAsyncImage
import com.dracula.socialnetworktwitch.core.presentation.theme.ProfilePictureSizeSmall
import com.dracula.socialnetworktwitch.core.presentation.theme.SpaceMedium
import com.dracula.socialnetworktwitch.core.presentation.theme.SpaceSmall
import com.dracula.socialnetworktwitch.core.presentation.theme.appFontFamily
import com.dracula.socialnetworktwitch.feature_chat.domain.model.Chat

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ChatUserProfileItem(
    chat: Chat,
    modifier: Modifier = Modifier,
    onItemClick: (chat: Chat) -> Unit,
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        onClick = {
            onItemClick(chat)
        },
        elevation = 5.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    vertical = SpaceSmall,
                    horizontal = SpaceMedium
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            StandardAsyncImage(
                url = chat.remoteUserProfilePictureUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(ProfilePictureSizeSmall)
                    .clip(CircleShape),
                placeholder = painterResource(id = R.drawable.avatar_placeholder),
                errorPlaceholder = painterResource(id = R.drawable.avatar_placeholder),
            )
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .padding(horizontal = SpaceSmall)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = chat.remoteUsername,
                        style = MaterialTheme.typography.body1.copy(
                            fontWeight = FontWeight.Bold,
                            fontFamily = appFontFamily
                        ),
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = chat.formattedTimestamp,
                        style = MaterialTheme.typography.body1.copy(
                            fontWeight = FontWeight.Normal,
                            fontSize = 12.sp,
                        )
                    )
                }
                Text(
                    text = chat.lastMessage,
                    style = MaterialTheme.typography.body2,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2
                )
            }
        }
    }

}