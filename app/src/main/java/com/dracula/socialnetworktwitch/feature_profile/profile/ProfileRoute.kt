package com.dracula.socialnetworktwitch.feature_profile.profile

import android.util.Base64
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.dracula.socialnetworktwitch.R
import com.dracula.socialnetworktwitch.core.domain.model.Post
import com.dracula.socialnetworktwitch.core.domain.model.User
import com.dracula.socialnetworktwitch.core.presentation.components.PullToRefreshBox
import com.dracula.socialnetworktwitch.core.presentation.components.StandardAlertDialog
import com.dracula.socialnetworktwitch.core.presentation.components.StandardTopBar
import com.dracula.socialnetworktwitch.core.presentation.theme.ProfilePictureSizeLarge
import com.dracula.socialnetworktwitch.core.presentation.theme.SpaceMedium
import com.dracula.socialnetworktwitch.core.presentation.utils.CommonUiEffect
import com.dracula.socialnetworktwitch.core.presentation.utils.Screens
import com.dracula.socialnetworktwitch.core.utils.LoadingState
import com.dracula.socialnetworktwitch.core.utils.PagingState
import com.dracula.socialnetworktwitch.core.utils.openUrlInBrowser
import com.dracula.socialnetworktwitch.core.utils.sendSharePostIntent
import com.dracula.socialnetworktwitch.feature_post.presentation.components.PostItem
import com.dracula.socialnetworktwitch.feature_profile.domain.model.Profile
import com.dracula.socialnetworktwitch.feature_profile.profile.components.BannerSection
import com.dracula.socialnetworktwitch.feature_profile.profile.components.ProfileHeaderSection
import kotlinx.coroutines.flow.collectLatest
import java.util.Locale

@Composable
fun ProfileRoute(
    userId: String?,
    modifier: Modifier = Modifier,
    showSnackbar: (message: String) -> Unit,
    onNavigate: (route: String) -> Unit,
    navigateToLogin: () -> Unit,
) {
    val viewModel: ProfileViewModel = hiltViewModel()
    val context = LocalContext.current
    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is CommonUiEffect.ShowSnackbar -> showSnackbar(event.uiText.asString(context))
                is CommonUiEffect.Navigate -> onNavigate(event.route)
                else -> Unit
            }

        }
    }
    ProfileScreen(
        modifier = modifier,
        userId = userId,
        onNavigate = onNavigate,
        navigateToLogin = navigateToLogin,
        state = viewModel.viewState,
        onEvent = viewModel::onEvent,
        userPosts = viewModel.viewState.pagingState
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ProfileScreen(
    state: ProfileState,
    userPosts: PagingState<Post>,
    userId: String?,
    modifier: Modifier = Modifier,
    onEvent: (event: ProfileScreenEvent) -> Unit,
    onNavigate: (route: String) -> Unit,
    navigateToLogin: () -> Unit,
) {
    val profile = state.data ?: Profile.empty()
    val context = LocalContext.current
    var showPopupMenu by remember {
        mutableStateOf(false)
    }
    val pullToRefreshState =
        rememberPullRefreshState(
            refreshing = state.refreshing,
            onRefresh = { onEvent(ProfileScreenEvent.Refreshing(userId = userId)) })


    Column(modifier = modifier.fillMaxSize()) {
        StandardTopBar(
            title = stringResource(
                id = R.string.x_profile,
                profile.username.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ENGLISH) else it.toString() },
            ),
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
                                onNavigate(
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
                            onEvent(ProfileScreenEvent.ShowLogoutDialog)
                            showPopupMenu = false
                        }) {
                            Text(text = stringResource(id = R.string.logout))
                        }
                    } else {
                        DropdownMenuItem(
                            onClick = {
                                onNavigate(
                                    Screens.MessageScreen.createRoute(
                                        chatId = null,
                                        remoteUserName = profile.username,
                                        remoteUserProfilePic = Base64.encodeToString(
                                            profile.profilePictureUrl.encodeToByteArray(), 0,
                                        ),
                                        remoteUserId = profile.userId,

                                        )
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
        PullToRefreshBox(
            state = pullToRefreshState,
            refreshing = state.refreshing,
            modifier = modifier
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                when {
                    state.isLoading -> item {
                        LoadingState(
                            modifier = Modifier.fillParentMaxSize(),
                            contentAlignment = Alignment.Center
                        )
                    }

                    state.data != null -> item {
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
                                onEvent(
                                    ProfileScreenEvent.ToggleFollowStateForUser(
                                        profile.userId,
                                    ),
                                )
                            }
                        )
                    }

                }
                itemsIndexed(userPosts.items) { index, post ->
                    if (index > userPosts.lastIndex && !userPosts.endReached && !userPosts.isLoading) {
                        onEvent(ProfileScreenEvent.LoadNextPosts)
                    }
                    PostItem(
                        post = post,
                        onPostClicked = {
                            onNavigate(Screens.PostDetailsScreen.createRoute(postId = it.id))
                        },
                        showProfileImage = false,
                        modifier = Modifier.offset(y = -ProfilePictureSizeLarge / 2f),
                        onCommentClicked = {
                            onNavigate(
                                Screens.PostDetailsScreen.createRoute(
                                    postId = post.id, showKeyboard = true,
                                ),
                            )
                        },
                        onLikeClicked = {
                            onEvent(ProfileScreenEvent.LikePost(post.id))
                        },
                        onShareClicked = {
                            context.sendSharePostIntent(postId = post.id)
                        },
                        onUsernameClicked = {

                        },
                        onDeleteClicked = { postId ->
                            onEvent(ProfileScreenEvent.DeletePost(postId))
                        },
                    )
                }

            }
        }

        if (state.showLogoutDialog) {
            StandardAlertDialog(
                message = stringResource(id = R.string.are_you_sure_to_logout),
                positiveButtonText = stringResource(id = R.string.logout_btn),
                negativeButtonText = stringResource(id = R.string.dismiss_btn),
                title = stringResource(id = R.string.logout),
                onPositiveButtonClicked = {
                    onEvent(ProfileScreenEvent.Logout)
                    navigateToLogin()
                },
                onNegativeButtonClicked = {
                    onEvent(ProfileScreenEvent.HideLogoutDialog)
                },
                onDismissed = { onEvent(ProfileScreenEvent.HideLogoutDialog) }
            )
        }

    }

}
