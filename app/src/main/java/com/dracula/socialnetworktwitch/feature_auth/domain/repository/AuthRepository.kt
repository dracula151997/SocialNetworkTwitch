package com.dracula.socialnetworktwitch.feature_auth.domain.repository

import com.dracula.socialnetworktwitch.core.utils.ApiResult
import com.dracula.socialnetworktwitch.core.utils.SimpleApiResult

interface AuthRepository {
    suspend fun register(email: String, username: String, password: String): SimpleApiResult

    suspend fun login(email: String, password: String): SimpleApiResult

}