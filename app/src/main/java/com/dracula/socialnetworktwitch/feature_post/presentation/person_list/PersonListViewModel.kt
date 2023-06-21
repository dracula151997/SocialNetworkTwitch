package com.dracula.socialnetworktwitch.feature_post.presentation.person_list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.dracula.socialnetworktwitch.core.domain.use_cases.GetOwnUserIdUseCase
import com.dracula.socialnetworktwitch.core.presentation.utils.BaseViewModel
import com.dracula.socialnetworktwitch.core.utils.ApiResult
import com.dracula.socialnetworktwitch.core.utils.Constants
import com.dracula.socialnetworktwitch.core.utils.orUnknownError
import com.dracula.socialnetworktwitch.feature_post.domain.use_case.GetLikesForParentUseCase
import com.dracula.socialnetworktwitch.feature_profile.domain.use_case.ToggleFollowStateForUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonListViewModel @Inject constructor(
    private val getLikesForParentUseCase: GetLikesForParentUseCase,
    private val getOwnUserIdUseCase: GetOwnUserIdUseCase,
    private val toggleFollowStateForUserUseCase: ToggleFollowStateForUserUseCase,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<PersonListState, PersonListEvent>() {

    var ownUserId: String? = null
        private set

    override fun initialState(): PersonListState {
        return PersonListState()
    }


    init {
        savedStateHandle.get<String>(Constants.NavArguments.NAV_PARENT_ID)?.let { parentId ->
            getLikesForParent(parentId)
            ownUserId = getOwnUserIdUseCase()
        }
    }

    private fun getLikesForParent(parentId: String) {
        viewModelScope.launch {
            setState {
                copy(isLoading = true)
            }

            when (val result = getLikesForParentUseCase(parentId = parentId)) {
                is ApiResult.Success -> {
                    setState {
                        copy(isLoading = false, users = result.data.orEmpty())
                    }

                }

                is ApiResult.Error -> {
                    setState {
                        copy(
                            isLoading = false
                        )
                    }
                    showSnackbar(uiText = result.uiText.orUnknownError())

                }
            }

        }
    }

    override fun onEvent(event: PersonListEvent) {
        when (event) {
            is PersonListEvent.ToggleFollowStateForUser -> toggleFollowStateForUser(event.userId)
        }
    }

    private fun toggleFollowStateForUser(userId: String) {
        viewModelScope.launch {
            val isFollowing = viewState.users.find { it.userId == userId }?.isFollowing == true
            setState {
                copy(
                    users = viewState.users.map {
                        if (it.userId == userId) it.copy(isFollowing = !it.isFollowing)
                        else it
                    }
                )
            }

            val result = toggleFollowStateForUserUseCase(
                userId = userId, isFollowing = isFollowing
            )
            when (result) {
                is ApiResult.Success -> Unit

                is ApiResult.Error -> {
                    setState {
                        copy(
                            users = viewState.users.map {
                                if (it.userId == userId) it.copy(isFollowing = isFollowing)
                                else it
                            }
                        )
                    }
                    showSnackbar(uiText = result.uiText.orUnknownError())
                }

            }
        }
    }

}