package com.dracula.socialnetworktwitch.feature_profile.profile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.dracula.socialnetworktwitch.R
import com.dracula.socialnetworktwitch.core.domain.use_cases.GetOwnUserIdUseCase
import com.dracula.socialnetworktwitch.core.presentation.utils.BaseViewModel
import com.dracula.socialnetworktwitch.core.utils.ApiResult
import com.dracula.socialnetworktwitch.core.utils.Constants
import com.dracula.socialnetworktwitch.core.utils.DefaultPaginator
import com.dracula.socialnetworktwitch.core.utils.ParentType
import com.dracula.socialnetworktwitch.core.utils.PostLiker
import com.dracula.socialnetworktwitch.core.utils.orUnknownError
import com.dracula.socialnetworktwitch.feature_chat.domain.use_case.CleanupScarletUseCase
import com.dracula.socialnetworktwitch.feature_post.domain.use_case.DeletePostUseCase
import com.dracula.socialnetworktwitch.feature_post.domain.use_case.ToggleLikeForParentUseCase
import com.dracula.socialnetworktwitch.feature_profile.domain.use_case.GetProfileUseCase
import com.dracula.socialnetworktwitch.feature_profile.domain.use_case.GetUserPostsUseCase
import com.dracula.socialnetworktwitch.feature_profile.domain.use_case.LogoutUseCase
import com.dracula.socialnetworktwitch.feature_profile.domain.use_case.ToggleFollowStateForUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
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
    private val cleanupScarletUseCase: CleanupScarletUseCase
) : BaseViewModel<ProfileState, ProfileScreenEvent>() {


    @Inject
    lateinit var postLiker: PostLiker

    private val paginator = DefaultPaginator(
        onLoad = { isLoading, refreshing ->
            setState {
                copy(isLoading = isLoading, refreshing = refreshing)
            }
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
            setState {
                copy(
                    isLoading = false,
                    pagingState = viewState.pagingState.addNewItems(result)
                )
            }
        },
        onError = { message ->
            showSnackbar(message)
        }
    )

    init {
        onEvent(ProfileScreenEvent.GetProfile(savedStateHandle.get<String>(Constants.NavArguments.NAV_USER_ID)))
        onEvent(ProfileScreenEvent.LoadNextPosts)
    }


    override fun initialState(): ProfileState {
        return ProfileState()
    }

    override fun onEvent(event: ProfileScreenEvent) {
        when (event) {
            is ProfileScreenEvent.GetProfile -> {
                getProfile(event.userId)
            }

            is ProfileScreenEvent.LikePost -> {
                toggleLikeForPost(
                    event.postId
                )
            }

            ProfileScreenEvent.HideLogoutDialog -> {
                setState { copy(showLogoutDialog = false) }
            }

            ProfileScreenEvent.ShowLogoutDialog -> {
                setState { copy(showLogoutDialog = true) }
            }

            ProfileScreenEvent.ShowMorePopupMenu -> {
                setState { copy(showMorePopupMenu = true) }
            }

            ProfileScreenEvent.ShowMorePopupMenu -> {
                setState { copy(showMorePopupMenu = false) }
            }

            ProfileScreenEvent.Logout -> {
                setState {
                    copy(
                        showLogoutDialog = false
                    )
                }
                logoutUseCase()
                cleanupScarletUseCase()
            }

            is ProfileScreenEvent.DeletePost -> deletePost(event.postId)
            is ProfileScreenEvent.ToggleFollowStateForUser -> toggleFollowStateForUser(userId = event.userId)
            is ProfileScreenEvent.Refreshing -> {
                getProfile(userId = event.userId, refreshing = true)
                loadNextPost(refreshing = true)
            }

            ProfileScreenEvent.LoadNextPosts -> loadNextPost()
        }
    }

    private fun deletePost(postId: String) {
        viewModelScope.launch {
            when (val result = deletePostUseCase(postId)) {
                is ApiResult.Success -> {
                    setState {
                        copy(
                            pagingState = viewState.pagingState.copy(
                                items = viewState.pagingState.items.filter { it.id != postId }
                            )
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

    private fun loadNextPost(refreshing: Boolean = false) {
        viewModelScope.launch {
            paginator.loadNextItems(refreshing = refreshing)
        }
    }

    private fun getProfile(userId: String?, refreshing: Boolean = false) {
        viewModelScope.launch {
            setState {
                copy(
                    isLoading = !refreshing,
                    refreshing = refreshing
                )
            }
            val result = getProfileUseCase(userId ?: getOwnUserIdUseCase())
            setState {
                copy(
                    isLoading = false,
                    refreshing = false,
                )
            }

            when (result) {
                is ApiResult.Success -> {
                    setState { copy(data = result.data) }

                }

                is ApiResult.Error -> {
                    showSnackbar(result.uiText.orUnknownError())
                }
            }
        }
    }

    private fun toggleLikeForPost(postId: String) {
        viewModelScope.launch {
            postLiker.likePost(
                posts = viewState.pagingState.items,
                postId = postId,
                onRequest = { currentlyLiked ->
                    toggleLikeForParentUseCase(postId, ParentType.Post.type, currentlyLiked)
                },
                onError = {},
                onStateUpdated = { newPosts ->
                    setState {
                        copy(
                            pagingState = viewState.pagingState.copy(items = newPosts)
                        )
                    }

                }
            )
        }
    }

    private fun toggleFollowStateForUser(userId: String) {
        viewModelScope.launch {
            val isFollowing = viewState.data?.isFollowing ?: false
            val followerCount = viewState.data?.followerCount ?: 0
            setState {
                copy(
                    data = viewState.data?.copy(isFollowing = !isFollowing)
                )
            }

            val result = toggleFollowStateForUserUseCase(
                userId = userId, isFollowing = isFollowing
            )
            when (result) {
                is ApiResult.Success -> {
                    setState {
                        copy(
                            data = viewState.data?.copy(
                                followerCount = if (isFollowing) followerCount.minus(1) else followerCount.plus(
                                    1
                                )
                            )
                        )
                    }
                }

                is ApiResult.Error -> {
                    setState {
                        copy(
                            data = viewState.data?.copy(
                                isFollowing = isFollowing,
                                followerCount = followerCount
                            )
                        )
                    }
                    showSnackbar(uiText = result.uiText.orUnknownError())
                }

            }
        }
    }


}