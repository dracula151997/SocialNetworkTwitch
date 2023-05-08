package com.dracula.socialnetworktwitch.feature_post.presentation.comment

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.text.font.FontWeight
import com.dracula.socialnetworktwitch.R
import com.dracula.socialnetworktwitch.core.domain.model.Comment
import com.dracula.socialnetworktwitch.core.presentation.Semantics
import com.dracula.socialnetworktwitch.core.presentation.components.LikeButton
import com.dracula.socialnetworktwitch.core.presentation.components.StandardAsyncImage
import com.dracula.socialnetworktwitch.core.presentation.theme.PaddingSmall
import com.dracula.socialnetworktwitch.core.presentation.theme.ProfilePictureSizeSmall
import com.dracula.socialnetworktwitch.core.presentation.theme.SpaceMedium
import com.dracula.socialnetworktwitch.core.presentation.theme.SpaceSmall
import com.dracula.socialnetworktwitch.core.presentation.theme.StandardElevation

@Composable
fun CommentItem(
    comment: Comment,
    modifier: Modifier = Modifier,
    onLikedClicked: (Boolean) -> Unit = {},
) {
    Card(
        modifier = modifier,
        elevation = StandardElevation,
        shape = MaterialTheme.shapes.medium,
        backgroundColor = MaterialTheme.colors.onSurface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(PaddingSmall)
        ) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    StandardAsyncImage(
                        url = comment.profilePictureUrl,
                        contentDescription = Semantics.ContentDescriptions.PROFILE_PICTURE,
                        modifier = Modifier
                            .size(ProfilePictureSizeSmall)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(SpaceSmall))
                    Text(
                        text = comment.username,
                        style = MaterialTheme.typography.body1.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
                Text(
                    text = comment.formattedTime,
                    style = MaterialTheme.typography.body2
                )

            }
            Spacer(modifier = Modifier.height(SpaceMedium))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = comment.comment,
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier.weight(
                        9f
                    )
                )
                LikeButton(
                    isLiked = comment.isLiked,
                    onClick = onLikedClicked,
                    modifier = Modifier.weight(
                        1f
                    )
                )
            }
            Spacer(modifier = Modifier.height(SpaceMedium))
            Text(
                text = pluralStringResource(
                    id = R.plurals.liked_by_x_peoples,
                    count = comment.likeCount,
                    comment.likeCount
                ),
                style = MaterialTheme.typography.body2.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}