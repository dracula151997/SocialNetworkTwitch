package com.dracula.socialnetworktwitch.presentation.ui.main_feed

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.dracula.socialnetworktwitch.R
import com.dracula.socialnetworktwitch.domain.model.Post
import com.dracula.socialnetworktwitch.presentation.components.StandardTopBar
import com.dracula.socialnetworktwitch.presentation.ui.Semantics
import com.dracula.socialnetworktwitch.presentation.ui.post.PostItem
import com.dracula.socialnetworktwitch.presentation.ui.utils.Screens

@Composable
fun MainFeedScreen(
    navController: NavController
) {
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
        PostItem(
            post = Post.dummy(),
            onPostClicked = { navController.navigate(Screens.PostDetailsScreen.route) })
    }

}