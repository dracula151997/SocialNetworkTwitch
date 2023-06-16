package com.dracula.socialnetworktwitch.feature_chat.presentation.message

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dracula.socialnetworktwitch.R
import com.dracula.socialnetworktwitch.core.presentation.components.SendTextField
import com.dracula.socialnetworktwitch.core.presentation.components.StandardAsyncImage
import com.dracula.socialnetworktwitch.core.presentation.components.StandardTopBar
import com.dracula.socialnetworktwitch.core.presentation.theme.SpaceMedium
import com.dracula.socialnetworktwitch.core.presentation.theme.appFontFamily
import kotlinx.coroutines.flow.collectLatest
import okio.ByteString.Companion.decodeBase64
import java.nio.charset.Charset

@Composable
fun MessageRoute(
    remoteUserId: String,
    remoteUsername: String,
    encodedRemoteUserProfilePic: String?,
    onNavUp: () -> Unit
) {
    val viewModel: MessageViewModel = hiltViewModel()
    MessagesScreen(
        viewModel = viewModel,
        remoteUserId = remoteUserId,
        remoteUserName = remoteUsername,
        onNavUp = onNavUp,
        encodedRemoteUserProfilePic = encodedRemoteUserProfilePic,
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun MessagesScreen(
    viewModel: MessageViewModel,
    remoteUserId: String,
    remoteUserName: String,
    encodedRemoteUserProfilePic: String? = null,
    onNavUp: () -> Unit,
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
                        ),
                    placeholder = painterResource(id = R.drawable.avatar_placeholder),
                    errorPlaceholder = painterResource(id = R.drawable.avatar_placeholder)
                )
                Spacer(modifier = Modifier.width(SpaceMedium))
                Text(text = remoteUserName, style = MaterialTheme.typography.body1)
            },
            showBackButton = true,
            onBack = onNavUp
        )
        Column {
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(SpaceMedium),
                verticalArrangement = Arrangement.spacedBy(SpaceMedium),
                state = scrollState
            ) {
                when {
                    pagingState.isLoading -> item {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillParentMaxSize()
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }

                    pagingState.items.isEmpty() -> item {
                        Box(
                            modifier = Modifier.fillParentMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(id = R.string.msg_no_messages_found),
                                style = MaterialTheme.typography.h6.copy(
                                    fontFamily = appFontFamily
                                ),
                                textAlign = TextAlign.Center
                            )
                        }

                    }

                    else -> items(pagingState.items.size) { index ->
                        val message = pagingState.items[index]
                        if (index >= pagingState.items.size - 1 && !pagingState.endReached) {
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