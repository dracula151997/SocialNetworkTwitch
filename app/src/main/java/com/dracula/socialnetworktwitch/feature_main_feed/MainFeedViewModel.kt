package com.dracula.socialnetworktwitch.feature_main_feed

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.dracula.socialnetworktwitch.feature_post.domain.use_case.GetPostsForFollowsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainFeedViewModel @Inject constructor(
    private val getPostsForFollowsUseCase: GetPostsForFollowsUseCase
) : ViewModel() {

    var state by mutableStateOf(MainFeedState())
        private set

    val posts = getPostsForFollowsUseCase()
        .cachedIn(viewModelScope)

    fun onEvent(event: MainFeedEvent){
        state = when(event){
            MainFeedEvent.LoadMorePosts -> state.copy(
                isLoadingNewPost = true
            )

            MainFeedEvent.LoadedPage -> state.copy(
                isLoadingFirstTime = false,
                isLoadingNewPost = false
            )
        }
    }

}