package com.dracula.socialnetworktwitch.presentation.ui.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.More
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.dracula.socialnetworktwitch.R
import com.dracula.socialnetworktwitch.domain.model.Post
import com.dracula.socialnetworktwitch.domain.model.User
import com.dracula.socialnetworktwitch.presentation.components.StandardTopBar
import com.dracula.socialnetworktwitch.presentation.ui.Semantics
import com.dracula.socialnetworktwitch.presentation.ui.post.PostItem
import com.dracula.socialnetworktwitch.presentation.ui.profile.components.BannerSection
import com.dracula.socialnetworktwitch.presentation.ui.profile.components.ProfileHeaderSection
import com.dracula.socialnetworktwitch.presentation.ui.theme.ProfilePictureSizeLarge
import com.dracula.socialnetworktwitch.presentation.ui.theme.SpaceMedium
import com.dracula.socialnetworktwitch.presentation.ui.utils.Screens

@Composable
fun ProfileScreen(navController: NavController) {
    Column(modifier = Modifier.fillMaxSize()) {
        StandardTopBar(
            title = stringResource(id = R.string.your_profile),
            navActions = {
                IconButton(
                    onClick = {
                    },
                ) {
                    Icon(
                        imageVector = Icons.Default.More,
                        contentDescription = Semantics.ContentDescriptions.MORE,
                    )
                }
            },
        )
        LazyColumn(
            Modifier.fillMaxSize()
        ) {
            item {
                BannerSection(
                    modifier = Modifier.aspectRatio(2.5f)
                )
                ProfileHeaderSection(
                    user = User.dummy(),
                    modifier = Modifier.padding(SpaceMedium),
                    onEditClick = {})
            }

            items(5) {
                PostItem(
                    post = Post.dummy(),
                    onPostClicked = {
                        navController.navigate(Screens.PostDetailsScreen.route)
                    },
                    showProfileImage = false,
                    modifier = Modifier.offset(y = -ProfilePictureSizeLarge / 2f)
                )
            }
        }
    }

}