package com.dracula.socialnetworktwitch.feature_chat.presentation.message

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dracula.socialnetworktwitch.R
import com.dracula.socialnetworktwitch.core.presentation.components.SendTextField
import com.dracula.socialnetworktwitch.core.presentation.components.StandardAsyncImage
import com.dracula.socialnetworktwitch.core.presentation.components.StandardTopBar
import com.dracula.socialnetworktwitch.core.presentation.theme.SpaceMedium
import com.dracula.socialnetworktwitch.feature_chat.domain.Message

@Composable
fun MessagesScreen(
    navController: NavController,
    profilePicture: String? = null,
    viewModel: MessageViewModel = hiltViewModel(),
) {
    val messageState = viewModel.messageFieldState
    val messages = listOf(
        Message(
            fromId = "mi",
            toId = "sale",
            text = "appetere",
            formattedTime = "1/1/2023 4:00 PM",
            chatId = "enim",
            id = "ius"
        ),
        Message(
            fromId = "mi",
            toId = "sale",
            text = "appetere",
            formattedTime = "1/1/2023 4:10 PM",
            chatId = "enim",
            id = "ius"
        ),
        Message(
            fromId = "mi",
            toId = "sale",
            text = "appetere",
            formattedTime = "1/1/2023 4:10 PM",
            chatId = "enim",
            id = "ius"
        ),
    )
    Column(
        Modifier.fillMaxSize()
    ) {
        StandardTopBar(
            title = {
                StandardAsyncImage(url = profilePicture)
            },
            navController = navController
        )
        Column(modifier = Modifier) {
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(SpaceMedium),
                verticalArrangement = Arrangement.spacedBy(SpaceMedium)
            ) {
                items(messages) { message ->
                    RemoteMessageItem(message = message.text, formattedTime = message.formattedTime)
                    Spacer(modifier = Modifier.height(SpaceMedium))
                    OwnMessageItem(message = message.text, formattedTime = message.formattedTime)
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