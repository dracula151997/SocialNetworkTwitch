package com.dracula.socialnetworktwitch.feature_auth.domain.model

import com.dracula.socialnetworktwitch.core.utils.UnitApiResult
import com.dracula.socialnetworktwitch.feature_auth.domain.utils.AuthValidationError

data class LoginResult(
    val emailError: AuthValidationError? = null,
    val passwordError: AuthValidationError? = null,
    val result: UnitApiResult? = null
)
