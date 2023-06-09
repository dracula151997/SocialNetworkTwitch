package com.dracula.socialnetworktwitch.feature_profile.profile

import android.util.Base64
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.dracula.socialnetworktwitch.R
import com.dracula.socialnetworktwitch.core.domain.model.User
import com.dracula.socialnetworktwitch.core.presentation.components.StandardAlertDialog
import com.dracula.socialnetworktwitch.core.presentation.components.StandardTopBar
import com.dracula.socialnetworktwitch.core.presentation.theme.ProfilePictureSizeLarge
import com.dracula.socialnetworktwitch.core.presentation.theme.SpaceMedium
import com.dracula.socialnetworktwitch.core.presentation.utils.Screens
import com.dracula.socialnetworktwitch.core.utils.UiEvent
import com.dracula.socialnetworktwitch.core.utils.openUrlInBrowser
import com.dracula.socialnetworktwitch.core.utils.sendSharePostIntent
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
    val profile = state.data ?: Profile.empty()
    val context = LocalContext.current
    var showPopupMenu by remember {
        mutableStateOf(false)
    }

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
            title = stringResource(
                id = R.string.x_profile,
                profile.username.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ENGLISH) else it.toString() },
            ),
            navController = navController,
            navActions = {
                IconButton(onClick = { showPopupMenu = !showPopupMenu }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = null,
                        tint = Color.White,

                        )
                }
                DropdownMenu(
                    expanded = showPopupMenu,
                    onDismissRequest = { showPopupMenu = !showPopupMenu },
                ) {
                    if (profile.isOwnProfile) {
                        DropdownMenuItem(
                            onClick = {
                                navController.navigate(
                                    Screens.EditProfileScreen.createRoute(
                                        userId = userId,
                                    ),
                                )
                                showPopupMenu = false
                            },
                        ) {
                            Text(text = stringResource(id = R.string.edit_your_profile))
                        }
                        DropdownMenuItem(onClick = {
                            viewModel.onEvent(ProfileScreenAction.ShowLogoutDialog)
                            showPopupMenu = false
                        }) {
                            Text(text = stringResource(id = R.string.logout))
                        }
                    } else {
                        DropdownMenuItem(
                            onClick = {
                                navController.navigate(
                                    Screens.MessageScreen.createRoute(
                                        chatId = null,
                                        remoteUserName = profile.username,
                                        remoteUserProfilePic = Base64.encodeToString(
                                            profile.profilePictureUrl.encodeToByteArray(), 0,
                                        ),
                                        remoteUserId = profile.userId,

                                        ),
                                )
                                showPopupMenu = false

                            },
                        ) {
                            Text(text = stringResource(id = R.string.send_a_message))
                        }
                    }
                }
            },
        )
        /*   if (state.isLoading) CircularProgressIndicator(
               modifier = Modifier.align(Alignment.CenterHorizontally)
           )*/
        LazyColumn(
            Modifier.fillMaxSize()
        ) {
            item {
                BannerSection(modifier = Modifier.aspectRatio(2.5f),
                    topSkillUrls = profile.topSkills,
                    githubUrl = profile.gitHubUrl,
                    instagramUrl = profile.instagramUrl,
                    linkedinUrl = profile.linkedinUrl,
                    bannerUrl = profile.bannerUrl,
                    onLinkClicked = { url ->
                        context.openUrlInBrowser(url)
                    }

                )
                ProfileHeaderSection(
                    user = User(
                        userId = profile.userId,
                        profilePictureUrl = profile.profilePictureUrl,
                        username = profile.username,
                        bio = profile.bio,
                        followingCount = profile.followingCount,
                        followerCount = profile.followerCount,
                        postCount = profile.postCount,
                    ),
                    modifier = Modifier.padding(SpaceMedium),
                    isOwnProfile = profile.isOwnProfile,
                    isFollowing = profile.isFollowing,
                    onFollowClicked = {
                        viewModel.onEvent(ProfileScreenAction.ToggleFollowStateForUser(profile.userId))
                    }
                )
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
                        navController.navigate(
                            Screens.PostDetailsScreen.createRoute(
                                postId = post.id, showKeyboard = true
                            )
                        )
                    },
                    onLikeClicked = {
                        viewModel.onEvent(ProfileScreenAction.LikePost(post.id))
                    },
                    onShareClicked = {
                        context.sendSharePostIntent(postId = post.id)
                    },
                    onUsernameClicked = {},
                    onDeleteClicked = { postId ->
                        viewModel.onEvent(ProfileScreenAction.DeletePost(postId))

                    })
            }
        }

        if (state.showLogoutDialog) {
            StandardAlertDialog(
                message = stringResource(id = R.string.are_you_sure_to_logout),
                positiveButtonText = stringResource(id = R.string.logout_btn),
                negativeButtonText = stringResource(id = R.string.dismiss_btn),
                title = stringResource(id = R.string.logout),
                onPositiveButtonClicked = {
                    viewModel.onEvent(ProfileScreenAction.Logout)
                    navController.navigate(Screens.LoginScreen.route) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                    }
                    navController.graph.clear()
                },
                onNegativeButtonClicked = {
                    viewModel.onEvent(ProfileScreenAction.HideLogoutDialog)
                },
                onDismissed = { viewModel.onEvent(ProfileScreenAction.HideLogoutDialog) }
            )
        }

    }

}
