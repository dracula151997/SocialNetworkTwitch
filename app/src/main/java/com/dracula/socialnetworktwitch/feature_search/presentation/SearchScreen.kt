package com.dracula.socialnetworktwitch.feature_search.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.PersonRemove
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dracula.socialnetworktwitch.R
import com.dracula.socialnetworktwitch.core.domain.model.User
import com.dracula.socialnetworktwitch.core.presentation.components.StandardTextField
import com.dracula.socialnetworktwitch.core.presentation.components.StandardTopBar
import com.dracula.socialnetworktwitch.core.presentation.components.UserProfileItem
import com.dracula.socialnetworktwitch.core.presentation.theme.IconSizeMedium
import com.dracula.socialnetworktwitch.core.presentation.theme.PaddingLarge
import com.dracula.socialnetworktwitch.core.presentation.theme.SpaceLarge
import com.dracula.socialnetworktwitch.core.presentation.theme.SpaceMedium
import com.dracula.socialnetworktwitch.core.presentation.utils.Screens
import com.dracula.socialnetworktwitch.core.utils.UiEvent
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SearchScreen(
    navController: NavController,
    scaffoldState: ScaffoldState,
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val state = viewModel.state
    val searchFieldState = viewModel.searchFieldState
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.event.collectLatest { event ->
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
    Box(modifier = modifier) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            StandardTopBar(
                title = stringResource(id = R.string.search_for_users),
                showBackButton = true,
                navController = navController
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(PaddingLarge)
            ) {
                StandardTextField(text = searchFieldState.text,
                    hint = stringResource(id = R.string.search),
                    leadingIcon = Icons.Default.Search,
                    onValueChanged = {
                        viewModel.onEvent(SearchEvent.OnSearch(it))
                    })
                Spacer(modifier = Modifier.height(SpaceLarge))
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(state.userItems) { user ->
                        UserProfileItem(user = User(
                            userId = user.userId,
                            profilePictureUrl = user.profilePictureUrl,
                            username = user.username,
                            bio = user.bio,
                            followerCount = 0,
                            followingCount = 0,
                            postCount = 0
                        ), actionIcon = {
                            IconButton(onClick = {
                                viewModel.onEvent(SearchEvent.ToggleFollowState(userId = user.userId))
                            }) {
                                Icon(
                                    imageVector = if (user.isFollowing) Icons.Default.PersonRemove else Icons.Default.PersonAdd,
                                    contentDescription = null,
                                    tint = MaterialTheme.colors.onBackground,
                                    modifier = Modifier.size(IconSizeMedium)
                                )

                            }

                        }, onItemClick = {
                            navController.navigate(Screens.ProfileScreen.createRoute(userId = user.userId))
                        })
                        Spacer(modifier = Modifier.height(SpaceMedium))
                    }
                }
            }
        }
        if (state.isLoading) CircularProgressIndicator(
            modifier = Modifier.align(Alignment.Center)
        )
    }
}