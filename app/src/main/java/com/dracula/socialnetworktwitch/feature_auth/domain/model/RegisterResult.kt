package com.dracula.socialnetworktwitch.feature_auth.domain.model

import com.dracula.socialnetworktwitch.core.utils.UnitApiResult
import com.dracula.socialnetworktwitch.feature_auth.domain.utils.AuthValidationError

data class RegisterResult(
    val emailError: AuthValidationError? = null,
    val usernameError: AuthValidationError? = null,
    val passwordError: AuthValidationError? = null,
    val result: UnitApiResult? = null
){
    val hasEmailError get() = emailError != null
    val hasUsernameError get() = usernameError != null
    val hasPasswordError get() = passwordError != null
}