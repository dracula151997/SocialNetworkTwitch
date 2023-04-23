package com.dracula.socialnetworktwitch.feature_auth.presentation.register

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.dracula.socialnetworktwitch.core.presentation.utils.states.PasswordTextFieldState
import com.dracula.socialnetworktwitch.core.presentation.utils.states.StandardTextFieldState
import com.dracula.socialnetworktwitch.core.utils.Constants
import com.dracula.socialnetworktwitch.feature_auth.domain.AuthError
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor() : ViewModel() {

    var usernameState by mutableStateOf(StandardTextFieldState())
        private set
    var emailState by mutableStateOf(StandardTextFieldState())
        private set
    var passwordState by mutableStateOf(PasswordTextFieldState())
        private set


    fun onEvent(event: RegisterEvent) {
        Timber.d("current event: $event")
        when (event) {
            is RegisterEvent.OnEmailEntered -> emailState = emailState.copy(
                text = event.value,
            )

            is RegisterEvent.OnPasswordEntered -> passwordState = passwordState.copy(
                text = event.value,
            )

            is RegisterEvent.OnUserNameEntered -> usernameState = usernameState.copy(
                text = event.value
            )

            RegisterEvent.TogglePasswordVisibility -> passwordState = passwordState.copy(
                isPasswordToggleVisible = !passwordState.isPasswordToggleVisible
            )

            RegisterEvent.Register -> {
                validateUsername(usernameState.text)
                validateEmail(emailState.text)
                validatePassword(passwordState.text)
            }

        }
    }

    private fun validateUsername(username: String) {
        val trimmedUsername = username.trim()
        if (trimmedUsername.isBlank()) {
            usernameState = usernameState.copy(
                error = AuthError.FieldEmpty
            )
            return
        }
        if (trimmedUsername.length < Constants.MIN_USERNAME_LENGTH) {
            usernameState = usernameState.copy(
                error = AuthError.InputTooShort
            )
            return
        }

    }

    private fun validateEmail(email: String) {
        val trimmedEmail = email.trim()
        if (trimmedEmail.isBlank()) {
            emailState = emailState.copy(
                error = AuthError.FieldEmpty
            )
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailState = emailState.copy(
                error = AuthError.InvalidEmail
            )
            return
        }

    }

    private fun validatePassword(password: String) {
        if (password.isBlank()) {
            passwordState = passwordState.copy(
                error = AuthError.FieldEmpty
            )
            return
        }
        if (password.length < Constants.MIN_USERNAME_LENGTH) {
            passwordState = passwordState.copy(
                error = AuthError.InputTooShort
            )
            return
        }
        val capitalLetterInPassword = password.any { it.isUpperCase() }
        val numberInPassword = password.any { it.isDigit() }
        if (!capitalLetterInPassword || !numberInPassword) {
            passwordState = passwordState.copy(
                error = AuthError.InvalidPassword
            )
            return
        }


    }
}