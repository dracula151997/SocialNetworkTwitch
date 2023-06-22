package com.dracula.socialnetworktwitch.feature_post.presentation.post_details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.dracula.socialnetworktwitch.R
import com.dracula.socialnetworktwitch.core.domain.use_cases.GetOwnUserIdUseCase
import com.dracula.socialnetworktwitch.core.presentation.utils.BaseViewModel
import com.dracula.socialnetworktwitch.core.presentation.utils.states.validator.NonEmptyFieldState
import com.dracula.socialnetworktwitch.core.utils.ApiResult
import com.dracula.socialnetworktwitch.core.utils.Constants
import com.dracula.socialnetworktwitch.core.utils.ParentType
import com.dracula.socialnetworktwitch.core.utils.orUnknownError
import com.dracula.socialnetworktwitch.feature_post.domain.use_case.CreateCommentUseCase
import com.dracula.socialnetworktwitch.feature_post.domain.use_case.GetCommentsForPostUseCase
import com.dracula.socialnetworktwitch.feature_post.domain.use_case.GetPostDetailsUseCase
import com.dracula.socialnetworktwitch.feature_post.domain.use_case.ToggleLikeForParentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostDetailsViewModel @Inject constructor(
    private val getPostDetailsUseCase: GetPostDetailsUseCase,
    private val getCommentsForPostUseCase: GetCommentsForPostUseCase,
    private val createCommentUseCase: CreateCommentUseCase,
    private val toggleLikeForParentUseCase: ToggleLikeForParentUseCase,
    private val getOwnUserIdUseCase: GetOwnUserIdUseCase,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel<PostDetailsState, PostDetailsEvent>() {

    var commentFieldState by mutableStateOf(NonEmptyFieldState())
        private set

    var ownUserId: String = ""
        private set

    init {
        savedStateHandle.postIdArgs?.let { postId ->
            getPostDetails(postId = postId)
            getCommentsForPost(postId = postId)
        }
        ownUserId = getOwnUserIdUseCase()

    }

    override fun onEvent(event: PostDetailsEvent) {
        when (event) {
            PostDetailsEvent.Comment -> createComment(
                postId = savedStateHandle.postIdArgs.orEmpty(),
                comment = commentFieldState.text
            )

            is PostDetailsEvent.LikeComment -> {
                val isLiked = viewState.comments.find { it.id == event.commentId }?.isLiked == true
                toggleLikeForParent(
                    parentId = event.commentId,
                    parentType = ParentType.Comment.type,
                    isLiked = isLiked
                )
            }

            PostDetailsEvent.LikePost -> {
                val isLiked = viewState.post?.isLiked == true
                toggleLikeForParent(
                    parentId = viewState.post?.id ?: return,
                    parentType = ParentType.Post.type,
                    isLiked = isLiked
                )
            }

            PostDetailsEvent.SharePost -> TODO()
        }
    }

    private fun createComment(postId: String, comment: String) {
        viewModelScope.launch {
            setState {
                copy(commentState = viewState.commentState.copy(isLoading = true))
            }
            val result = createCommentUseCase(
                postId = postId, comment = comment
            )

            setState {
                copy(commentState = viewState.commentState.copy(isLoading = false))
            }
            when (val result = result) {
                is ApiResult.Success -> {
                    showSnackbar(R.string.comment_posted_successfully)
                    getCommentsForPost(postId)
                }

                is ApiResult.Error -> {
                    showSnackbar(result.uiText.orUnknownError())
                }
            }
        }
    }

    private fun getPostDetails(postId: String) {
        viewModelScope.launch {
            setState {
                copy(isPostLoading = true)
            }

            when (val result = getPostDetailsUseCase(postId)) {
                is ApiResult.Success -> {
                    setState { copy(isPostLoading = false, post = result.data) }

                }

                is ApiResult.Error -> {
                    setState { copy(isPostLoading = false) }
                    showSnackbar(result.uiText.orUnknownError())
                }
            }
        }
    }

    private fun getCommentsForPost(postId: String) {
        viewModelScope.launch {
            setState { copy(isLoadingComments = true) }

            when (val result = getCommentsForPostUseCase(postId)) {
                is ApiResult.Success -> {
                    setState { copy(isLoadingComments = false, comments = result.data.orEmpty()) }

                }

                is ApiResult.Error -> {
                    setState { copy(isLoadingComments = false) }
                    showSnackbar(result.uiText.orUnknownError())
                }
            }
        }
    }

    private fun toggleLikeForParent(parentId: String, parentType: Int, isLiked: Boolean) {
        viewModelScope.launch {
            when (ParentType.fromType(type = parentType)) {
                ParentType.Post -> {
                    setState {
                        copy(post = viewState.post?.copy(isLiked = !isLiked))
                    }
                }

                ParentType.Comment -> {
                    setState {
                        copy(comments = viewState.comments.map {
                            if (it.id == parentId) it.copy(isLiked = !isLiked) else it
                        })
                    }

                }

                else -> Unit
            }
            when (toggleLikeForParentUseCase(parentId, parentType, isLiked)) {
                is ApiResult.Success -> Unit
                is ApiResult.Error -> {
                    when (ParentType.fromType(type = parentType)) {
                        ParentType.Post -> {
                            val post = viewState.post
                            setState {
                                copy(
                                    post = viewState.post?.copy(
                                        isLiked = isLiked,
                                        likeCount = if (isLiked) post?.likeCount?.minus(1)
                                            ?: 0 else post?.likeCount?.plus(1) ?: 0
                                    )
                                )
                            }
                        }

                        ParentType.Comment -> {
                            setState {
                                copy(
                                    comments = viewState.comments.map {
                                        if (it.id == parentId) it.copy(
                                            isLiked = isLiked,
                                            likeCount = if (it.isLiked) it.likeCount.minus(1) else it.likeCount.plus(
                                                1
                                            )
                                        ) else it
                                    }
                                )
                            }
                        }

                        else -> Unit
                    }
                }
            }
        }
    }

    override fun initialState(): PostDetailsState {
        return PostDetailsState()
    }

    private val SavedStateHandle.postIdArgs
        get() = savedStateHandle.get<String>(Constants.NavArguments.NAV_POST_ID)
    private val SavedStateHandle.shouldShowKeyboard get() = savedStateHandle.get<Boolean>(Constants.NavArguments.NAV_SHOW_KEYBOARD)
}