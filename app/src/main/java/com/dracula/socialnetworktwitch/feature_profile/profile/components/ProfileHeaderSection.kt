package com.dracula.socialnetworktwitch.feature_profile.profile.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
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
import com.dracula.socialnetworktwitch.core.domain.model.User
import com.dracula.socialnetworktwitch.core.presentation.Semantics
import com.dracula.socialnetworktwitch.core.presentation.components.StandardAsyncImage
import com.dracula.socialnetworktwitch.core.presentation.theme.LightGray
import com.dracula.socialnetworktwitch.core.presentation.theme.ProfilePictureSizeLarge

@Composable
fun ProfileHeaderSection(
    user: User,
    modifier: Modifier = Modifier,
    profilePictureSize: Dp = ProfilePictureSizeLarge,
    isOwnProfile: Boolean = true,
    isFollowing: Boolean = false,
    onFollowClicked: () -> Unit = {}
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
            contentDescription = Semantics.ContentDescriptions.PROFILE_PICTURE,
            placeholder = painterResource(id = R.drawable.avatar_placeholder),
            errorPlaceholder = painterResource(id = R.drawable.avatar_placeholder)
        )
        Text(
            text = user.username,
            style = MaterialTheme.typography.h1.copy(
                fontSize = 24.sp
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
        )
        Text(
            text = user.bio,
            style = MaterialTheme.typography.body2,
            textAlign = TextAlign.Center
        )
        ProfileStats(
            user = user,
            isFollowing = isFollowing,
            isOwnProfile = isOwnProfile,
            onFollowClick = onFollowClicked
        )
    }

}