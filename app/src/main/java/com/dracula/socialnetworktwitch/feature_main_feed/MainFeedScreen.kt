package com.dracula.socialnetworktwitch.feature_main_feed

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.dracula.socialnetworktwitch.R
import com.dracula.socialnetworktwitch.core.domain.model.Post
import com.dracula.socialnetworktwitch.core.presentation.Semantics
import com.dracula.socialnetworktwitch.core.presentation.components.PullToRefreshBox
import com.dracula.socialnetworktwitch.core.presentation.components.StandardTopBar
import com.dracula.socialnetworktwitch.core.presentation.theme.appFontFamily
import com.dracula.socialnetworktwitch.core.presentation.utils.Screens
import com.dracula.socialnetworktwitch.core.utils.ErrorView
import com.dracula.socialnetworktwitch.core.utils.LoadingState
import com.dracula.socialnetworktwitch.core.utils.PagingState
import com.dracula.socialnetworktwitch.core.utils.UiEvent
import com.dracula.socialnetworktwitch.core.utils.sendSharePostIntent
import com.dracula.socialnetworktwitch.feature_post.presentation.components.PostItem
import kotlinx.coroutines.flow.collectLatest

@Composable
fun MainFeedRoute(
    modifier: Modifier = Modifier,
    onNavigate: (route: String) -> Unit,
    onNavUp: () -> Unit,
    showSnackbar: (message: String) -> Unit,
) {
    val viewModel: MainFeedViewModel = hiltViewModel()
    val context = LocalContext.current
    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> showSnackbar(event.uiText.asString(context = context))
                is UiEvent.Navigate -> onNavigate(event.route)
                UiEvent.NavigateUp -> onNavUp()
            }
        }
    }
    MainFeedScreen(
        modifier = modifier,
        onNavigate = onNavigate,
        postsPagingState = viewModel.postsPagingState,
        onEvent = { event ->
            viewModel.onEvent(event)
        }

    )

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun MainFeedScreen(
    modifier: Modifier = Modifier,
    postsPagingState: PagingState<Post>,
    onEvent: (event: MainFeedAction) -> Unit,
    onNavigate: (route: String) -> Unit,
) {
    val posts = postsPagingState.items
    val context = LocalContext.current
    val pullToRefreshState =
        rememberPullRefreshState(
            refreshing = postsPagingState.refreshing,
            onRefresh = { onEvent(MainFeedAction.Refresh) })

    Column(
        modifier = modifier
            .fillMaxSize()

    ) {
        StandardTopBar(
            title = stringResource(id = R.string.your_feed),
        ) {
            IconButton(onClick = { onNavigate(Screens.SearchScreen.route) }) {
                Icon(
                    imageVector = Icons.Outlined.Search,
                    contentDescription = Semantics.ContentDescriptions.MAKE_POST,
                    tint = MaterialTheme.colors.onBackground
                )
            }
        }
        PullToRefreshBox(
            state = pullToRefreshState,
            refreshing = postsPagingState.refreshing,
        ) {
            LazyColumn {
                when {
                    postsPagingState.isLoading -> item {
                        LoadingState(modifier = Modifier.fillParentMaxSize())
                    }

                    posts.isEmpty() -> item {
                        ErrorView(
                            errorMessage = stringResource(id = R.string.msg_no_posts_to_display),
                            modifier = Modifier.fillParentMaxSize(),
                            textStyle = MaterialTheme.typography.h6.copy(
                                fontFamily = appFontFamily
                            )
                        )
                    }

                    else -> items(posts.size) { index ->
                        val post = posts[index]
                        if (index > posts.size - 1 && !postsPagingState.endReached) {
                            onEvent(MainFeedAction.LoadNextPosts)
                        }
                        PostItem(
                            post = post,
                            onPostClicked = {
                                onNavigate(
                                    Screens.PostDetailsScreen.createRoute(
                                        postId = post.id,
                                    ),
                                )
                            },
                            onUsernameClicked = {
                                onNavigate(
                                    Screens.ProfileScreen.createRoute(
                                        userId = post.userId
                                    )
                                )
                            },
                            onShareClicked = { context.sendSharePostIntent(postId = post.id) },
                            onLikeClicked = { onEvent(MainFeedAction.LikePost(post.id)) },
                            onCommentClicked = {
                                onNavigate(
                                    Screens.PostDetailsScreen.createRoute(
                                        postId = post.id,
                                        showKeyboard = true
                                    )
                                )
                            },
                            onDeleteClicked = { postId ->
                                onEvent(MainFeedAction.DeletePost(postId))
                            }
                        )

                    }


                }
            }

            PullRefreshIndicator(
                refreshing = postsPagingState.refreshing,
                state = pullToRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}