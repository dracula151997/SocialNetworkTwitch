package com.dracula.socialnetworktwitch.feature_auth.presentation.register

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dracula.socialnetworktwitch.R
import com.dracula.socialnetworktwitch.core.presentation.utils.states.PasswordTextFieldState
import com.dracula.socialnetworktwitch.core.presentation.utils.states.StandardTextFieldState
import com.dracula.socialnetworktwitch.core.utils.ApiResult
import com.dracula.socialnetworktwitch.core.utils.Constants
import com.dracula.socialnetworktwitch.core.utils.UiText
import com.dracula.socialnetworktwitch.core.utils.orUnknownError
import com.dracula.socialnetworktwitch.feature_auth.domain.use_case.RegisterUseCase
import com.dracula.socialnetworktwitch.feature_auth.domain.utils.AuthError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    var usernameState by mutableStateOf(StandardTextFieldState())
        private set
    var emailState by mutableStateOf(StandardTextFieldState())
        private set
    var passwordState by mutableStateOf(PasswordTextFieldState())
        private set

    var registerState by mutableStateOf(RegisterState())
        private set

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()


    fun onEvent(event: RegisterEvent) {
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
                registerIfNoErrors()
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
        usernameState = usernameState.copy(
            error = null
        )

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

        emailState = emailState.copy(
            error = null
        )

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

        passwordState = passwordState.copy(
            error = null
        )


    }

    private fun registerIfNoErrors() {
        if (emailState.error != null && usernameState.error != null && passwordState.error != null)
            return

        viewModelScope.launch {
            registerState = RegisterState.loading()
            val result = registerUseCase(
                email = emailState.text,
                username = usernameState.text,
                password = passwordState.text
            )
            registerState = when (result) {
                is ApiResult.Success -> {
                    _eventFlow.emit(UiEvent.SnackbarEvent(UiText.StringResource(R.string.success_registeration)))
                    RegisterState.success()
                }

                is ApiResult.Error -> {
                    _eventFlow.emit(UiEvent.SnackbarEvent(result.uiText.orUnknownError()))
                    RegisterState.error(result.uiText)
                }
            }
        }
    }

    sealed interface UiEvent {
        data class SnackbarEvent(val uiText: UiText) : UiEvent
    }
}