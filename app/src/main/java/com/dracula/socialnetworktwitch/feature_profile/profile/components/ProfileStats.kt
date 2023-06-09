package com.dracula.socialnetworktwitch.feature_profile.profile.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import com.dracula.socialnetworktwitch.R
import com.dracula.socialnetworktwitch.core.domain.model.User
import com.dracula.socialnetworktwitch.core.presentation.theme.SpaceMedium
import com.dracula.socialnetworktwitch.core.presentation.theme.SpaceSmall

@Composable
fun ProfileStats(
    user: User,
    modifier: Modifier = Modifier,
    isFollowing: Boolean = true,
    isOwnProfile: Boolean = true,
    onFollowClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        ProfileNumber(
            number = user.followingCount,
            text = stringResource(id = R.string.following)
        )
        Spacer(modifier = Modifier.width(SpaceMedium))
        ProfileNumber(
            number = user.followerCount,
            text = pluralStringResource(id = R.plurals.x_follower, count = user.followerCount)
        )
        Spacer(modifier = Modifier.width(SpaceMedium))
        ProfileNumber(
            number = user.postCount,
            text = pluralStringResource(id = R.plurals.x_post, count = user.postCount)
        )
        Spacer(modifier = Modifier.width(SpaceSmall))
        AnimatedVisibility(
            visible = !isOwnProfile,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Button(
                onClick = onFollowClick,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = if (isFollowing) Color.Red else MaterialTheme.colors.primary
                )
            ) {
                Text(
                    text = if (isFollowing)
                        stringResource(id = R.string.unfollow)
                    else
                        stringResource(id = R.string.follow),
                    color = if (isFollowing) Color.White else MaterialTheme.colors.onPrimary
                )
            }

        }

    }
}