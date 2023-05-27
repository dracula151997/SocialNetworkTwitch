package com.dracula.socialnetworktwitch.feature_chat.presentation.chat

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.dracula.socialnetworktwitch.feature_chat.domain.Chat

@Composable
fun ChatItem(
    chat: Chat,
    modifier: Modifier = Modifier,
    onItemClick: (chat: Chat) -> Unit,

    ) {
    ChatUserProfileItem(chat = chat, onItemClick = onItemClick, modifier = modifier)
}