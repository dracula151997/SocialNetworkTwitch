package com.dracula.socialnetworktwitch.feature_search.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import com.dracula.socialnetworktwitch.core.presentation.utils.BaseViewModel
import com.dracula.socialnetworktwitch.core.presentation.utils.states.validator.TextFieldState
import com.dracula.socialnetworktwitch.core.utils.ApiResult
import com.dracula.socialnetworktwitch.core.utils.Constants
import com.dracula.socialnetworktwitch.core.utils.orUnknownError
import com.dracula.socialnetworktwitch.feature_profile.domain.use_case.ToggleFollowStateForUserUseCase
import com.dracula.socialnetworktwitch.feature_search.domain.SearchUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchUserUseCase: SearchUserUseCase,
    private val toggleFollowStateForUserUseCase: ToggleFollowStateForUserUseCase
) : BaseViewModel<SearchState, SearchEvent>(), DefaultLifecycleObserver {

    var searchFieldState by mutableStateOf(TextFieldState())
        private set

    private var searchJob: Job? = null
    private var lastQuery = ""

    override fun initialState(): SearchState {
        return SearchState()
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        if (lastQuery.isNotEmpty())
            search(query = lastQuery)

    }

    override fun onEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.OnSearch -> {
                search(event.query)
            }

            is SearchEvent.ToggleFollowState -> toggleFollowStateForUser(userId = event.userId)
            SearchEvent.Refreshing -> search(
                query = searchFieldState.text,
                refreshing = true
            )
        }
    }

    private fun toggleFollowStateForUser(userId: String) {
        viewModelScope.launch {
            val isFollowing = viewState.userItems?.find { it.userId == userId }?.isFollowing == true
            setState {
                copy(
                    userItems = viewState.userItems?.map {
                        if (it.userId == userId) it.copy(
                            isFollowing = !it.isFollowing
                        ) else it
                    },
                )
            }

            val result = toggleFollowStateForUserUseCase(
                userId = userId, isFollowing = isFollowing
            )
            when (result) {
                is ApiResult.Success -> Unit

                is ApiResult.Error -> {
                    setState {
                        copy(userItems = viewState.userItems?.map {
                            if (it.userId == userId) it.copy(
                                isFollowing = isFollowing
                            ) else it
                        })
                    }
                    showSnackbar(result.uiText.orUnknownError())
                }

            }
        }
    }

    private fun search(query: String, refreshing: Boolean = false) {
        lastQuery = query
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(Constants.SEARCH_DELAY)

            setState { copy(isLoading = !refreshing, refreshing = refreshing) }

            val response = searchUserUseCase(query)

            setState { copy(isLoading = false, refreshing = false) }
            when (response) {
                is ApiResult.Success -> {
                    setState { copy(userItems = response.data.orEmpty()) }
                }

                is ApiResult.Error -> {
                    showSnackbar(uiText = response.uiText.orUnknownError())
                }
            }
        }
    }
}