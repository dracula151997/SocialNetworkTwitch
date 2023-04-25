package com.dracula.socialnetworktwitch.feature_auth.domain.repository

import com.dracula.socialnetworktwitch.core.utils.ApiResult
import com.dracula.socialnetworktwitch.core.utils.SimpleResource
import com.dracula.socialnetworktwitch.feature_auth.data.dto.request.CreateAccountRequest

interface AuthRepository  {
    suspend fun register(email: String, username: String, password: String): SimpleResource

}