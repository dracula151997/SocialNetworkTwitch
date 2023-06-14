package com.dracula.socialnetworktwitch.feature_activity.domain.use_case

import com.dracula.socialnetworktwitch.core.utils.ApiResult
import com.dracula.socialnetworktwitch.core.utils.Constants
import com.dracula.socialnetworktwitch.feature_activity.domain.model.Activity
import com.dracula.socialnetworktwitch.feature_activity.domain.repository.ActivityRepository
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class GetActivitiesUseCase @Inject constructor(
    private val repository: ActivityRepository
) {
    suspend operator fun invoke(
        page: Int,
        pageSize: Int = Constants.DEFAULT_PAGE_SIZE,
    ): ApiResult<List<Activity>> {
        return repository.getActivitiesForUser(page, pageSize)
    }
}