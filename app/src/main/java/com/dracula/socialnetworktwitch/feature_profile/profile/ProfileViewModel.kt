package com.dracula.socialnetworktwitch.feature_profile.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dracula.socialnetworktwitch.core.domain.model.Post
import com.dracula.socialnetworktwitch.core.domain.use_cases.GetOwnUserIdUseCase
import com.dracula.socialnetworktwitch.core.utils.ApiResult
import com.dracula.socialnetworktwitch.core.utils.BaseUiEvent
import com.dracula.socialnetworktwitch.core.utils.Constants
import com.dracula.socialnetworktwitch.core.utils.DefaultPaginator
import com.dracula.socialnetworktwitch.core.utils.PagingState
import com.dracula.socialnetworktwitch.core.utils.ParentType
import com.dracula.socialnetworktwitch.core.utils.PostLiker
import com.dracula.socialnetworktwitch.core.utils.UiEvent
import com.dracula.socialnetworktwitch.core.utils.orUnknownError
import com.dracula.socialnetworktwitch.feature_post.domain.use_case.ToggleLikeForParentUseCase
import com.dracula.socialnetworktwitch.feature_profile.domain.use_case.GetProfileUseCase
import com.dracula.socialnetworktwitch.feature_profile.domain.use_case.GetUserPostsUseCase
import com.dracula.socialnetworktwitch.feature_profile.domain.use_case.LogoutUseCase
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
    private val logoutUseCase: LogoutUseCase,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileState())
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<BaseUiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    var postsPagingState by mutableStateOf<PagingState<Post>>(PagingState())
        private set

    @Inject
    lateinit var postLiker: PostLiker

    private val paginator = DefaultPaginator(
        onLoad = { isLoading ->
            postsPagingState = postsPagingState.copy(isLoading = isLoading)
        },
        onRequest = { page ->
            getUserPostsUseCase(
                savedStateHandle.get<String>(Constants.NavArguments.NAV_USER_ID)
                    ?: getOwnUserIdUseCase(),
                page = page,
                pageSize = Constants.DEFAULT_PAGE_SIZE,
            )
        },
        onSuccess = { result ->
            postsPagingState = postsPagingState.copy(
                isLoading = false,
                items = postsPagingState.items + result,
                endReached = result.isEmpty()
            )
        },
        onError = { message ->
            viewModelScope.launch {
                _eventFlow.emit(UiEvent.ShowSnackbar(message))
            }
        }
    )

    init {
        loadNextPost()
    }


    fun onEvent(event: ProfileScreenAction) {
        when (event) {
            is ProfileScreenAction.GetProfile -> {
                getProfile(event.userId)
            }

            is ProfileScreenAction.LikePost -> {
                toggleLikeForPost(
                    event.postId
                )
            }

            ProfileScreenAction.HideLogoutDialog -> _state.value = state.value.copy(
                showLogoutDialog = false
            )

            ProfileScreenAction.ShowLogoutDialog -> _state.value = state.value.copy(
                showLogoutDialog = true
            )

            ProfileScreenAction.ShowMorePopupMenu -> _state.value = state.value.copy(
                showMorePopupMenu = true
            )

            ProfileScreenAction.ShowMorePopupMenu -> _state.value = state.value.copy(
                showMorePopupMenu = false
            )

            ProfileScreenAction.Logout -> {
                _state.value = state.value.copy(
                    showLogoutDialog = false
                )
                logoutUseCase()
            }
        }
    }

    fun loadNextPost() {
        viewModelScope.launch {
            paginator.loadNextItems()
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

    private fun toggleLikeForPost(postId: String) {
        viewModelScope.launch {
            postLiker.likePost(
                posts = postsPagingState.items,
                postId = postId,
                onRequest = { currentlyLiked ->
                    toggleLikeForParentUseCase(postId, ParentType.Post.type, currentlyLiked)
                },
                onError = {},
                onStateUpdated = { newPosts ->
                    postsPagingState = postsPagingState.copy(items = newPosts)

                }
            )
        }
    }

}