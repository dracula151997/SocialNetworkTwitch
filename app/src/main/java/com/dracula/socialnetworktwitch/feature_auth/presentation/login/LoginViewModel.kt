package com.dracula.socialnetworktwitch.feature_auth.presentation.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {
    var usernameText by mutableStateOf("")
        private set

    var passwordText by mutableStateOf("")
        private set

    var showPasswordToggle by mutableStateOf(false)
        private set


    var usernameError by mutableStateOf("")
        private set


    var passwordError by mutableStateOf("")
        private set

    fun setUsername(username: String) {
        usernameText = username
    }

    fun setPassword(password: String) {
        passwordText = password
    }

    fun showPasswordToggle(showToggle: Boolean) {
        showPasswordToggle = showToggle
    }

    fun setIsUsernameError(error: String) {
        usernameError = error
    }

    fun setIsPasswordError(error: String) {
        passwordError = error
    }


}