package com.dracula.socialnetworktwitch.feature_auth.domain.utils

import com.dracula.socialnetworktwitch.core.utils.ValidationError

sealed class AuthValidationError : ValidationError() {
    object FieldEmpty : AuthValidationError()
    object InputTooShort : AuthValidationError()
    object InvalidEmail : AuthValidationError()
    object InvalidPassword : AuthValidationError()
}