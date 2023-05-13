package com.dracula.socialnetworktwitch.feature_activity.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.dracula.socialnetworktwitch.feature_activity.domain.use_case.GetActivitiesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ActivityViewModel @Inject constructor(
    private val getActivitiesUseCase: GetActivitiesUseCase
) : ViewModel() {

    val activities = getActivitiesUseCase().cachedIn(viewModelScope)

    var state by mutableStateOf(ActivityState())
        private set

    fun onEvent(event: ActivityAction) {
        when (event) {
            is ActivityAction.ClickOnUser -> TODO()
            is ActivityAction.ClickOnParent -> TODO()
        }
    }
}