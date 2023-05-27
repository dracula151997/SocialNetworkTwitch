package com.dracula.socialnetworktwitch.feature_chat.presentation.chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.dracula.socialnetworktwitch.R
import com.dracula.socialnetworktwitch.core.presentation.components.StandardTopBar
import com.dracula.socialnetworktwitch.core.presentation.theme.PaddingMedium
import com.dracula.socialnetworktwitch.core.presentation.theme.SpaceMedium
import com.dracula.socialnetworktwitch.core.presentation.utils.Screens
import com.dracula.socialnetworktwitch.feature_chat.domain.Chat

@Composable
fun ChatScreen(navController: NavController, chats: List<Chat>) {
    Box(
        contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            StandardTopBar(title = stringResource(id = R.string.chats), navController)
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(SpaceMedium),
                contentPadding = PaddingValues(
                    horizontal = PaddingMedium,
                    vertical = PaddingMedium
                ),
                modifier = Modifier.fillMaxSize()
            ) {

                items(chats) { chat ->
                    ChatItem(
                        chat = chat,
                        onItemClick = {
                            navController.navigate(Screens.MessageScreen.route)
                        },
                    )
                }
            }
        }
    }
}