package com.dracula.socialnetworktwitch.feature_post.presentation.post_details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dracula.socialnetworktwitch.R
import com.dracula.socialnetworktwitch.core.presentation.utils.states.StandardTextFieldState
import com.dracula.socialnetworktwitch.core.utils.ApiResult
import com.dracula.socialnetworktwitch.core.utils.Constants
import com.dracula.socialnetworktwitch.core.utils.UiEvent
import com.dracula.socialnetworktwitch.core.utils.UiText
import com.dracula.socialnetworktwitch.core.utils.orUnknownError
import com.dracula.socialnetworktwitch.feature_post.domain.use_case.CreateCommentUseCase
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
    private val createCommentUseCase: CreateCommentUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    var state by mutableStateOf(PostDetailsState())
        private set

    var commentState by mutableStateOf(CommentState())
        private set

    private val _event = MutableSharedFlow<UiEvent>()
    val event = _event.asSharedFlow()

    var commentFieldState by mutableStateOf(StandardTextFieldState())
        private set

    init {
        savedStateHandle.postIdArgs?.let { postId ->
            getPostDetails(postId = postId)
            getCommentsForPost(postId = postId)
        }
    }

    fun onEvent(event: PostDetailsEvent) {
        when (event) {
            PostDetailsEvent.Comment -> createComment(
                postId = savedStateHandle.postIdArgs.orEmpty(),
                comment = commentFieldState.text
            )

            is PostDetailsEvent.LikeComment -> TODO()
            PostDetailsEvent.LikePost -> TODO()
            PostDetailsEvent.SharePost -> TODO()
            is PostDetailsEvent.CommentEntered -> {
                commentFieldState = StandardTextFieldState(
                    text = event.commentText,
                )
            }
        }
    }

    private fun createComment(postId: String, comment: String) {
        viewModelScope.launch {
            commentState = CommentState(isLoading = true)
            val result =
                createCommentUseCase(
                    postId = postId,
                    comment = comment
                )
            if (result.hasCommentError)
                commentFieldState = commentFieldState.copy(
                    error = result.commentError
                )
            if (result.hasPostIdError)
                _event.emit(UiEvent.SnackbarEvent(UiText.unknownError()))

            commentState = commentState.copy(isLoading = false)
            when (val result = result.result) {
                is ApiResult.Success -> {
                    commentFieldState.defaultState()
                    _event.emit(UiEvent.SnackbarEvent(UiText.StringResource(R.string.comment_posted_successfully)))
                    getCommentsForPost(postId)
                }

                is ApiResult.Error -> {
                    _event.emit(UiEvent.SnackbarEvent(result.uiText.orUnknownError()))
                }

                null -> Unit
            }
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

    val SavedStateHandle.postIdArgs
        get() = savedStateHandle.get<String>(Constants.NavArguments.NAV_POST_ID)
}