package com.dracula.socialnetworktwitch.feature_activity.domain.repository

import com.dracula.socialnetworktwitch.core.utils.ApiResult
import com.dracula.socialnetworktwitch.core.utils.Constants
import com.dracula.socialnetworktwitch.feature_activity.domain.model.Activity

interface ActivityRepository {
    suspend fun getActivitiesForUser(
        page: Int = Constants.DEFAULT_PAGE,
        pageSize: Int = Constants.DEFAULT_PAGE_SIZE
    ): ApiResult<List<Activity>>
}