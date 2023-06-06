package com.dracula.socialnetworktwitch.feature_chat.presentation.message

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dracula.socialnetworktwitch.R
import com.dracula.socialnetworktwitch.core.presentation.components.SendTextField
import com.dracula.socialnetworktwitch.core.presentation.components.StandardAsyncImage
import com.dracula.socialnetworktwitch.core.presentation.components.StandardTopBar
import com.dracula.socialnetworktwitch.core.presentation.theme.SpaceMedium
import okio.ByteString.Companion.decodeBase64
import java.nio.charset.Charset

@Composable
fun MessagesScreen(
    navController: NavController,
    remoteUserId: String,
    remoteUserName: String,
    encodedRemoteUserProfilePic: String? = null,
    viewModel: MessageViewModel = hiltViewModel(),
) {
    val remoteUserProfilePic = remember {
        encodedRemoteUserProfilePic?.decodeBase64()?.string(charset = Charset.defaultCharset())
    }
    val messageState = viewModel.messageFieldState
    val pagingState = viewModel.pagingState
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
        Column(
            Modifier.fillMaxSize()
        ) {
            StandardTopBar(
                title = {
                    StandardAsyncImage(url = remoteUserProfilePic, modifier = Modifier.size(30.dp))
                    Spacer(modifier = Modifier.width(SpaceMedium))
                    Text(text = remoteUserName)
                },
                navController = navController
            )
            Column(modifier = Modifier) {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(SpaceMedium),
                    verticalArrangement = Arrangement.spacedBy(SpaceMedium)
                ) {
                    items(pagingState.items.size) { index ->
                        val message = pagingState.items[index]
                        if (index >= pagingState.items.size - 1
                            && !pagingState.endReached
                            && !pagingState.isLoading
                        ) {
                            viewModel.onEvent(MessageEvent.GetMessagesForChat)
                        }
                        if (message.fromId == remoteUserId) {
                            RemoteMessageItem(
                                message = message.text,
                                formattedTime = message.formattedTime
                            )
                            Spacer(modifier = Modifier.height(SpaceMedium))
                        } else {
                            OwnMessageItem(
                                message = message.text,
                                formattedTime = message.formattedTime
                            )

                        }
                    }

                }
                SendTextField(
                    text = messageState.text,
                    hint = stringResource(id = R.string.enter_your_message),
                    onValueChange = {
                        viewModel.onEvent(MessageEvent.EnteredMessage(it))
                    },
                    onSend = { viewModel.onEvent(MessageEvent.SendMessage) },
                    enabled = messageState.hasText && !messageState.hasError,
                )

            }
        }

    }
}