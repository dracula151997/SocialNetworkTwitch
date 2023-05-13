package com.dracula.socialnetworktwitch.feature_main_feed

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.dracula.socialnetworktwitch.core.utils.ApiResult
import com.dracula.socialnetworktwitch.core.utils.BaseUiEvent
import com.dracula.socialnetworktwitch.core.utils.ParentType
import com.dracula.socialnetworktwitch.core.utils.UiEvent
import com.dracula.socialnetworktwitch.core.utils.orUnknownError
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
) : ViewModel() {

    var state by mutableStateOf(MainFeedState())
        private set

    val posts = getPostsForFollowsUseCase().cachedIn(viewModelScope)

    private val _eventFlow = MutableSharedFlow<BaseUiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onEvent(event: MainFeedAction) {
        when (event) {
            MainFeedAction.LoadMorePosts -> state = state.copy(
                isLoadingNewPost = true
            )

            MainFeedAction.LoadedPage -> state = state.copy(
                isLoadingFirstTime = false, isLoadingNewPost = false
            )

            is MainFeedAction.LikePost -> toggleLikeForParent(
                parentId = event.postId,
                isLiked = false
            )
        }
    }

    private fun toggleLikeForParent(parentId: String, isLiked: Boolean) {
        viewModelScope.launch {
            when (val result =
                toggleLikeForParentUseCase(parentId, ParentType.Post.type, isLiked)) {
                is ApiResult.Success -> {
                    _eventFlow.emit(MainFeedUiEvent.LikedPost)
                }

                is ApiResult.Error -> {
                    _eventFlow.emit(UiEvent.ShowSnackbar(result.uiText.orUnknownError()))
                }
            }
        }
    }


}