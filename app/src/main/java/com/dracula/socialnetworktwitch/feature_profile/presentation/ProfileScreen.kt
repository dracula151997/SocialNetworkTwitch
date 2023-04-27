package com.dracula.socialnetworktwitch.presentation.ui.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.dracula.socialnetworktwitch.R
import com.dracula.socialnetworktwitch.core.domain.model.Post
import com.dracula.socialnetworktwitch.feature_profile.domain.User
import com.dracula.socialnetworktwitch.core.presentation.components.StandardTopBar
import com.dracula.socialnetworktwitch.core.presentation.Semantics
import com.dracula.socialnetworktwitch.feature_post.presentation.components.PostItem
import com.dracula.socialnetworktwitch.feature_profile.presentation.components.BannerSection
import com.dracula.socialnetworktwitch.feature_profile.presentation.components.ProfileHeaderSection
import com.dracula.socialnetworktwitch.core.presentation.theme.ProfilePictureSizeLarge
import com.dracula.socialnetworktwitch.core.presentation.theme.SpaceMedium
import com.dracula.socialnetworktwitch.core.presentation.utils.Screens

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
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = Semantics.ContentDescriptions.MORE,
                        tint = Color.White
                    )
                }
            },
            navController = navController
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