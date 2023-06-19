package com.dracula.socialnetworktwitch.feature_search.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.PersonRemove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import com.dracula.socialnetworktwitch.R
import com.dracula.socialnetworktwitch.core.presentation.components.PullToRefreshBox
import com.dracula.socialnetworktwitch.core.presentation.components.StandardTextField
import com.dracula.socialnetworktwitch.core.presentation.components.StandardTopBar
import com.dracula.socialnetworktwitch.core.presentation.components.UserProfileItem
import com.dracula.socialnetworktwitch.core.presentation.theme.IconSizeMedium
import com.dracula.socialnetworktwitch.core.presentation.theme.PaddingLarge
import com.dracula.socialnetworktwitch.core.presentation.theme.SpaceLarge
import com.dracula.socialnetworktwitch.core.presentation.theme.SpaceMedium
import com.dracula.socialnetworktwitch.core.presentation.theme.appFontFamily
import com.dracula.socialnetworktwitch.core.presentation.utils.Screens
import com.dracula.socialnetworktwitch.core.presentation.utils.states.validator.TextFieldState
import com.dracula.socialnetworktwitch.core.utils.ErrorState
import com.dracula.socialnetworktwitch.core.utils.LoadingState
import com.dracula.socialnetworktwitch.core.utils.UiEvent
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SearchRoute(
    showSnackbar: (message: String) -> Unit,
    onNavUp: () -> Unit,
    onNavigate: (route: String) -> Unit
) {
    val viewModel: SearchViewModel = hiltViewModel()
    val context = LocalContext.current
    LaunchedEffect(key1 = true) {
        viewModel.event.collectLatest { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> showSnackbar(event.uiText.asString(context))
                else -> Unit
            }
        }
    }
    SearchScreen(
        onNavUp = onNavUp,
        onNavigate = onNavigate,
        onEvent = viewModel::onEvent,
        state = viewModel.state,
        searchFieldState = viewModel.searchFieldState
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun SearchScreen(
    state: SearchState,
    searchFieldState: TextFieldState,
    onEvent: (event: SearchAction) -> Unit,
    onNavUp: () -> Unit,
    onNavigate: (route: String) -> Unit
) {
    val pullRefreshState = rememberPullRefreshState(refreshing = state.refreshing,
        onRefresh = { onEvent(SearchAction.Refreshing) })


    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        StandardTopBar(
            title = stringResource(id = R.string.search_for_users),
            showBackButton = true,
            onBack = onNavUp
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(PaddingLarge)
        ) {
            StandardTextField(
                state = searchFieldState,
                hint = stringResource(id = R.string.search),
                leadingIcon = Icons.Default.Search,
                doOnValueChanged = {
                    onEvent(SearchAction.OnSearch(it))
                },
            )
            Spacer(modifier = Modifier.height(SpaceLarge))
            PullToRefreshBox(
                state = pullRefreshState, refreshing = state.refreshing
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    when {
                        state.isLoading -> item {
                            LoadingState(
                                modifier = Modifier.fillParentMaxSize(), contentAlignment = Center
                            )
                        }

                        state.userItems == null -> item {
                            ErrorState(
                                errorMessage = stringResource(id = R.string.msg_try_to_enter_the_username),
                                textStyle = MaterialTheme.typography.h6.copy(
                                    fontFamily = appFontFamily
                                ),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillParentMaxSize(),
                                contentAlignment = Center
                            )
                        }

                        state.userItems.isEmpty() -> item {
                            ErrorState(
                                errorMessage = stringResource(id = R.string.msg_no_username_found),
                                textStyle = MaterialTheme.typography.h6.copy(
                                    fontFamily = appFontFamily
                                ),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillParentMaxSize(),
                                contentAlignment = Center
                            )
                        }

                        else -> items(state.userItems) { user ->
                            UserProfileItem(
                                user = user,
                                actionIcon = {
                                    IconButton(
                                        onClick = {
                                            onEvent(SearchAction.ToggleFollowState(userId = user.userId))
                                        },
                                    ) {
                                        Icon(
                                            imageVector = if (user.isFollowing) Icons.Default.PersonRemove else Icons.Default.PersonAdd,
                                            contentDescription = null,
                                            tint = MaterialTheme.colors.onBackground,
                                            modifier = Modifier.size(IconSizeMedium),
                                        )

                                    }

                                },
                                onItemClick = {
                                    onNavigate(Screens.ProfileScreen.createRoute(userId = user.userId))
                                },
                            )
                            Spacer(modifier = Modifier.height(SpaceMedium))
                        }

                    }
                }

            }
        }
    }
}