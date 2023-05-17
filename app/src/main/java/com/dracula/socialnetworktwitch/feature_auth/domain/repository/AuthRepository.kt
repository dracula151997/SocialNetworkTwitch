package com.dracula.socialnetworktwitch.feature_auth.domain.repository

import com.dracula.socialnetworktwitch.core.utils.UnitApiResult

interface AuthRepository {
    suspend fun register(email: String, username: String, password: String): UnitApiResult

    suspend fun login(email: String, password: String): UnitApiResult

    suspend fun authenticate(): UnitApiResult

    val ownUserId: String

    val userToken: String

}