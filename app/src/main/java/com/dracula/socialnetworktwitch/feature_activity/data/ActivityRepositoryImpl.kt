package com.dracula.socialnetworktwitch.feature_activity.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.dracula.socialnetworktwitch.core.utils.Constants
import com.dracula.socialnetworktwitch.feature_activity.data.data_source.remote.ActivityApi
import com.dracula.socialnetworktwitch.feature_activity.data.data_source.remote.paging.ActivitySource
import com.dracula.socialnetworktwitch.feature_activity.domain.model.Activity
import com.dracula.socialnetworktwitch.feature_activity.domain.repository.ActivityRepository
import kotlinx.coroutines.flow.Flow

class ActivityRepositoryImpl(
    private val api: ActivityApi
) : ActivityRepository {
    override val activities: Flow<PagingData<Activity>>
        get() = Pager(
            config = PagingConfig(
                pageSize = Constants.DEFAULT_PAGE_SIZE,
            ),
            pagingSourceFactory = { ActivitySource(api) }
        ).flow
}