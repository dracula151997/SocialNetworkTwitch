package com.dracula.socialnetworktwitch.feature_profile.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.dracula.socialnetworktwitch.core.domain.model.User
import com.dracula.socialnetworktwitch.core.presentation.Semantics
import com.dracula.socialnetworktwitch.core.presentation.components.StandardTopBar
import com.dracula.socialnetworktwitch.core.presentation.theme.ProfilePictureSizeLarge
import com.dracula.socialnetworktwitch.core.presentation.theme.SpaceMedium
import com.dracula.socialnetworktwitch.core.presentation.utils.Screens
import com.dracula.socialnetworktwitch.core.utils.UiEvent
import com.dracula.socialnetworktwitch.feature_post.presentation.components.PostItem
import com.dracula.socialnetworktwitch.feature_profile.domain.model.Profile
import com.dracula.socialnetworktwitch.feature_profile.profile.components.BannerSection
import com.dracula.socialnetworktwitch.feature_profile.profile.components.ProfileHeaderSection
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ProfileScreen(
    navController: NavController,
    scaffoldState: ScaffoldState,
    userId: String? = null,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val userPosts = viewModel.userPosts.collectAsLazyPagingItems()
    val (userId1, username, bio, followerCount, followingCount, postCount, profilePictureUrl, bannerUrl, topSkills, gitHubUrl, instagramUrl, linkedinUrl, isOwnProfile, isFollowing) = state.data
        ?: Profile.empty()
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.onEvent(ProfileEvent.GetProfile(userId))
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is UiEvent.SnackbarEvent -> scaffoldState.snackbarHostState.showSnackbar(
                    message = event.uiText.asString(
                        context
                    )
                )

                else -> Unit
            }

        }
    }
    Column(modifier = Modifier.fillMaxSize()) {
        StandardTopBar(
            title = username,
            navActions = {
                IconButton(
                    onClick = {},
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
        if (state.isLoading) CircularProgressIndicator(
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        LazyColumn(
            Modifier.fillMaxSize()
        ) {
            item {
                BannerSection(
                    modifier = Modifier.aspectRatio(2.5f),
                    topSkillUrls = topSkills,
                    githubUrl = gitHubUrl,
                    instagramUrl = instagramUrl,
                    linkedinUrl = linkedinUrl,
                    bannerUrl = bannerUrl

                )
                ProfileHeaderSection(user = User(
                    userId = userId1,
                    profilePictureUrl = profilePictureUrl,
                    username = username,
                    bio = bio,
                    followingCount = followingCount,
                    followerCount = followerCount,
                    postCount = postCount,
                ),
                    isOwnProfile = isOwnProfile,
                    isFollowing = isFollowing,
                    modifier = Modifier.padding(SpaceMedium),
                    onEditClick = {
                        navController.navigate(Screens.EditProfileScreen.createRoute(userId = userId))
                    })
            }

            items(userPosts) { post ->
                post?.let {
                    PostItem(
                        post = it,
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

}