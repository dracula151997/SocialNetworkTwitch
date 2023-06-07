package com.dracula.socialnetworktwitch.feature_chat.presentation.chat

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dracula.socialnetworktwitch.core.utils.ApiResult
import com.dracula.socialnetworktwitch.core.utils.UiEvent
import com.dracula.socialnetworktwitch.core.utils.orUnknownError
import com.dracula.socialnetworktwitch.feature_chat.domain.use_case.GetChatsForUserUseCase
import com.dracula.socialnetworktwitch.feature_chat.domain.use_case.InitializeRepositoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val getChatsForUserUseCase: GetChatsForUserUseCase,
    private val initializeRepositoryUseCase: InitializeRepositoryUseCase,
) : ViewModel() {
    var state by mutableStateOf(ChatState())
        private set

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        getChatsForUser()
        initializeRepositoryUseCase()
    }


    private fun getChatsForUser() {
        viewModelScope.launch {
            state = state.copy(
                isLoading = true
            )
            val response = getChatsForUserUseCase()
            state = state.copy(
                isLoading = false
            )
            when (response) {
                is ApiResult.Success -> state = state.copy(
                    chats = response.data.orEmpty()
                )

                is ApiResult.Error -> {
                    _eventFlow.emit(
                        UiEvent.ShowSnackbar(response.uiText.orUnknownError())
                    )
                }
            }
        }
    }
}