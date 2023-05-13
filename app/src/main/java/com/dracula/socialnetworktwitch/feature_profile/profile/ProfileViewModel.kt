package com.dracula.socialnetworktwitch.feature_profile.profile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.dracula.socialnetworktwitch.core.domain.use_cases.GetOwnUserIdUseCase
import com.dracula.socialnetworktwitch.core.utils.ApiResult
import com.dracula.socialnetworktwitch.core.utils.BaseUiEvent
import com.dracula.socialnetworktwitch.core.utils.Constants
import com.dracula.socialnetworktwitch.core.utils.ParentType
import com.dracula.socialnetworktwitch.core.utils.UiEvent
import com.dracula.socialnetworktwitch.core.utils.orUnknownError
import com.dracula.socialnetworktwitch.feature_main_feed.MainFeedUiEvent
import com.dracula.socialnetworktwitch.feature_post.domain.use_case.ToggleLikeForParentUseCase
import com.dracula.socialnetworktwitch.feature_profile.domain.use_case.GetProfileUseCase
import com.dracula.socialnetworktwitch.feature_profile.domain.use_case.GetUserPostsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getProfileUseCase: GetProfileUseCase,
    private val getUserPostsUseCase: GetUserPostsUseCase,
    private val getOwnUserIdUseCase: GetOwnUserIdUseCase,
    private val toggleLikeForParentUseCase: ToggleLikeForParentUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {


    private val _state = MutableStateFlow(ProfileState())
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<BaseUiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    val userPosts = getUserPostsUseCase(
        savedStateHandle.get<String>(Constants.NavArguments.NAV_USER_ID) ?: getOwnUserIdUseCase()
    ).cachedIn(viewModelScope)

    fun onEvent(event: ProfileScreenAction) {
        when (event) {
            is ProfileScreenAction.GetProfile -> {
                getProfile(event.userId)
            }

            is ProfileScreenAction.ToggleLikeForPost -> toggleLikeForParent(
                event.postId,
                isLiked = false
            )
        }
    }

    private fun getProfile(userId: String?) {
        viewModelScope.launch {
            _state.value = ProfileState.loading()
            when (val result = getProfileUseCase(userId ?: getOwnUserIdUseCase())) {
                is ApiResult.Success -> {
                    _state.value = ProfileState.success(result.data)
                }

                is ApiResult.Error -> {
                    _state.value = ProfileState.error()
                    _eventFlow.emit(UiEvent.ShowSnackbar(result.uiText.orUnknownError()))
                }
            }
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