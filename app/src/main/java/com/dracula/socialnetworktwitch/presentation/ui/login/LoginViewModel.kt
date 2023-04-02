package com.dracula.socialnetworktwitch.presentation.ui.login

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

    fun setUsername(username: String) {
        usernameText = username
    }

    fun setPassword(password: String) {
        passwordText = password
    }


}