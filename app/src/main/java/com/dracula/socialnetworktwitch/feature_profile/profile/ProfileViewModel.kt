package com.dracula.socialnetworktwitch.feature_profile.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dracula.socialnetworktwitch.R
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
import com.dracula.socialnetworktwitch.core.utils.UiText
import com.dracula.socialnetworktwitch.core.utils.orUnknownError
import com.dracula.socialnetworktwitch.feature_post.domain.use_case.DeletePostUseCase
import com.dracula.socialnetworktwitch.feature_post.domain.use_case.ToggleLikeForParentUseCase
import com.dracula.socialnetworktwitch.feature_profile.domain.use_case.GetProfileUseCase
import com.dracula.socialnetworktwitch.feature_profile.domain.use_case.GetUserPostsUseCase
import com.dracula.socialnetworktwitch.feature_profile.domain.use_case.LogoutUseCase
import com.dracula.socialnetworktwitch.feature_profile.domain.use_case.ToggleFollowStateForUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getProfileUseCase: GetProfileUseCase,
    private val getUserPostsUseCase: GetUserPostsUseCase,
    private val getOwnUserIdUseCase: GetOwnUserIdUseCase,
    private val toggleLikeForParentUseCase: ToggleLikeForParentUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val deletePostUseCase: DeletePostUseCase,
    private val toggleFollowStateForUserUseCase: ToggleFollowStateForUserUseCase,
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
        onLoad = { isLoading, refreshing ->
            postsPagingState = postsPagingState.copy(isLoading = isLoading, refreshing = refreshing)
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

            is ProfileScreenAction.DeletePost -> deletePost(event.postId)
            is ProfileScreenAction.ToggleFollowStateForUser -> toggleFollowStateForUser(userId = event.userId)
            is ProfileScreenAction.Refreshing -> {
                getProfile(userId = event.userId, refreshing = true)
                loadNextPost(refreshing = true)
            }
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

    fun loadNextPost(refreshing: Boolean = false) {
        viewModelScope.launch {
            paginator.loadNextItems(refreshing = refreshing)
        }
    }

    private fun getProfile(userId: String?, refreshing: Boolean = false) {
        Timber.d("getProfile: $refreshing")
        viewModelScope.launch {
            _state.value = state.value.copy(
                isLoading = !refreshing,
                refreshing = refreshing,
            )
            when (val result = getProfileUseCase(userId ?: getOwnUserIdUseCase())) {
                is ApiResult.Success -> {
                    _state.value = state.value.copy(
                        isLoading = false,
                        refreshing = false,
                        data = result.data
                    )
                }

                is ApiResult.Error -> {
                    _state.value = state.value.copy(
                        isLoading = false,
                        refreshing = false,
                        data = result.data
                    )
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

    private fun toggleFollowStateForUser(userId: String) {
        viewModelScope.launch {
            val isFollowing = state.value.data?.isFollowing ?: false
            val followerCount = state.value.data?.followerCount ?: 0
            _state.value = state.value.copy(
                data = state.value.data?.copy(isFollowing = !isFollowing)
            )
            val result = toggleFollowStateForUserUseCase(
                userId = userId, isFollowing = isFollowing
            )
            when (result) {
                is ApiResult.Success -> {
                    _state.value = state.value.copy(
                        data = state.value.data?.copy(
                            followerCount = if (isFollowing) followerCount.minus(1) else followerCount.plus(
                                1
                            ),
                        )
                    )
                }

                is ApiResult.Error -> {
                    _state.value = state.value.copy(
                        data = state.value.data?.copy(
                            isFollowing = isFollowing,
                            followerCount = followerCount
                        )
                    )
                    _eventFlow.emit(UiEvent.ShowSnackbar(uiText = result.uiText.orUnknownError()))
                }

            }
        }
    }


}