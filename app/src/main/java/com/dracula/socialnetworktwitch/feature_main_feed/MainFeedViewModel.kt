package com.dracula.socialnetworktwitch.feature_main_feed

import androidx.lifecycle.viewModelScope
import com.dracula.socialnetworktwitch.R
import com.dracula.socialnetworktwitch.core.domain.model.Post
import com.dracula.socialnetworktwitch.core.presentation.utils.BaseViewModel
import com.dracula.socialnetworktwitch.core.utils.ApiResult
import com.dracula.socialnetworktwitch.core.utils.DefaultPaginator
import com.dracula.socialnetworktwitch.core.utils.PagingState
import com.dracula.socialnetworktwitch.core.utils.ParentType
import com.dracula.socialnetworktwitch.core.utils.PostLiker
import com.dracula.socialnetworktwitch.core.utils.orUnknownError
import com.dracula.socialnetworktwitch.feature_post.domain.use_case.DeletePostUseCase
import com.dracula.socialnetworktwitch.feature_post.domain.use_case.GetPostsForFollowsUseCase
import com.dracula.socialnetworktwitch.feature_post.domain.use_case.ToggleLikeForParentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainFeedViewModel @Inject constructor(
    private val getPostsForFollowsUseCase: GetPostsForFollowsUseCase,
    private val toggleLikeForParentUseCase: ToggleLikeForParentUseCase,
    private val deletePostUseCase: DeletePostUseCase,
) : BaseViewModel<PagingState<Post>, MainFeedEvent>() {

    private val paginator = DefaultPaginator(
        onLoad = { isLoading, refreshing ->
            setState {
                copy(isLoading = isLoading, refreshing = refreshing)
            }
        },
        onRequest = { page ->
            getPostsForFollowsUseCase(page = page)
        },
        onSuccess = { result ->
            setState {
                addNewItems(result)
            }
        },
        onError = { message ->
            showSnackbar(message)
        }
    )

    @Inject
    lateinit var postLiker: PostLiker

    override fun onEvent(event: MainFeedEvent) {
        when (event) {
            is MainFeedEvent.LikePost -> toggleLikeForPost(
                postId = event.postId
            )

            is MainFeedEvent.DeletePost -> deletePost(event.postId)
            MainFeedEvent.Refresh -> loadNextPost(refreshing = true)
            MainFeedEvent.LoadNextPosts -> loadNextPost()
        }
    }

    private fun deletePost(postId: String) {
        viewModelScope.launch {
            when (val result = deletePostUseCase(postId)) {
                is ApiResult.Success -> {
                    setState {
                        copy(
                            items = viewState.items.filter { it.id != postId }
                        )
                    }
                    showSnackbar(R.string.successfully_deleted_post)
                }

                is ApiResult.Error -> {
                    showSnackbar(result.uiText.orUnknownError())
                }
            }
        }
    }

    init {
        onEvent(MainFeedEvent.LoadNextPosts)
    }

    override fun initialState(): PagingState<Post> {
        return PagingState()
    }


    private fun loadNextPost(refreshing: Boolean = false) {
        viewModelScope.launch {
            paginator.loadNextItems(refreshing)
        }
    }

    private fun toggleLikeForPost(postId: String) {
        viewModelScope.launch {
            postLiker.likePost(
                posts = viewState.items,
                postId = postId,
                onRequest = { currentlyLiked ->
                    toggleLikeForParentUseCase(postId, ParentType.Post.type, currentlyLiked)
                },
                onError = {

                },
                onStateUpdated = { newPosts ->
                    setState {
                        copy(items = newPosts)
                    }
                }
            )
        }
    }


}