package com.dracula.socialnetworktwitch.feature_profile.data.data_source.remote

import com.dracula.socialnetworktwitch.core.data.remote.dto.response.BasicApiResponse
import com.dracula.socialnetworktwitch.feature_profile.data.data_source.remote.dto.response.ProfileResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ProfileApi {
    @GET("/api/user/profile")
    suspend fun getProfile(
        @Query("userId") userId: String
    ): BasicApiResponse<ProfileResponse>

    companion object {
        const val BASE_URL = "http://10.0.2.2:8080"
    }
}