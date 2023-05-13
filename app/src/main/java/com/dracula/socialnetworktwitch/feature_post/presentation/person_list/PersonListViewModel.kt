package com.dracula.socialnetworktwitch.feature_post.presentation.person_list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dracula.socialnetworktwitch.core.domain.use_cases.GetOwnUserIdUseCase
import com.dracula.socialnetworktwitch.core.utils.ApiResult
import com.dracula.socialnetworktwitch.core.utils.Constants
import com.dracula.socialnetworktwitch.core.utils.UiEvent
import com.dracula.socialnetworktwitch.core.utils.orUnknownError
import com.dracula.socialnetworktwitch.feature_post.domain.use_case.GetLikesForParentUseCase
import com.dracula.socialnetworktwitch.feature_profile.domain.use_case.ToggleFollowStateForUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonListViewModel @Inject constructor(
    private val getLikesForParentUseCase: GetLikesForParentUseCase,
    private val getOwnUserIdUseCase: GetOwnUserIdUseCase,
    private val toggleFollowStateForUserUseCase: ToggleFollowStateForUserUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    var state by mutableStateOf(PersonListState())
        private set

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    var ownUserId: String? = null
        private set


    init {
        savedStateHandle.get<String>(Constants.NavArguments.NAV_PARENT_ID)?.let { parentId ->
            getLikesForParent(parentId)
            ownUserId = getOwnUserIdUseCase()
        }
    }

    private fun getLikesForParent(parentId: String) {
        viewModelScope.launch {
            state = state.copy(
                isLoading = true,
            )
            when (val result = getLikesForParentUseCase(parentId = parentId)) {
                is ApiResult.Success -> {
                    state = state.copy(
                        isLoading = false,
                        users = result.data.orEmpty()
                    )
                }

                is ApiResult.Error -> {
                    state = state.copy(
                        isLoading = false,
                    )
                    _eventFlow.emit(
                        UiEvent.ShowSnackbar(
                            uiText = result.uiText.orUnknownError()
                        )
                    )
                }
            }

        }
    }

    fun onEvent(event: PersonListAction) {
        when (event) {
            is PersonListAction.ToggleFollowStateForUser -> toggleFollowStateForUser(event.userId)
        }
    }

    private fun toggleFollowStateForUser(userId: String) {
        viewModelScope.launch {
            val isFollowing = state.users.find { it.userId == userId }?.isFollowing == true
            state = state.copy(users = state.users.map {
                if (it.userId == userId) it.copy(isFollowing = !it.isFollowing)
                else it
            })
            val result = toggleFollowStateForUserUseCase(
                userId = userId, isFollowing = isFollowing
            )
            when (result) {
                is ApiResult.Success -> Unit

                is ApiResult.Error -> {
                    state = state.copy(users = state.users.map {
                        if (it.userId == userId) it.copy(isFollowing = isFollowing)
                        else it
                    })
                    _eventFlow.emit(UiEvent.ShowSnackbar(uiText = result.uiText.orUnknownError()))
                }

            }
        }
    }

}