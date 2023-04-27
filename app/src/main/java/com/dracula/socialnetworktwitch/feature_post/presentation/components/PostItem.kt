package com.dracula.socialnetworktwitch.feature_post.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dracula.socialnetworktwitch.core.utils.Constants
import com.dracula.socialnetworktwitch.R
import com.dracula.socialnetworktwitch.core.presentation.components.PostActionRow
import com.dracula.socialnetworktwitch.core.presentation.theme.HintGray
import com.dracula.socialnetworktwitch.core.presentation.theme.MediumGray
import com.dracula.socialnetworktwitch.core.presentation.theme.PaddingMedium
import com.dracula.socialnetworktwitch.core.presentation.theme.PaddingSmall
import com.dracula.socialnetworktwitch.core.presentation.theme.ProfilePictureSizeMedium
import com.dracula.socialnetworktwitch.core.presentation.theme.SpaceSmall
import com.dracula.socialnetworktwitch.core.domain.model.Post
import com.dracula.socialnetworktwitch.core.presentation.Semantics

@Composable
fun PostItem(
    post: Post,
    modifier: Modifier = Modifier,
    showProfileImage: Boolean = true,
    onPostClicked: (Post) -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = PaddingMedium, vertical = PaddingSmall)

    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = if (showProfileImage) ProfilePictureSizeMedium / 2 else 0.dp)
                .clip(MaterialTheme.shapes.medium)
                .shadow(elevation = 5.dp)
                .background(MediumGray)
                .clickable { onPostClicked(post) }
        ) {
            Image(
                painter = painterResource(id = R.drawable.kermit),
                contentDescription = Semantics.ContentDescriptions.POST_PHOTO,
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(PaddingSmall)
            ) {
                PostActionRow(
                    username = post.username,
                    modifier = Modifier.fillMaxWidth(),
                    onLikeClicked = { isLiked ->

                    },
                    onShareClicked = {

                    },
                    onCommentClicked = {

                    },
                    onUsernameClicked = { username ->

                    }
                )
                Text(
                    text = buildAnnotatedString {
                        append(post.description)
                        withStyle(
                            SpanStyle(
                                color = HintGray
                            )
                        ) {
                            append(stringResource(id = R.string.read_more))
                        }
                    },
                    style = MaterialTheme.typography.body2,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = Constants.MAX_POST_DESCRIPTION_LINES,

                    )
                Spacer(modifier = Modifier.height(SpaceSmall))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(id = R.string.liked_by_x_people, post.likeCount),
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.h2.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                        )
                    )
                    if (post.commentCount > 0)
                        Text(
                            text = pluralStringResource(
                                id = R.plurals.x_comments,
                                count = post.commentCount,
                                post.commentCount
                            ),
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.h2.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        )

                }
            }
        }

        if (showProfileImage)
            Image(
                painter = painterResource(id = R.drawable.philipp),
                contentDescription = Semantics.ContentDescriptions.PROFILE_PICTURE,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(ProfilePictureSizeMedium)
                    .align(Alignment.TopCenter)
            )
    }
}