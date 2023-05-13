package com.dracula.socialnetworktwitch.feature_post.presentation.post_details

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dracula.socialnetworktwitch.R
import com.dracula.socialnetworktwitch.core.presentation.Semantics
import com.dracula.socialnetworktwitch.core.presentation.components.PostActionRow
import com.dracula.socialnetworktwitch.core.presentation.components.StandardAsyncImage
import com.dracula.socialnetworktwitch.core.presentation.components.StandardTextField
import com.dracula.socialnetworktwitch.core.presentation.components.StandardTopBar
import com.dracula.socialnetworktwitch.core.presentation.theme.MediumGray
import com.dracula.socialnetworktwitch.core.presentation.theme.PaddingLarge
import com.dracula.socialnetworktwitch.core.presentation.theme.PaddingMedium
import com.dracula.socialnetworktwitch.core.presentation.theme.PaddingSmall
import com.dracula.socialnetworktwitch.core.presentation.theme.ProfilePictureSizeMedium
import com.dracula.socialnetworktwitch.core.presentation.theme.SpaceLarge
import com.dracula.socialnetworktwitch.core.presentation.theme.SpaceSmall
import com.dracula.socialnetworktwitch.core.presentation.utils.Screens
import com.dracula.socialnetworktwitch.core.utils.UiEvent
import com.dracula.socialnetworktwitch.feature_post.presentation.comment.CommentItem
import kotlinx.coroutines.flow.collectLatest

@Composable
fun PostDetailsScreen(
    navController: NavController,
    scaffoldState: ScaffoldState,
    viewModel: PostDetailsViewModel = hiltViewModel(),

    ) {
    val state = viewModel.state
    val post = state.post
    val comments = state.comments
    val context = LocalContext.current
    val commentFieldState = viewModel.commentFieldState
    val commentState = viewModel.commentState
    val focusManager = LocalFocusManager.current
    LaunchedEffect(key1 = true) {
        viewModel.event.collectLatest { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> scaffoldState.snackbarHostState.showSnackbar(
                    message = event.uiText.asString(
                        context
                    )
                )

                else -> Unit
            }
        }
    }
    Box(modifier = Modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
        ) {
            StandardTopBar(
                title = stringResource(id = R.string.post_details),
                showBackButton = true,
                navController = navController
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
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
                                StandardAsyncImage(
                                    url = post?.imageUrl.orEmpty(),
                                    contentDescription = Semantics.ContentDescriptions.POST_PHOTO,
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(PaddingMedium)
                                ) {
                                    PostActionRow(
                                        username = state.post?.username.orEmpty(),
                                        modifier = Modifier.fillMaxWidth(),
                                        onLikeClicked = { isLiked ->
                                            viewModel.onEvent(PostDetailsAction.LikePost)
                                        },
                                        onShareClicked = {

                                        },
                                        onCommentClicked = {
                                        },
                                        onUsernameClicked = { username ->

                                        },
                                        isLiked = state.post?.isLiked == true
                                    )
                                    Text(
                                        text = post?.description.orEmpty(),
                                        style = MaterialTheme.typography.body2,
                                    )
                                    Spacer(modifier = Modifier.height(SpaceSmall))
                                    Text(
                                        text = stringResource(
                                            id = R.string.liked_by_x_people, post?.likeCount ?: 0
                                        ),
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.body2.copy(
                                            fontWeight = FontWeight.Bold,
                                        ),
                                        modifier = Modifier.clickable {
                                            navController.navigate(
                                                Screens.PersonListScreen.createRoute(
                                                    parentId = post?.id
                                                )
                                            )
                                        }
                                    )
                                }
                            }
                            StandardAsyncImage(
                                url = post?.profileImageUrl.orEmpty(),
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
                items(comments) { comment ->
                    CommentItem(
                        comment = comment,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = PaddingMedium, vertical = PaddingSmall),
                        onLikedByClicked = { commentId ->
                            navController.navigate(Screens.PersonListScreen.createRoute(parentId = commentId))
                        },
                        onLikedClicked = {
                            viewModel.onEvent(PostDetailsAction.LikeComment(commentId = comment.id))
                        }
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(PaddingMedium)
                    .background(color = MaterialTheme.colors.surface),
                verticalAlignment = Alignment.CenterVertically
            ) {
                StandardTextField(
                    text = commentFieldState.text,
                    onValueChanged = {
                        viewModel.onEvent(PostDetailsAction.CommentEntered(it))
                    },
                    hint = stringResource(id = R.string.enter_your_comment),
                    modifier = Modifier.weight(1f),
                    imeAction = ImeAction.Done,
                    keyboardActions = KeyboardActions(
                        onDone = {
                            viewModel.onEvent(PostDetailsAction.Comment)
                            focusManager.clearFocus()
                        }
                    ),

                    )
                if (commentState.isLoading)
                    CircularProgressIndicator()
                else
                    IconButton(
                        onClick = {
                            focusManager.clearFocus()
                            viewModel.onEvent(PostDetailsAction.Comment)
                        },
                        enabled = viewModel.commentFieldState.hasText
                    ) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = Semantics.ContentDescriptions.POST_PHOTO,
                            tint = if (commentFieldState.hasError) MaterialTheme.colors.background else MaterialTheme.colors.primary
                        )
                    }
            }
        }
        if (state.isPostLoading)
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
    }
}
