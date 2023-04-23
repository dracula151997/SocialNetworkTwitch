package com.dracula.socialnetworktwitch.feature_profile.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dracula.socialnetworktwitch.R
import com.dracula.socialnetworktwitch.feature_profile.domain.User
import com.dracula.socialnetworktwitch.core.presentation.Semantics
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
    onEditClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .offset(y = -profilePictureSize / 2),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.philipp),
            contentDescription = Semantics.ContentDescriptions.PROFILE_PICTURE,
            modifier = Modifier
                .size(profilePictureSize)
                .clip(CircleShape)
                .border(
                    width = 1.dp,
                    color = LightGray,
                    shape = CircleShape
                )

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
            if (isOwnProfile)
                IconButton(onClick = onEditClick, modifier = Modifier.size(30.dp)) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = Semantics.ContentDescriptions.EDIT_PROFILE_ICON
                    )
                }
        }
        Text(
            text = user.description,
            style = MaterialTheme.typography.body1,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(SpaceMedium))
        ProfileStats(user = user, isFollowing = false, isOwnProfile = isOwnProfile) {

        }
    }

}