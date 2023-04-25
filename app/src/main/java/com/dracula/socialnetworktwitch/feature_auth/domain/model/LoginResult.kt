package com.dracula.socialnetworktwitch.feature_auth.domain.model

import com.dracula.socialnetworktwitch.core.utils.SimpleApiResult
import com.dracula.socialnetworktwitch.feature_auth.domain.utils.AuthError

data class LoginResult(
    val emailError: AuthError? = null,
    val passwordError: AuthError? = null,
    val result: SimpleApiResult? = null
)
