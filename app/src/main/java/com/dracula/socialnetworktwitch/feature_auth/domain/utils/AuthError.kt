package com.dracula.socialnetworktwitch.feature_auth.domain.utils

import com.dracula.socialnetworktwitch.core.utils.Error

sealed class AuthError : Error() {
    object FieldEmpty : AuthError()
    object InputTooShort : AuthError()
    object InvalidEmail : AuthError()
    object InvalidPassword : AuthError()
}