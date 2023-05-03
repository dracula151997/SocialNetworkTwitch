package com.dracula.socialnetworktwitch.feature_search.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dracula.socialnetworktwitch.core.presentation.utils.states.StandardTextFieldState
import com.dracula.socialnetworktwitch.core.utils.ApiResult
import com.dracula.socialnetworktwitch.core.utils.Constants
import com.dracula.socialnetworktwitch.core.utils.UiEvent
import com.dracula.socialnetworktwitch.core.utils.orUnknownError
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
    private val searchUserUseCase: SearchUserUseCase
) : ViewModel() {

    var searchFieldState by mutableStateOf(StandardTextFieldState())
        private set

    var state by mutableStateOf(SearchState())
        private set

    private val _event = MutableSharedFlow<UiEvent>()
    val event: SharedFlow<UiEvent> = _event

    private var searchJob: Job? = null

    fun onEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.OnSearch -> {
                searchFieldState = searchFieldState.copy(text = event.query)
                search(event.query)
            }
        }
    }

    private fun search(query: String) {
        searchJob = viewModelScope.launch {
            delay(Constants.SEARCH_DELAY)
            val response = searchUserUseCase(searchFieldState.text)
            state = state.copy(isLoading = true)
            when (response) {
                is ApiResult.Success -> {
                    state = state.copy(
                        isLoading = false,
                        userItems = response.data ?: emptyList(),
                    )
                }

                is ApiResult.Error -> {
                    _event.emit(
                        UiEvent.SnackbarEvent(
                            uiText = response.uiText.orUnknownError()
                        )
                    )
                    state = state.copy(
                        isLoading = false,
                    )
                }
            }
        }
    }
}