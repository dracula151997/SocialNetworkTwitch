package com.dracula.socialnetworktwitch.feature_auth.domain.repository

import com.dracula.socialnetworktwitch.core.utils.UnitResult

interface AuthRepository {
    suspend fun register(email: String, username: String, password: String): UnitResult

    suspend fun login(email: String, password: String): UnitResult

    suspend fun authenticate() : UnitResult

}