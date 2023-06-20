package com.dracula.socialnetworktwitch.feature_activity.presentation

import androidx.lifecycle.viewModelScope
import com.dracula.socialnetworktwitch.core.presentation.utils.BaseViewModel
import com.dracula.socialnetworktwitch.core.utils.DefaultPaginator
import com.dracula.socialnetworktwitch.core.utils.PagingState
import com.dracula.socialnetworktwitch.feature_activity.domain.model.Activity
import com.dracula.socialnetworktwitch.feature_activity.domain.use_case.GetActivitiesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ActivityViewModel @Inject constructor(
    private val getActivitiesUseCase: GetActivitiesUseCase
) : BaseViewModel<PagingState<Activity>, ActivityAction>() {


    val paginator = DefaultPaginator(
        onLoad = { isLoading, refreshing ->
            setState {
                copy(
                    isLoading = isLoading,
                    refreshing = refreshing
                )
            }
        },
        onRequest = { page ->
            getActivitiesUseCase(page = page)
        },
        onSuccess = { newActivities ->
            setState { addNewItems(newActivities) }
        },
        onError = {
            showSnackbar(it)
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

    override fun onEvent(event: ActivityAction) {
        when (event) {
            is ActivityAction.ClickOnUser -> TODO()
            is ActivityAction.ClickOnParent -> TODO()
            ActivityAction.Refreshing -> loadActivities(refreshing = true)
        }
    }

    override fun initialState(): PagingState<Activity> {
        return PagingState()
    }
}