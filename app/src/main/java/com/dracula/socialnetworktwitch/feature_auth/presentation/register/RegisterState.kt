package com.dracula.socialnetworktwitch.feature_auth.presentation.register

import com.dracula.socialnetworktwitch.core.utils.UiText

data class RegisterState(
    val isLoading: Boolean = false,
    val successful: Boolean? = null,
    val message: UiText? = null
){
    companion object{
        fun loading(): RegisterState {
            return RegisterState(isLoading = true)
        }

        fun success(): RegisterState{
            return RegisterState(successful = true)
        }

        fun error(message: UiText?) : RegisterState{
            return RegisterState(message = message)
        }
    }
}
