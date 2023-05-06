package com.dracula.socialnetworktwitch.feature_activity.data.data_source.remote

import com.dracula.socialnetworktwitch.core.utils.Constants
import com.dracula.socialnetworktwitch.feature_activity.data.data_source.remote.dto.ActivityResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ActivityApi {
    @GET("/api/activity/get")
    suspend fun getActivityList(
        @Query("page") page: Int = Constants.DEFAULT_PAGE,
        @Query("pageSize") pageSize: Int = Constants.DEFAULT_PAGE_SIZE
    ): List<ActivityResponse>

    companion object {
        const val BASE_URL = "http://10.0.2.2:8080"
    }
}