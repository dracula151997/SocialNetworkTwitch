package com.dracula.socialnetworktwitch.feature_chat.presentation.message

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.dracula.socialnetworktwitch.core.presentation.utils.BaseViewModel
import com.dracula.socialnetworktwitch.core.presentation.utils.states.validator.NonEmptyFieldState
import com.dracula.socialnetworktwitch.core.utils.ApiResult
import com.dracula.socialnetworktwitch.core.utils.Constants
import com.dracula.socialnetworktwitch.core.utils.DefaultPaginator
import com.dracula.socialnetworktwitch.core.utils.PagingState
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
import javax.inject.Inject

@HiltViewModel
class MessageViewModel @Inject constructor(
    private val sendMessageUseCase: SendMessageUseCase,
    private val getMessagesForChatUseCase: GetMessagesForChatUseCase,
    private val observeChatEventUseCase: ObserveChatEventUseCase,
    private val observeMessagesUseCase: ObserveMessagesUseCase,
    private val savedStateHandle: SavedStateHandle,
) : BaseViewModel<PagingState<Message>, MessageScreenAction>() {
    var messageFieldState by mutableStateOf(NonEmptyFieldState())
        private set

    private val _messageReceived = MutableSharedFlow<MessageScreenEvent>(replay = 1)
    val messageReceived = _messageReceived.asSharedFlow()

    private val paginator = DefaultPaginator(
        onLoad = { isLoading, refreshing ->
            setState {
                copy(isLoading = isLoading, refreshing = refreshing)
            }
        },
        onRequest = { nextPage ->
            savedStateHandle.get<String>(Constants.NavArguments.NAV_CHAT_ID)?.let { chatId ->
                getMessagesForChatUseCase(chatId = chatId, page = nextPage)
            } ?: ApiResult.Error(UiText.unknownError())

        },
        onSuccess = { messages ->
            setState { addNewItems(newItems = messages) }
        },
        onError = { errorMessage ->
            showSnackbar(errorMessage)
        }
    )

    init {
        loadNextMessages()
        observeChatEvents()
        observeChatMessages()
    }

    override fun onEvent(event: MessageScreenAction) {
        when (event) {
            MessageScreenAction.SendMessage -> sendMessage()
            MessageScreenAction.GetMessagesForChat -> loadNextMessages()
        }
    }

    private fun loadNextMessages() {
        viewModelScope.launch {
            paginator.loadNextItems()
            _messageReceived.emit(MessageScreenEvent.AllMessagesLoaded)
        }

    }

    private fun observeChatEvents() {
        observeChatEventUseCase()
            .onEach {
                when (it) {
                    is WebSocket.Event.OnConnectionOpened<*> -> {
                        observeChatMessages()
                    }

                    is WebSocket.Event.OnMessageReceived -> {
                        _messageReceived.emit(MessageScreenEvent.NewMessageReceived)
                    }

                    else -> Unit
                }
            }.launchIn(viewModelScope)
    }

    private fun observeChatMessages() {
        observeMessagesUseCase()
            .onEach { newMessage ->
                setState {
                    addItem(newMessage)
                }
            }.launchIn(viewModelScope)
    }

    private fun sendMessage() {
        viewModelScope.launch {
            sendMessageUseCase(
                text = messageFieldState.text,
                toId = savedStateHandle.get<String>(Constants.NavArguments.NAV_REMOTE_USER_ID)
                    ?: return@launch,
                chatId = savedStateHandle.get<String>(Constants.NavArguments.NAV_CHAT_ID)
            )
            messageFieldState.clearText()
            _messageReceived.emit(MessageScreenEvent.MessageSent)
        }
    }

    override fun initialState(): PagingState<Message> {
        return PagingState()
    }
}

sealed interface MessageScreenEvent {
    object NewMessageReceived : MessageScreenEvent
    object AllMessagesLoaded : MessageScreenEvent
    object MessageSent : MessageScreenEvent
}