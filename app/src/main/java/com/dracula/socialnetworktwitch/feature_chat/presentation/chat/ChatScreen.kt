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
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dracula.socialnetworktwitch.R
import com.dracula.socialnetworktwitch.core.presentation.components.StandardTopBar
import com.dracula.socialnetworktwitch.core.presentation.theme.PaddingMedium
import com.dracula.socialnetworktwitch.core.presentation.theme.SpaceMedium
import com.dracula.socialnetworktwitch.core.presentation.utils.Screens
import com.dracula.socialnetworktwitch.core.utils.UiEvent
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ChatScreen(
    navController: NavController,
    scaffoldState: ScaffoldState,
    viewModel: ChatViewModel = hiltViewModel()
) {
    val state = viewModel.state
    val context = LocalContext.current
    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> scaffoldState.snackbarHostState.showSnackbar(
                    event.uiText.asString(
                        context = context
                    )
                )

                else -> Unit
            }
        }
    }
    Box(
        contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            StandardTopBar(title = stringResource(id = R.string.chats), navController)
            Box {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(SpaceMedium),
                    contentPadding = PaddingValues(
                        horizontal = PaddingMedium,
                        vertical = PaddingMedium
                    ),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(state.chats) { chat ->
                        ChatItem(
                            chat = chat,
                            onItemClick = {
                                navController.navigate(
                                    Screens.MessageScreen.createRoute(
                                        remoteUserId = chat.remoteUserId,
                                        chatId = chat.chatId,
                                        remoteUserName = chat.remoteUsername,
                                        remoteUserProfilePic = Base64.encodeToString(
                                            chat.remoteUserProfilePicture.encodeToByteArray(),
                                            0
                                        )
                                    )
                                )
                            },
                        )
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
}