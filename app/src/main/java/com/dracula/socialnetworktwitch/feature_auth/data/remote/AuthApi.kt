package com.dracula.socialnetworktwitch.feature_auth.data.remote

import com.dracula.socialnetworktwitch.core.data.remote.dto.response.BasicApiResponse
import com.dracula.socialnetworktwitch.feature_auth.data.dto.request.CreateAccountRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("/api/user/create")
    suspend fun register(@Body request: CreateAccountRequest): BasicApiResponse

    companion object {
        const val BASE_URL = "http://10.0.2.2:8080"
    }

}
