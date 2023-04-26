package com.dracula.socialnetworktwitch.feature_auth.domain.model

import com.dracula.socialnetworktwitch.core.utils.UnitApiResult
import com.dracula.socialnetworktwitch.feature_auth.domain.utils.AuthError

data class RegisterResult(
    val emailError: AuthError? = null,
    val usernameError: AuthError? = null,
    val passwordError: AuthError? = null,
    val result: UnitApiResult? = null
){
    val hasEmailError get() = emailError != null
    val hasUsernameError get() = usernameError != null
    val hasPasswordError get() = passwordError != null
}