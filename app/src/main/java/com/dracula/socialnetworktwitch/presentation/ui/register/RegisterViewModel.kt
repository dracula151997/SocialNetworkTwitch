package com.dracula.socialnetworktwitch.presentation.ui.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor() : ViewModel() {
    var emailText by mutableStateOf("")
        private set

    var usernameText by mutableStateOf("")
        private set

    var passwordText by mutableStateOf("")
        private set

    fun setEmail(email: String){
        emailText = email
    }

    fun setUsername(username: String) {
        usernameText = username
    }

    fun setPassword(password: String) {
        passwordText = password
    }
}