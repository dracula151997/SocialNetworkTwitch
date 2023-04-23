package com.dracula.socialnetworktwitch.feature_search.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.dracula.socialnetworktwitch.core.presentation.utils.states.StandardTextFieldState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(

) : ViewModel() {

    private val _searchState = mutableStateOf(StandardTextFieldState())
    val searchState: State<StandardTextFieldState> = _searchState

    fun setSearchState(state: StandardTextFieldState) {
        _searchState.value = state
    }
}