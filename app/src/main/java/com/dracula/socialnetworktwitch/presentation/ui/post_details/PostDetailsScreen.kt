package com.dracula.socialnetworktwitch.presentation.ui.post_details

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.dracula.socialnetworktwitch.R
import com.dracula.socialnetworktwitch.domain.model.Comment
import com.dracula.socialnetworktwitch.domain.model.Post
import com.dracula.socialnetworktwitch.presentation.components.BackIcon
import com.dracula.socialnetworktwitch.presentation.components.PostActionRow
import com.dracula.socialnetworktwitch.presentation.components.StandardTopBar
import com.dracula.socialnetworktwitch.presentation.ui.Semantics
import com.dracula.socialnetworktwitch.presentation.ui.comment.CommentItem
import com.dracula.socialnetworktwitch.presentation.ui.theme.*

@Composable
fun PostDetailsScreen(navController: NavController, post: Post) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {
        StandardTopBar(
            title = stringResource(id = R.string.your_feed),
            navigationIcon = {
                BackIcon {
                    navController.popBackStack()
                }
            }
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.surface),
        ) {
            item {
                Column(
                    Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colors.background)
                ) {
                    Spacer(modifier = Modifier.height(PaddingLarge))
                    Box(Modifier.fillMaxSize()) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .offset(y = ProfilePictureSizeMedium / 2)
                                .background(MediumGray)

                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.kermit),
                                contentDescription = Semantics.ContentDescriptions.POST_PHOTO,
                            )

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(PaddingMedium)
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
                                    text = post.description,
                                    style = MaterialTheme.typography.body2,
                                )
                                Spacer(modifier = Modifier.height(SpaceSmall))
                                Text(
                                    text = stringResource(
                                        id = R.string.liked_by_x_people,
                                        post.likeCount
                                    ),
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.body2.copy(
                                        fontWeight = FontWeight.Bold,
                                    )
                                )
                            }
                        }
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
                Spacer(modifier = Modifier.height(SpaceLarge))
            }
            items(4) {
                CommentItem(
                    comment = Comment.dummy(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = PaddingMedium, vertical = PaddingSmall)
                )
            }
        }
    }
}
