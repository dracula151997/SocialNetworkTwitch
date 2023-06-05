package com.dracula.socialnetworktwitch.feature_chat.presentation.message

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dracula.socialnetworktwitch.core.presentation.utils.states.StandardTextFieldState
import com.dracula.socialnetworktwitch.core.utils.ApiResult
import com.dracula.socialnetworktwitch.core.utils.Constants
import com.dracula.socialnetworktwitch.core.utils.DefaultPaginator
import com.dracula.socialnetworktwitch.core.utils.PagingState
import com.dracula.socialnetworktwitch.core.utils.UiEvent
import com.dracula.socialnetworktwitch.core.utils.UiText
import com.dracula.socialnetworktwitch.feature_chat.domain.model.Message
import com.dracula.socialnetworktwitch.feature_chat.domain.use_case.GetMessagesForChatUseCase
import com.dracula.socialnetworktwitch.feature_chat.domain.use_case.ObserveChatEventUseCase
import com.dracula.socialnetworktwitch.feature_chat.domain.use_case.ObserveMessagesUseCase
import com.dracula.socialnetworktwitch.feature_chat.domain.use_case.SendMessageUseCase
import com.tinder.scarlet.WebSocket
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MessageViewModel @Inject constructor(
    private val sendMessageUseCase: SendMessageUseCase,
    private val getMessagesForChatUseCase: GetMessagesForChatUseCase,
    private val observeChatEventUseCase: ObserveChatEventUseCase,
    private val observeMessagesUseCase: ObserveMessagesUseCase,
    private val savedStateHandle: SavedStateHandle

) : ViewModel() {
    var messageFieldState by mutableStateOf(StandardTextFieldState())
        private set

    var pagingState by mutableStateOf(PagingState<Message>())
        private set

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val paginator = DefaultPaginator(
        onLoad = { isLoading ->
            pagingState = pagingState.copy(isLoading = isLoading)
        },
        onRequest = { nextPage ->
            savedStateHandle.get<String>(Constants.NavArguments.NAV_CHAT_ID)?.let { chatId ->
                getMessagesForChatUseCase(chatId = chatId, page = nextPage)
            } ?: ApiResult.Error(UiText.unknownError())

        },
        onSuccess = { messages ->
            pagingState = pagingState.addNewItems(newItems = messages)
        },
        onError = { errorMessage ->
            _eventFlow.emit(UiEvent.ShowSnackbar(errorMessage))
        }
    )

    init {
        loadNextMessages()
        observeChatEvents()
        observeChatMessages()
        Timber.d("${savedStateHandle.keys()}")
    }

    fun onEvent(event: MessageEvent) {
        when (event) {
            is MessageEvent.EnteredMessage -> messageFieldState = messageFieldState.copy(
                text = event.message
            )

            MessageEvent.SendMessage -> sendMessage()

            MessageEvent.GetMessagesForChat -> loadNextMessages()
        }
    }

    private fun loadNextMessages() {
        viewModelScope.launch {
            paginator.loadNextItems()
        }

    }

    private fun observeChatEvents() {
        observeChatEventUseCase()
            .onEach {
                when (it) {
                    is WebSocket.Event.OnConnectionOpened<*> -> {
                        observeChatMessages()
                        Timber.d("OnConnectionOpened")
                    }

                    is WebSocket.Event.OnMessageReceived -> Timber.d("OnMessageReceived: ${it.message}")
                    is WebSocket.Event.OnConnectionClosing -> Timber.d("OnConnectionClosing: ${it.shutdownReason}")
                    is WebSocket.Event.OnConnectionClosed -> Timber.d("OnConnectionClosed: ${it.shutdownReason}")
                    is WebSocket.Event.OnConnectionFailed -> Timber.e("OnConnectionFailed: ${it.throwable}")
                }
            }.launchIn(viewModelScope)
    }

    private fun observeChatMessages() {
        observeMessagesUseCase()
            .onEach { newMessage ->
                pagingState = pagingState.copy(
                    items = pagingState.items + newMessage
                )
            }.launchIn(viewModelScope)
    }

    private fun sendMessage() {
        viewModelScope.launch {
            sendMessageUseCase(
                text = messageFieldState.text,
                toId = savedStateHandle.get<String>(Constants.NavArguments.NAV_REMOTE_USER_ID)
                    .orEmpty(),
                chatId = savedStateHandle.get<String>(Constants.NavArguments.NAV_CHAT_ID)
            )
        }
    }
}