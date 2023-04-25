package com.dracula.socialnetworktwitch.feature_auth.data.remote

import com.dracula.socialnetworktwitch.core.data.remote.dto.response.BasicApiResponse
import com.dracula.socialnetworktwitch.core.data.remote.dto.response.UnitApiResponse
import com.dracula.socialnetworktwitch.feature_auth.data.dto.request.CreateAccountRequest
import com.dracula.socialnetworktwitch.feature_auth.data.dto.request.LoginRequest
import com.dracula.socialnetworktwitch.feature_auth.data.dto.response.AuthResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("/api/user/create")
    suspend fun register(@Body request: CreateAccountRequest): UnitApiResponse

    @POST("/api/user/login")
    suspend fun login(@Body request: LoginRequest): BasicApiResponse<AuthResponse>

    companion object {
        const val BASE_URL = "http://10.0.2.2:8080"
    }

}
