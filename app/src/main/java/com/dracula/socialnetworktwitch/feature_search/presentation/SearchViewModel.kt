package com.dracula.socialnetworktwitch.feature_search.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dracula.socialnetworktwitch.core.presentation.utils.states.validator.TextFieldState
import com.dracula.socialnetworktwitch.core.utils.ApiResult
import com.dracula.socialnetworktwitch.core.utils.Constants
import com.dracula.socialnetworktwitch.core.utils.UiEvent
import com.dracula.socialnetworktwitch.core.utils.orUnknownError
import com.dracula.socialnetworktwitch.feature_profile.domain.use_case.ToggleFollowStateForUserUseCase
import com.dracula.socialnetworktwitch.feature_search.domain.SearchUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchUserUseCase: SearchUserUseCase,
    private val toggleFollowStateForUserUseCase: ToggleFollowStateForUserUseCase
) : ViewModel() {

    var searchFieldState by mutableStateOf(TextFieldState())
        private set

    var state by mutableStateOf(SearchState())
        private set

    private val _event = MutableSharedFlow<UiEvent>()
    val event: SharedFlow<UiEvent> = _event

    private var searchJob: Job? = null

    fun onEvent(event: SearchAction) {
        when (event) {
            is SearchAction.OnSearch -> {
                search(event.query)
            }

            is SearchAction.ToggleFollowState -> toggleFollowStateForUser(userId = event.userId)
            SearchAction.Refreshing -> search(
                query = searchFieldState.text,
                refreshing = true
            )
        }
    }

    private fun toggleFollowStateForUser(userId: String) {
        viewModelScope.launch {
            val isFollowing = state.userItems?.find { it.userId == userId }?.isFollowing == true
            state = state.copy(userItems = state.userItems?.map {
                if (it.userId == userId) it.copy(isFollowing = !it.isFollowing)
                else it
            })
            val result = toggleFollowStateForUserUseCase(
                userId = userId, isFollowing = isFollowing
            )
            when (result) {
                is ApiResult.Success -> Unit

                is ApiResult.Error -> {
                    state = state.copy(userItems = state.userItems?.map {
                        if (it.userId == userId) it.copy(isFollowing = isFollowing)
                        else it
                    })
                    _event.emit(UiEvent.ShowSnackbar(uiText = result.uiText.orUnknownError()))
                }

            }
        }
    }

    private fun search(query: String, refreshing: Boolean = false) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(Constants.SEARCH_DELAY)
            state = state.copy(
                isLoading = !refreshing,
                refreshing = refreshing
            )

            val response = searchUserUseCase(query)

            state = state.copy(
                isLoading = false,
                refreshing = false
            )
            when (response) {
                is ApiResult.Success -> {
                    state = state.copy(
                        userItems = response.data.orEmpty()
                    )
                }

                is ApiResult.Error -> {
                    _event.emit(
                        UiEvent.ShowSnackbar(
                            uiText = response.uiText.orUnknownError()
                        )
                    )
                }
            }
        }
    }
}