package com.dracula.socialnetworktwitch.feature_chat.presentation.chat

import android.util.Base64
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import com.dracula.socialnetworktwitch.R
import com.dracula.socialnetworktwitch.core.presentation.components.PullToRefreshBox
import com.dracula.socialnetworktwitch.core.presentation.components.StandardTopBar
import com.dracula.socialnetworktwitch.core.presentation.theme.PaddingMedium
import com.dracula.socialnetworktwitch.core.presentation.theme.SpaceMedium
import com.dracula.socialnetworktwitch.core.presentation.theme.appFontFamily
import com.dracula.socialnetworktwitch.core.presentation.utils.Screens
import com.dracula.socialnetworktwitch.core.utils.UiEvent
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ChatRoute(
    onNavigate: (route: String) -> Unit,
    onNavUp: () -> Unit,
    showSnackbar: (message: String) -> Unit,
) {
    val viewModel: ChatViewModel = hiltViewModel()
    ChatScreen(
        onNavigate = onNavigate,
        onNavUp = onNavUp,
        showSnackbar = showSnackbar,
        viewModel = viewModel
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ChatScreen(
    showSnackbar: (message: String) -> Unit,
    onNavigate: (route: String) -> Unit,
    onNavUp: () -> Unit,
    viewModel: ChatViewModel
) {
    val state = viewModel.state
    val context = LocalContext.current
    val pullToRefreshState = rememberPullRefreshState(
        refreshing = state.refreshing,
        onRefresh = { viewModel.onEvent(ChatScreenAction.Refreshing) })
    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> showSnackbar(event.uiText.asString(context))
                is UiEvent.Navigate -> onNavigate(event.route)
                UiEvent.NavigateUp -> onNavUp()

            }
        }
    }
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        StandardTopBar(title = stringResource(id = R.string.chats))
        PullToRefreshBox(
            state = pullToRefreshState,
            refreshing = state.refreshing,
            modifier = Modifier
                .fillMaxSize()
                .pullRefresh(state = pullToRefreshState)
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(SpaceMedium),
                contentPadding = PaddingValues(
                    horizontal = PaddingMedium,
                    vertical = PaddingMedium
                ),
                modifier = Modifier.fillMaxSize()
            ) {
                when {
                    state.isLoading -> item {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                    state.chats.isEmpty() -> item {
                        Box(
                            modifier = Modifier.fillParentMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(id = R.string.msg_no_chats_to_display),
                                style = MaterialTheme.typography.h6.copy(
                                    fontFamily = appFontFamily
                                ),
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    else -> items(state.chats) { chat ->
                        ChatItem(
                            chat = chat,
                            onItemClick = { chat ->
                                onNavigate(
                                    Screens.MessageScreen.createRoute(
                                        remoteUserId = chat.remoteUserId,
                                        chatId = chat.chatId,
                                        remoteUserName = chat.remoteUsername,
                                        remoteUserProfilePic = Base64.encodeToString(
                                            chat.remoteUserProfilePictureUrl.encodeToByteArray(),
                                            0
                                        )
                                    )
                                )
                            },
                        )
                    }

                }
            }
        }


    }
}