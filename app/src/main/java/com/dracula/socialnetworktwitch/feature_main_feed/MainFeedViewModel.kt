package com.dracula.socialnetworktwitch.feature_main_feed

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dracula.socialnetworktwitch.R
import com.dracula.socialnetworktwitch.core.domain.model.Post
import com.dracula.socialnetworktwitch.core.utils.ApiResult
import com.dracula.socialnetworktwitch.core.utils.BaseUiEvent
import com.dracula.socialnetworktwitch.core.utils.DefaultPaginator
import com.dracula.socialnetworktwitch.core.utils.PagingState
import com.dracula.socialnetworktwitch.core.utils.ParentType
import com.dracula.socialnetworktwitch.core.utils.PostLiker
import com.dracula.socialnetworktwitch.core.utils.UiEvent
import com.dracula.socialnetworktwitch.core.utils.UiText
import com.dracula.socialnetworktwitch.core.utils.orUnknownError
import com.dracula.socialnetworktwitch.feature_post.domain.use_case.DeletePostUseCase
import com.dracula.socialnetworktwitch.feature_post.domain.use_case.GetPostsForFollowsUseCase
import com.dracula.socialnetworktwitch.feature_post.domain.use_case.ToggleLikeForParentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainFeedViewModel @Inject constructor(
    private val getPostsForFollowsUseCase: GetPostsForFollowsUseCase,
    private val toggleLikeForParentUseCase: ToggleLikeForParentUseCase,
    private val deletePostUseCase: DeletePostUseCase,
) : ViewModel() {

    private val _eventFlow = MutableSharedFlow<BaseUiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    var postsPagingState by mutableStateOf(PagingState<Post>())
        private set

    private val paginator = DefaultPaginator(
        onLoad = { isLoading, refreshing ->
            postsPagingState = postsPagingState.copy(isLoading = isLoading, refreshing = refreshing)
        },
        onRequest = { page ->
            getPostsForFollowsUseCase(page = page)
        },
        onSuccess = { result ->
            postsPagingState = postsPagingState.addNewItems(result)
        },
        onError = { message ->
            viewModelScope.launch { _eventFlow.emit(UiEvent.ShowSnackbar(message)) }
        }
    )

    @Inject
    lateinit var postLiker: PostLiker

    fun onEvent(event: MainFeedAction) {
        when (event) {
            is MainFeedAction.LikePost -> toggleLikeForPost(
                postId = event.postId
            )

            is MainFeedAction.DeletePost -> deletePost(event.postId)
            MainFeedAction.Refresh -> loadNextPost(refreshing = true)
        }
    }

    private fun deletePost(postId: String) {
        viewModelScope.launch {
            when (val result = deletePostUseCase(postId)) {
                is ApiResult.Success -> {
                    postsPagingState = postsPagingState.copy(
                        items = postsPagingState.items.filter { it.id != postId }
                    )
                    _eventFlow.emit(UiEvent.ShowSnackbar(UiText.StringResource(R.string.successfully_deleted_post)))
                }

                is ApiResult.Error -> {
                    _eventFlow.emit(UiEvent.ShowSnackbar(result.uiText.orUnknownError()))
                }
            }
        }
    }

    init {
        loadNextPost()
    }

    fun loadNextPost(refreshing: Boolean = false) {
        viewModelScope.launch {
            paginator.loadNextItems(refreshing)
        }
    }


    private fun toggleLikeForPost(postId: String) {

        viewModelScope.launch {
            postLiker.likePost(
                posts = postsPagingState.items,
                postId = postId,
                onRequest = { currentlyLiked ->
                    toggleLikeForParentUseCase(postId, ParentType.Post.type, currentlyLiked)
                },
                onError = {

                },
                onStateUpdated = { newPosts ->
                    postsPagingState = postsPagingState.copy(items = newPosts)
                }
            )
        }
    }


}