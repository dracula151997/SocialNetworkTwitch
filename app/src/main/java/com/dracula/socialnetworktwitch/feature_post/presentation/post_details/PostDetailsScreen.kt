package com.dracula.socialnetworktwitch.feature_post.presentation.post_details

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dracula.socialnetworktwitch.R
import com.dracula.socialnetworktwitch.core.presentation.Semantics
import com.dracula.socialnetworktwitch.core.presentation.components.LoadingContent
import com.dracula.socialnetworktwitch.core.presentation.components.PostActionRow
import com.dracula.socialnetworktwitch.core.presentation.components.SendTextField
import com.dracula.socialnetworktwitch.core.presentation.components.StandardAsyncImage
import com.dracula.socialnetworktwitch.core.presentation.components.StandardTopBar
import com.dracula.socialnetworktwitch.core.presentation.theme.MediumGray
import com.dracula.socialnetworktwitch.core.presentation.theme.PaddingLarge
import com.dracula.socialnetworktwitch.core.presentation.theme.PaddingMedium
import com.dracula.socialnetworktwitch.core.presentation.theme.PaddingSmall
import com.dracula.socialnetworktwitch.core.presentation.theme.ProfilePictureSizeMedium
import com.dracula.socialnetworktwitch.core.presentation.theme.SpaceLarge
import com.dracula.socialnetworktwitch.core.presentation.theme.SpaceSmall
import com.dracula.socialnetworktwitch.core.presentation.utils.Screens
import com.dracula.socialnetworktwitch.core.presentation.utils.states.validator.CommentFieldState
import com.dracula.socialnetworktwitch.core.utils.UiEvent
import com.dracula.socialnetworktwitch.core.utils.sendSharePostIntent
import com.dracula.socialnetworktwitch.feature_post.presentation.comment.CommentItem
import kotlinx.coroutines.flow.collectLatest

@Composable
fun PostDetailsRoute(
    showKeyboard: () -> Boolean = { true },
    showSnackbar: (message: String) -> Unit,
    onNavigate: (route: String) -> Unit,
    onNavUp: () -> Unit
) {
    val viewModel: PostDetailsViewModel = hiltViewModel()
    val context = LocalContext.current
    LaunchedEffect(key1 = true) {
        viewModel.event.collectLatest { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> showSnackbar(event.uiText.asString(context))
                else -> Unit
            }
        }
    }

    PostDetailsScreen(
        onNavigate = onNavigate,
        onNavUp = onNavUp,
        showKeyboard = showKeyboard(),
        commentFieldState = viewModel.commentFieldState,
        screenState = viewModel.state,
        onEvent = {
            viewModel.onEvent(it)
        },
        ownUserID = viewModel.ownUserId
    )

}

@Composable
private fun PostDetailsScreen(
    screenState: PostDetailsState,
    commentFieldState: CommentFieldState,
    onEvent: (event: PostDetailsEvent) -> Unit,
    onNavigate: (route: String) -> Unit,
    onNavUp: () -> Unit,
    showKeyboard: Boolean = false,
    ownUserID: String = ""
) {
    val post = screenState.post
    val comments = screenState.comments
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val focusRequester = remember {
        FocusRequester()
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
                onBack = onNavUp
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .background(MaterialTheme.colors.surface),
            ) {
                item {
                    LoadingContent(
                        isLoading = screenState.isPostLoading,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = SpaceLarge)
                    ) {
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
                                            username = screenState.post?.username.orEmpty(),
                                            modifier = Modifier.fillMaxWidth(),
                                            onLikeClicked = { isLiked ->
                                                onEvent(PostDetailsEvent.LikePost)
                                            },
                                            onShareClicked = {
                                                context.sendSharePostIntent(postId = post?.id.orEmpty())
                                            },
                                            onCommentClicked = {
                                                focusRequester.requestFocus()
                                            },
                                            onUsernameClicked = { username ->
                                                onNavigate(
                                                    Screens.ProfileScreen.createRoute(
                                                        userId = post?.userId,
                                                    ),
                                                )
                                            },
                                            isLiked = screenState.post?.isLiked == true,
                                            isOwnPost = post?.isOwnPost == true || post?.userId == ownUserID
                                        )
                                        Text(
                                            text = post?.description.orEmpty(),
                                            style = MaterialTheme.typography.body2,
                                        )
                                        Spacer(modifier = Modifier.height(SpaceSmall))
                                        Text(
                                            text = stringResource(
                                                id = R.string.x_likes, post?.likeCount ?: 0
                                            ),
                                            fontWeight = FontWeight.Bold,
                                            style = MaterialTheme.typography.body2.copy(
                                                fontWeight = FontWeight.Bold,
                                            ),
                                            modifier = Modifier.clickable {
                                                onNavigate(
                                                    Screens.PersonListScreen.createRoute(
                                                        parentId = post?.id
                                                    )
                                                )
                                            }
                                        )
                                    }
                                }
                                StandardAsyncImage(
                                    url = post?.profileImageUrl,
                                    contentDescription = Semantics.ContentDescriptions.PROFILE_PICTURE,
                                    errorPlaceholder = painterResource(id = R.drawable.avatar_placeholder),
                                    placeholder = painterResource(id = R.drawable.avatar_placeholder),
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .border(
                                            BorderStroke(
                                                1.dp,
                                                color = MaterialTheme.colors.primary,
                                            ),
                                            shape = CircleShape
                                        )
                                        .clip(CircleShape)
                                        .size(ProfilePictureSizeMedium)
                                        .background(color = Color.LightGray, shape = CircleShape)
                                        .align(Alignment.TopCenter)
                                )

                            }

                        }
                    }
                }

                items(comments, key = { it.id }) { comment ->
                    CommentItem(
                        comment = comment,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = PaddingMedium, vertical = PaddingSmall),
                        onLikedByClicked = { commentId ->
                            onNavigate(Screens.PersonListScreen.createRoute(parentId = commentId))
                        },
                        onLikedClicked = {
                            onEvent(PostDetailsEvent.LikeComment(comment.id))
                        }
                    )
                }
            }
            SendTextField(
                state = commentFieldState,
                hint = stringResource(id = R.string.enter_your_comment),
                onSend = {
                    onEvent(PostDetailsEvent.Comment)
                    focusManager.clearFocus()
                },
                focusRequester = focusRequester,
            )
        }
        if (screenState.isPostLoading)
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )

        LaunchedEffect(key1 = Unit) {
            if (showKeyboard)
                focusRequester.requestFocus()
        }
    }
}

