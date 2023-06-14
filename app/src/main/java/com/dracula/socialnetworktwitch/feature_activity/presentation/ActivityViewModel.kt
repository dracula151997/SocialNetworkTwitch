package com.dracula.socialnetworktwitch.feature_activity.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dracula.socialnetworktwitch.core.utils.DefaultPaginator
import com.dracula.socialnetworktwitch.core.utils.PagingState
import com.dracula.socialnetworktwitch.core.utils.UiEvent
import com.dracula.socialnetworktwitch.feature_activity.domain.model.Activity
import com.dracula.socialnetworktwitch.feature_activity.domain.use_case.GetActivitiesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ActivityViewModel @Inject constructor(
    private val getActivitiesUseCase: GetActivitiesUseCase
) : ViewModel() {
    var activitiesPagingState: PagingState<Activity> by mutableStateOf(PagingState())
        private set

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    val paginator = DefaultPaginator(
        onLoad = { isLoading, refreshing ->
            activitiesPagingState = activitiesPagingState.copy(
                isLoading = isLoading,
                refreshing = refreshing
            )
        },
        onRequest = { page ->
            getActivitiesUseCase(page = page)
        },
        onSuccess = { newActivities ->
            activitiesPagingState = activitiesPagingState.addNewItems(newActivities)
        },
        onError = {
            _eventFlow.emit(UiEvent.ShowSnackbar(it))
        }
    )

    init {
        loadActivities()
    }

    private fun loadActivities(refreshing: Boolean = false) {
        viewModelScope.launch {
            paginator.loadNextItems(refreshing = refreshing)
        }
    }

    fun onEvent(event: ActivityAction) {
        when (event) {
            is ActivityAction.ClickOnUser -> TODO()
            is ActivityAction.ClickOnParent -> TODO()
            ActivityAction.Refreshing -> loadActivities(refreshing = true)
        }
    }
}