package com.dracula.socialnetworktwitch.feature_activity.data

import com.dracula.socialnetworktwitch.R
import com.dracula.socialnetworktwitch.core.utils.ApiResult
import com.dracula.socialnetworktwitch.core.utils.UiText
import com.dracula.socialnetworktwitch.feature_activity.data.data_source.remote.ActivityApi
import com.dracula.socialnetworktwitch.feature_activity.domain.model.Activity
import com.dracula.socialnetworktwitch.feature_activity.domain.repository.ActivityRepository
import retrofit2.HttpException
import java.io.IOException

class ActivityRepositoryImpl(
    private val api: ActivityApi
) : ActivityRepository {
    override suspend fun getActivitiesForUser(
        page: Int,
        pageSize: Int
    ): ApiResult<List<Activity>> {
        return try {
            val response = api.getActivityList(page, pageSize)
            ApiResult.Success(response.map { it.toActivity() })
        } catch (e: IOException) {
            ApiResult.Error(UiText.StringResource(R.string.error_couldnot_reach_server))
        } catch (e: HttpException) {
            ApiResult.Error(UiText.StringResource(R.string.error_something_went_wrong))
        }
    }
}