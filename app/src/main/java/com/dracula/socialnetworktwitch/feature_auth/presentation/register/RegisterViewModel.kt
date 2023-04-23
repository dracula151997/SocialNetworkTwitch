package com.dracula.socialnetworktwitch.feature_auth.presentation.register

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.dracula.socialnetworktwitch.core.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor() : ViewModel() {

    var state by mutableStateOf(RegisterState())
        private set

    fun onEvent(event: RegisterEvent) {
        Timber.d("current event: $event")
        when (event) {
            is RegisterEvent.OnEmailEntered -> state = state.copy(
                emailText = event.value,
                emailError = null
            )

            is RegisterEvent.OnPasswordEntered -> state = state.copy(
                passwordText = event.value,
                passwordError = null
            )

            is RegisterEvent.OnUserNameEntered -> state = state.copy(
                usernameText = event.value,
                usernameError = null
            )

            RegisterEvent.TogglePasswordVisibility -> state = state.copy(
                isPasswordToggleVisible = !state.isPasswordToggleVisible
            )
            RegisterEvent.Register -> {
                validateUsername(state.usernameText)
                validateEmail(state.emailText)
                validatePassword(state.passwordText)
            }

        }
    }

    private fun validateUsername(username: String) {
        val trimmedUsername = username.trim()
        if (trimmedUsername.isBlank()) {
            state = state.copy(
                usernameError = RegisterState.UsernameError.FieldEmpty
            )
            return
        }
        if (trimmedUsername.length < Constants.MIN_USERNAME_LENGTH) {
            state = state.copy(
                usernameError = RegisterState.UsernameError.InputTooShort
            )
            return
        }

    }

    private fun validateEmail(email: String) {
        val trimmedEmail = email.trim()
        if (trimmedEmail.isBlank()) {
            state = state.copy(
                emailError = RegisterState.EmailError.FieldEmpty
            )
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            state = state.copy(
                emailError = RegisterState.EmailError.InvalidEmail
            )
            return
        }

    }

    private fun validatePassword(password: String) {
        if (password.isBlank()) {
            state = state.copy(
                passwordError = RegisterState.PasswordError.FieldEmpty
            )
            return
        }
        if (password.length < Constants.MIN_USERNAME_LENGTH) {
            state = state.copy(
                passwordError = RegisterState.PasswordError.InputTooShort
            )
            return
        }
        val capitalLetterInPassword = password.any { it.isUpperCase() }
        val numberInPassword = password.any { it.isDigit() }
        if (!capitalLetterInPassword || !numberInPassword) {
            state = state.copy(
                passwordError = RegisterState.PasswordError.InvalidPassword
            )
            return
        }


    }
}