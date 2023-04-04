package com.dracula.socialnetworktwitch.presentation.ui.mainfeed

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.dracula.socialnetworktwitch.domain.model.Post
import com.dracula.socialnetworktwitch.presentation.ui.post.PostListItem

@Composable
fun MainFeedScreen(
    navController: NavController
) {
    PostListItem(
        post = Post.dummy()
    )

}