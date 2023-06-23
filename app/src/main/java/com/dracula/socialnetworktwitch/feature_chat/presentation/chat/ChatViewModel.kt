package com.dracula.socialnetworktwitch.feature_chat.presentation.chat

import androidx.lifecycle.viewModelScope
import com.dracula.socialnetworktwitch.core.presentation.utils.BaseViewModel
import com.dracula.socialnetworktwitch.core.presentation.utils.UiEvent
import com.dracula.socialnetworktwitch.core.utils.ApiResult
import com.dracula.socialnetworktwitch.core.utils.orUnknownError
import com.dracula.socialnetworktwitch.feature_chat.domain.use_case.GetChatsForUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val getChatsForUserUseCase: GetChatsForUserUseCase,
) : BaseViewModel<ChatScreenState, ChatScreenEvent>() {

    override fun onEvent(event: ChatScreenEvent) {
        when (event) {
            ChatScreenEvent.Refreshing -> {
                getChatsForUser(refreshing = true)
            }
        }
    }

    override fun initialState(): ChatScreenState {
        return ChatScreenState()
    }


    init {
        getChatsForUser()
    }

    private fun getChatsForUser(refreshing: Boolean = false) {
        viewModelScope.launch {
            setState {
                copy(
                    isLoading = !refreshing,
                    refreshing = refreshing
                )
            }
            val response = getChatsForUserUseCase()
            setState {
                copy(
                    isLoading = false,
                    refreshing = false
                )
            }
            when (response) {
                is ApiResult.Success -> {
                    setState {
                        copy(chats = response.data.orEmpty())
                    }
                }

                is ApiResult.Error -> {
                    showSnackbar(response.uiText.orUnknownError())
                }
            }
        }
    }
}

sealed class ChatScreenEvent : UiEvent() {
    object Refreshing : ChatScreenEvent()
}