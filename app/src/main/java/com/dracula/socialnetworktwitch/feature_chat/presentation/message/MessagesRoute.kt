package com.dracula.socialnetworktwitch.feature_chat.presentation.message

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
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
import com.dracula.socialnetworktwitch.core.presentation.utils.states.validator.TextFieldState
import com.dracula.socialnetworktwitch.core.utils.ErrorState
import com.dracula.socialnetworktwitch.core.utils.LoadingState
import com.dracula.socialnetworktwitch.core.utils.PagingState
import com.dracula.socialnetworktwitch.feature_chat.domain.model.Message
import kotlinx.coroutines.flow.collectLatest
import okio.ByteString.Companion.decodeBase64
import java.nio.charset.Charset

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MessageRoute(
    remoteUserId: String,
    remoteUsername: String,
    encodedRemoteUserProfilePic: String?,
    onNavUp: () -> Unit
) {
    val viewModel: MessageViewModel = hiltViewModel()
    val pagingState = viewModel.pagingState
    val keyboardController = LocalSoftwareKeyboardController.current
    val scrollState = rememberLazyListState()
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

    MessagesScreen(
        remoteUserId = remoteUserId,
        remoteUserName = remoteUsername,
        onNavUp = onNavUp,
        encodedRemoteUserProfilePic = encodedRemoteUserProfilePic,
        messageFieldState = viewModel.messageFieldState,
        onEvent = viewModel::onEvent,
        pagingState = viewModel.pagingState,
        scrollState = scrollState
    )
}

@Composable
private fun MessagesScreen(
    messageFieldState: TextFieldState,
    scrollState: LazyListState,
    pagingState: PagingState<Message>,
    remoteUserId: String,
    remoteUserName: String,
    encodedRemoteUserProfilePic: String? = null,
    onEvent: (event: MessageScreenAction) -> Unit,
    onNavUp: () -> Unit,
) {
    val remoteUserProfilePic = remember {
        encodedRemoteUserProfilePic?.decodeBase64()?.string(charset = Charset.defaultCharset())
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
                        LoadingState(
                            modifier = Modifier.fillParentMaxSize(),
                            contentAlignment = Alignment.Center
                        )
                    }

                    pagingState.items.isEmpty() -> item {
                        ErrorState(
                            errorMessage = stringResource(id = R.string.msg_no_messages_found),
                            textStyle = MaterialTheme.typography.h6.copy(
                                fontFamily = appFontFamily,
                            ),
                            textAlign = TextAlign.Center,
                            contentAlignment = Alignment.Center
                        )
                    }

                    else -> items(pagingState.items.size) { index ->
                        val message = pagingState.items[index]
                        if (index >= pagingState.items.size - 1 && !pagingState.endReached) {
                            onEvent(MessageScreenAction.GetMessagesForChat)
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
                state = messageFieldState,
                hint = stringResource(id = R.string.enter_your_message),
                onSend = { onEvent(MessageScreenAction.SendMessage) },
                enabled = messageFieldState.isValid,

                )
        }
    }
}