package com.dracula.socialnetworktwitch.feature_chat.presentation.message

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dracula.socialnetworktwitch.R
import com.dracula.socialnetworktwitch.core.presentation.components.SendTextField
import com.dracula.socialnetworktwitch.core.presentation.components.StandardAsyncImage
import com.dracula.socialnetworktwitch.core.presentation.components.StandardTopBar
import com.dracula.socialnetworktwitch.core.presentation.theme.SpaceMedium
import kotlinx.coroutines.flow.collectLatest
import okio.ByteString.Companion.decodeBase64
import java.nio.charset.Charset

@OptIn(ExperimentalComposeUiApi::class, ExperimentalAnimationApi::class)
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
    val scrollState = rememberLazyListState()
    val keyboardController = LocalSoftwareKeyboardController.current
    LaunchedEffect(key1 = pagingState) {
        viewModel.messageReceived.collectLatest {
            when (it) {
                MessageScreenEvent.AllMessagesLoaded -> if (pagingState.items.isNotEmpty()) scrollState.animateScrollToItem(
                    index = pagingState.items.size - 1
                )

                MessageScreenEvent.MessageSent -> keyboardController?.hide()
                MessageScreenEvent.NewMessageReceived -> {
                    if (pagingState.items.isNotEmpty() && pagingState.items.size > 1)
                        scrollState.animateScrollToItem(index = pagingState.items.size - 1)
                }
            }

        }
    }
    Column(
        Modifier.fillMaxSize()
    ) {
        StandardTopBar(
            title = {
                StandardAsyncImage(
                    url = remoteUserProfilePic, modifier = Modifier
                        .size(30.dp)
                        .clip(
                            CircleShape
                        )
                )
                Spacer(modifier = Modifier.width(SpaceMedium))
                Text(text = remoteUserName, style = MaterialTheme.typography.body1)
            },
            navController = navController,
            showBackButton = true
        )
        Column {
            AnimatedContent(targetState = pagingState.isLoading, label = "") { isLoading ->
                when (isLoading) {
                    true -> CircularProgressIndicator(modifier = Modifier.weight(1f))
                    else -> LazyColumn(
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(SpaceMedium),
                        verticalArrangement = Arrangement.spacedBy(SpaceMedium),
                        state = scrollState
                    ) {
                        items(pagingState.items.size) { index ->
                            val message = pagingState.items[index]
                            if (index >= pagingState.items.size - 1 && !pagingState.endReached && !pagingState.isLoading) {
                                viewModel.onEvent(MessageEvent.GetMessagesForChat)
                            }

                            if (message.fromId == remoteUserId) {
                                RemoteMessageItem(
                                    message = message.text,
                                    formattedTime = message.formattedTime
                                )
                            } else {
                                OwnMessageItem(
                                    message = message.text,
                                    formattedTime = message.formattedTime
                                )

                            }
                        }

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