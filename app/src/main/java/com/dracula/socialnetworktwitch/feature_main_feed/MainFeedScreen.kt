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
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.dracula.socialnetworktwitch.R
import com.dracula.socialnetworktwitch.core.presentation.Semantics
import com.dracula.socialnetworktwitch.core.presentation.components.StandardTopBar
import com.dracula.socialnetworktwitch.core.presentation.utils.Screens
import com.dracula.socialnetworktwitch.feature_post.presentation.components.PostItem
import kotlinx.coroutines.launch

@Composable
fun MainFeedScreen(
    navController: NavController,
    scaffoldState: ScaffoldState,
    viewModel: MainFeedViewModel = hiltViewModel()
) {
    val posts = viewModel.posts.collectAsLazyPagingItems()
    val (isLoadingFirstTime, isLoadingNewPost, page) = viewModel.state
    val scope = rememberCoroutineScope()

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
            if (posts.itemCount == 0) {
                Text(
                    text = stringResource(id = R.string.msg_no_posts_to_display),
                    modifier = Modifier.align(Center),
                    style = MaterialTheme.typography.h2
                )
            } else {
                LazyColumn {
                    items(posts) { post ->
                        post?.let {
                            PostItem(
                                post = post,
                                onPostClicked = { navController.navigate(Screens.PostDetailsScreen.route) })
                        }

                    }
                    item {
                        if (isLoadingNewPost) CircularProgressIndicator(
                            modifier = Modifier.align(
                                BottomCenter
                            )
                        )
                    }

                    posts.apply {
                        when {
                            loadState.refresh is LoadState.Loading -> viewModel.onEvent(
                                MainFeedEvent.LoadedPage
                            )

                            loadState.append is LoadState.Loading -> viewModel.onEvent(MainFeedEvent.LoadMorePosts)
                            loadState.append is LoadState.NotLoading -> viewModel.onEvent(
                                MainFeedEvent.LoadedPage
                            )

                            loadState.append is LoadState.Error -> scope.launch {
                                scaffoldState.snackbarHostState.showSnackbar(
                                    message = "Error",
                                    duration = SnackbarDuration.Long
                                )
                            }
                        }
                    }


                }

            }
            if (isLoadingFirstTime) CircularProgressIndicator(modifier = Modifier.align(Center))
        }

    }

}