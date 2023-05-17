package com.dracula.socialnetworktwitch.feature_profile.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.AlertDialog
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.dracula.socialnetworktwitch.R
import com.dracula.socialnetworktwitch.core.domain.model.User
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
import java.util.Locale

@Composable
fun ProfileScreen(
    navController: NavController,
    scaffoldState: ScaffoldState,
    userId: String? = null,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val userPosts = viewModel.postsPagingState
    val (userId1, username, bio, followerCount, followingCount, postCount, profilePictureUrl, bannerUrl, topSkills, gitHubUrl, instagramUrl, linkedinUrl, isOwnProfile, isFollowing) = state.data
        ?: Profile.empty()
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.onEvent(ProfileScreenAction.GetProfile(userId))
        viewModel.eventFlow.collectLatest { event ->
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
    Column(modifier = Modifier.fillMaxSize()) {
        StandardTopBar(
            title = stringResource(id = R.string.x_profile,
                username.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ENGLISH) else it.toString() }),
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
                    },
                    onLogoutClicked = {
                        viewModel.onEvent(ProfileScreenAction.ShowLogoutDialog)
                    })
            }

            items(userPosts.items.size) { index ->
                val post = userPosts.items[index]
                if (index > userPosts.items.size - 1 && !userPosts.endReached && !userPosts.isLoading) {
                    viewModel.loadNextPost()
                }
                PostItem(post = post,
                    onPostClicked = {
                        navController.navigate(Screens.PostDetailsScreen.createRoute(postId = post.id))
                    },
                    showProfileImage = false,
                    modifier = Modifier.offset(y = -ProfilePictureSizeLarge / 2f),
                    onCommentClicked = {

                    },
                    onLikeClicked = {
                        viewModel.onEvent(ProfileScreenAction.LikePost(post.id))
                    },
                    onShareClicked = {},
                    onUsernameClicked = {})
            }


        }

        if (state.showLogoutDialog) {
            AlertDialog(onDismissRequest = { viewModel.onEvent(ProfileScreenAction.HideLogoutDialog) },
                title = {
                    Text(text = stringResource(id = R.string.logout))
                },
                text = {
                    Text(text = stringResource(id = R.string.are_you_sure_to_logout))
                },
                buttons = {
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        TextButton(
                            onClick = {
                                viewModel.onEvent(ProfileScreenAction.HideLogoutDialog)
                            },
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = MaterialTheme.colors.primary
                            )
                        ) {
                            Text(text = stringResource(id = R.string.dismiss_btn))
                        }

                        TextButton(onClick = {
                            viewModel.onEvent(ProfileScreenAction.Logout)
                            navController.navigate(Screens.LoginScreen.route) {
                                popUpTo(Screens.LoginScreen.route) {
                                    inclusive = true
                                }
                            }
                        }) {
                            Text(text = stringResource(id = R.string.logout_btn))
                        }
                    }


                })
        }

    }

}