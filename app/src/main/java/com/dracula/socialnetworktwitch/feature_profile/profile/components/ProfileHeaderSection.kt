package com.dracula.socialnetworktwitch.feature_profile.profile.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Message
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dracula.socialnetworktwitch.core.domain.model.User
import com.dracula.socialnetworktwitch.core.presentation.Semantics
import com.dracula.socialnetworktwitch.core.presentation.components.StandardAsyncImage
import com.dracula.socialnetworktwitch.core.presentation.theme.LightGray
import com.dracula.socialnetworktwitch.core.presentation.theme.ProfilePictureSizeLarge
import com.dracula.socialnetworktwitch.core.presentation.theme.SpaceMedium
import com.dracula.socialnetworktwitch.core.presentation.theme.SpaceSmall

@Composable
fun ProfileHeaderSection(
    user: User,
    modifier: Modifier = Modifier,
    profilePictureSize: Dp = ProfilePictureSizeLarge,
    isOwnProfile: Boolean = true,
    isFollowing: Boolean = false,
    onLogoutClicked: () -> Unit = {},
    onEditClick: () -> Unit,
    onMessageClicked: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .offset(y = -profilePictureSize / 2),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        StandardAsyncImage(
            url = user.profilePictureUrl,
            modifier = Modifier
                .size(profilePictureSize)
                .clip(CircleShape)
                .border(
                    width = 1.dp,
                    color = LightGray,
                    shape = CircleShape
                ),
            contentDescription = Semantics.ContentDescriptions.PROFILE_PICTURE
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.offset(x = if (isOwnProfile) (30.dp + SpaceSmall) / 2f else 0.dp)
        ) {
            Text(
                text = user.username,
                style = MaterialTheme.typography.h1.copy(
                    fontSize = 24.sp
                ),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.width(SpaceSmall))
            if (isOwnProfile) {
                IconButton(onClick = onEditClick, modifier = Modifier.size(30.dp)) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = Semantics.ContentDescriptions.EDIT_PROFILE_ICON
                    )
                }
                IconButton(onClick = onLogoutClicked) {
                    Icon(
                        imageVector = Icons.Filled.Logout,
                        contentDescription = Semantics.ContentDescriptions.LOGOUT
                    )
                }
            } else {
                IconButton(onClick = onMessageClicked) {
                    Icon(imageVector = Icons.Default.Message, contentDescription = null)
                }
            }
        }
        Text(
            text = user.bio,
            style = MaterialTheme.typography.body2,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(SpaceMedium))
        ProfileStats(user = user, isFollowing = isFollowing, isOwnProfile = isOwnProfile) {

        }
    }

}