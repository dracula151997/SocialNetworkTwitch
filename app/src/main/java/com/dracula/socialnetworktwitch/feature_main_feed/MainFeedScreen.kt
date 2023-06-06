package com.dracula.socialnetworktwitch.feature_main_feed

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dracula.socialnetworktwitch.R
import com.dracula.socialnetworktwitch.core.presentation.Semantics
import com.dracula.socialnetworktwitch.core.presentation.components.StandardTopBar
import com.dracula.socialnetworktwitch.core.presentation.utils.Screens
import com.dracula.socialnetworktwitch.core.utils.UiEvent
import com.dracula.socialnetworktwitch.core.utils.sendSharePostIntent
import com.dracula.socialnetworktwitch.feature_post.presentation.components.PostItem
import kotlinx.coroutines.flow.collectLatest

@Composable
fun MainFeedScreen(
    navController: NavController,
    scaffoldState: ScaffoldState,
    viewModel: MainFeedViewModel = hiltViewModel()
) {
    val postsPagingState = viewModel.postsPagingState
    val posts = postsPagingState.items
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                MainFeedUiEvent.LikedPost -> {
                }

                is UiEvent.ShowSnackbar -> scaffoldState.snackbarHostState.showSnackbar(
                    message = event.uiText.asString(
                        context
                    )
                )
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        StandardTopBar(
            title = stringResource(id = R.string.your_feed),
            navController = navController
        ) {
            IconButton(onClick = { navController.navigate(Screens.SearchScreen.route) }) {
                Icon(
                    imageVector = Icons.Outlined.Search,
                    contentDescription = Semantics.ContentDescriptions.MAKE_POST,
                    tint = MaterialTheme.colors.onBackground
                )
            }
        }
        Box(modifier = Modifier.fillMaxSize()) {
            if (posts.isEmpty()) {
                Text(
                    text = stringResource(id = R.string.msg_no_posts_to_display),
                    modifier = Modifier.align(Center),
                    style = MaterialTheme.typography.h2
                )
            } else {
                LazyColumn {
                    items(posts.size) { index ->
                        val post = posts[index]
                        if (index > posts.size - 1 && !postsPagingState.endReached && !postsPagingState.isLoading) {
                            viewModel.loadNextPost()
                        }
                        PostItem(
                            post = post,
                            onPostClicked = {
                                navController.navigate(
                                    Screens.PostDetailsScreen.createRoute(
                                        postId = post.id
                                    )
                                )
                            },
                            onUsernameClicked = {
                                navController.navigate(
                                    Screens.ProfileScreen.createRoute(
                                        userId = post.userId
                                    )
                                )
                            },
                            onShareClicked = { context.sendSharePostIntent(postId = post.id) },
                            onLikeClicked = { viewModel.onEvent(MainFeedAction.LikePost(post.id)) },
                            onCommentClicked = {
                                navController.navigate(
                                    Screens.PostDetailsScreen.createRoute(
                                        postId = post.id,
                                        showKeyboard = true
                                    )
                                )
                            },
                            onDeleteClicked = { postId ->
                                viewModel.onEvent(MainFeedAction.DeletePost(postId))
                            }
                        )

                    }
                }
                if (postsPagingState.isLoading) CircularProgressIndicator(
                    modifier = Modifier.align(
                        Center
                    )
                )
            }
        }
    }
}