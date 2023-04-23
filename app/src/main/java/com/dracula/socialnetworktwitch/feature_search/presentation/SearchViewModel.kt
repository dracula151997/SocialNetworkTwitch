package com.dracula.socialnetworktwitch.feature_search.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.dracula.socialnetworktwitch.core.presentation.utils.states.StandardTextFieldState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(

) : ViewModel() {

    var searchState by mutableStateOf(StandardTextFieldState())
        private set

    fun setSearch(state: StandardTextFieldState) {
        searchState = state
    }
}