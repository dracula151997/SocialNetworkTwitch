package com.dracula.socialnetworktwitch.feature_post.presentation.post_details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dracula.socialnetworktwitch.core.utils.ApiResult
import com.dracula.socialnetworktwitch.core.utils.Constants
import com.dracula.socialnetworktwitch.core.utils.UiEvent
import com.dracula.socialnetworktwitch.core.utils.orUnknownError
import com.dracula.socialnetworktwitch.feature_post.domain.use_case.GetCommentsForPostUseCase
import com.dracula.socialnetworktwitch.feature_post.domain.use_case.GetPostDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostDetailsViewModel @Inject constructor(
    private val getPostDetailsUseCase: GetPostDetailsUseCase,
    private val getCommentsForPostUseCase: GetCommentsForPostUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    var state by mutableStateOf(PostDetailsState())
        private set

    private val _event = MutableSharedFlow<UiEvent>()
    val event = _event.asSharedFlow()

    init {
        savedStateHandle.get<String>(Constants.NavArguments.NAV_POST_ID)?.let { postId ->
            getPostDetails(postId = postId)
            getCommentsForPost(postId = postId)
        }
    }

    fun onEvent(event: PostDetailsEvent) {
        when (event) {
            is PostDetailsEvent.Comment -> TODO()
            is PostDetailsEvent.LikeComment -> TODO()
            PostDetailsEvent.LikePost -> TODO()
            PostDetailsEvent.SharePost -> TODO()
        }
    }

    private fun getPostDetails(postId: String) {
        viewModelScope.launch {
            state = state.copy(
                isPostLoading = true,
            )
            when (val result = getPostDetailsUseCase(postId)) {
                is ApiResult.Success -> {
                    state = state.copy(
                        isPostLoading = false,
                        post = result.data,
                    )
                }

                is ApiResult.Error -> {
                    state = state.copy(
                        isPostLoading = false,
                    )
                    _event.emit(
                        UiEvent.SnackbarEvent(
                            result.uiText.orUnknownError()
                        )
                    )
                }
            }
        }
    }

    private fun getCommentsForPost(postId: String) {
        viewModelScope.launch {
            state = state.copy(
                isLoadingComments = true,
            )
            when (val result = getCommentsForPostUseCase(postId)) {
                is ApiResult.Success -> {
                    state = state.copy(
                        isLoadingComments = false,
                        comments = result.data.orEmpty()
                    )
                }

                is ApiResult.Error -> {
                    state = state.copy(
                        isLoadingComments = false,
                    )
                    _event.emit(
                        UiEvent.SnackbarEvent(
                            result.uiText.orUnknownError()
                        )
                    )
                }
            }
        }
    }
}