package com.dracula.socialnetworktwitch.feature_profile.data.data_source.remote

import com.dracula.socialnetworktwitch.core.data.remote.dto.response.BasicApiResponse
import com.dracula.socialnetworktwitch.core.data.remote.dto.response.UnitApiResponse
import com.dracula.socialnetworktwitch.feature_profile.data.data_source.remote.dto.response.ProfileResponse
import com.dracula.socialnetworktwitch.feature_profile.data.data_source.remote.dto.response.SkillDto
import okhttp3.MultipartBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Query

interface ProfileApi {
    @GET("/api/user/profile")
    suspend fun getProfile(
        @Query("userId") userId: String
    ): BasicApiResponse<ProfileResponse>

    @GET("/api/skills/get")
    suspend fun getSkills(): BasicApiResponse<List<SkillDto>>

    @PUT("/api/user/update")
    @Multipart
    suspend fun updateProfile(
        @Part bannerImage: MultipartBody.Part?,
        @Part profilePicture: MultipartBody.Part?,
        @Part updateProfileData: MultipartBody.Part
    ): UnitApiResponse


    companion object {
        const val BASE_URL = "http://10.0.2.2:8080"
    }
}