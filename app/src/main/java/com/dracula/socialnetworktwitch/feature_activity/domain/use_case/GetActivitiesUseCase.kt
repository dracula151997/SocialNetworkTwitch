package com.dracula.socialnetworktwitch.feature_activity.domain.use_case

import androidx.paging.PagingData
import com.dracula.socialnetworktwitch.feature_activity.domain.model.Activity
import com.dracula.socialnetworktwitch.feature_activity.domain.repository.ActivityRepository
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class GetActivitiesUseCase @Inject constructor(
    private val repository: ActivityRepository
) {
    operator fun invoke(): Flow<PagingData<Activity>> {
        return repository.activities
    }
}