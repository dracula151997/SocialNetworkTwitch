package com.dracula.socialnetworktwitch.feature_activity.domain.repository

import androidx.paging.PagingData
import com.dracula.socialnetworktwitch.feature_activity.domain.model.Activity
import kotlinx.coroutines.flow.Flow

interface ActivityRepository {
    val activities: Flow<PagingData<Activity>>
}