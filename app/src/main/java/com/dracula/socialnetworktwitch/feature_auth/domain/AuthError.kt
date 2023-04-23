package com.dracula.socialnetworktwitch.feature_auth.domain

import com.dracula.socialnetworktwitch.core.utils.Error

sealed class AuthError : Error() {
    object FieldEmpty : Error()
    object InputTooShort : AuthError()
    object InvalidEmail : AuthError()
    object InvalidPassword : AuthError()
}