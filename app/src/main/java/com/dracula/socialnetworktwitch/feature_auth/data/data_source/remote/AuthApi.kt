package com.dracula.socialnetworktwitch.feature_auth.data.data_source.remote

import com.dracula.socialnetworktwitch.core.data.remote.dto.response.BasicApiResponse
import com.dracula.socialnetworktwitch.core.data.remote.dto.response.UnitApiResponse
import com.dracula.socialnetworktwitch.feature_auth.data.data_source.remote.dto.request.CreateAccountRequest
import com.dracula.socialnetworktwitch.feature_auth.data.data_source.remote.dto.request.LoginRequest
import com.dracula.socialnetworktwitch.feature_auth.data.data_source.remote.dto.response.AuthResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApi {
    @POST("/api/user/create")
    suspend fun register(@Body request: CreateAccountRequest): UnitApiResponse

    @POST("/api/user/login")
    suspend fun login(@Body request: LoginRequest): BasicApiResponse<AuthResponse>

    @GET("/api/user/authenticate")
    suspend fun authenticate()

    companion object {
        const val BASE_URL = "http://10.0.2.2:8080"
    }

}