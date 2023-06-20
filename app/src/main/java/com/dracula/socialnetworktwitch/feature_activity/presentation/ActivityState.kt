package com.dracula.socialnetworktwitch.feature_activity.presentation

import com.dracula.socialnetworktwitch.core.presentation.utils.UiState
import com.dracula.socialnetworktwitch.feature_activity.domain.model.Activity

data class ActivityState(
    val isLoading: Boolean = false,
    val activities: List<Activity> = emptyList(),
) : UiState()