package com.dracula.socialnetworktwitch.feature_post.presentation.person_list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.PersonRemove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.dracula.socialnetworktwitch.R
import com.dracula.socialnetworktwitch.core.presentation.components.StandardTopBar
import com.dracula.socialnetworktwitch.core.presentation.components.UserProfileItem
import com.dracula.socialnetworktwitch.core.presentation.theme.IconSizeMedium
import com.dracula.socialnetworktwitch.core.presentation.theme.SpaceLarge
import com.dracula.socialnetworktwitch.core.presentation.theme.SpaceMedium
import com.dracula.socialnetworktwitch.core.presentation.utils.CommonUiEffect
import com.dracula.socialnetworktwitch.core.presentation.utils.Screens
import kotlinx.coroutines.flow.collectLatest

@Composable
fun PersonListRoute(
    showSnackbar: (message: String) -> Unit,
    onNavigate: (String) -> Unit,
    onNavUp: () -> Unit
) {
    val viewModel: PersonListViewModel = hiltViewModel()
    val context = LocalContext.current
    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is CommonUiEffect.ShowSnackbar -> showSnackbar(event.uiText.asString(context))
                else -> Unit
            }
        }

    }
    PersonListScreen(
        state = viewModel.viewState,
        ownUserId = viewModel.ownUserId,
        onEvent = viewModel::onEvent,
        onNavUp = onNavUp,
        onNavigate = onNavigate
    )

}

@Composable
private fun PersonListScreen(
    state: PersonListState,
    ownUserId: String?,
    onEvent: (event: PersonListEvent) -> Unit,
    onNavigate: (String) -> Unit = {},
    onNavUp: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        StandardTopBar(
            showBackButton = true,
            title = stringResource(id = R.string.liked_by),
            onBack = onNavUp
        )
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            if (state.hasNoUsers) {
                Text(
                    text = stringResource(id = R.string.no_people_liked),
                    modifier = Modifier.align(Alignment.Center),
                    style = MaterialTheme.typography.h2
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(SpaceLarge)
                ) {
                    items(state.users) { user ->
                        UserProfileItem(
                            user = user,
                            actionIcon = {
                                Icon(
                                    imageVector = if (user.isFollowing) {
                                        Icons.Default.PersonRemove
                                    } else Icons.Default.PersonAdd,
                                    contentDescription = null,
                                    tint = MaterialTheme.colors.onBackground,
                                    modifier = Modifier.size(IconSizeMedium)
                                )
                            },
                            onItemClick = {
                                onNavigate(Screens.ProfileScreen.createRoute(userId = user.userId))
                            },
                            onActionItemClick = {
                                onEvent(PersonListEvent.ToggleFollowStateForUser(user.userId))
                            },
                            ownUserId = ownUserId
                        )
                        Spacer(modifier = Modifier.height(SpaceMedium))
                    }
                }
            }
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}