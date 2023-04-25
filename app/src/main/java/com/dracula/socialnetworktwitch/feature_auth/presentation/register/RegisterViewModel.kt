package com.dracula.socialnetworktwitch.feature_auth.presentation.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dracula.socialnetworktwitch.R
import com.dracula.socialnetworktwitch.core.presentation.utils.states.PasswordTextFieldState
import com.dracula.socialnetworktwitch.core.presentation.utils.states.StandardTextFieldState
import com.dracula.socialnetworktwitch.core.utils.ApiResult
import com.dracula.socialnetworktwitch.core.utils.UiText
import com.dracula.socialnetworktwitch.core.utils.orUnknownError
import com.dracula.socialnetworktwitch.feature_auth.domain.use_case.RegisterUseCase
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
                register()
            }

        }
    }

    private fun register() {
        viewModelScope.launch {
            clearTextFieldErrorState()
            registerState = RegisterState.loading()
            val registerResult = registerUseCase(
                email = emailState.text,
                username = usernameState.text,
                password = passwordState.text
            )
            if (registerResult.hasEmailError)
                emailState = emailState.copy(
                    error = registerResult.emailError
                )
            if (registerResult.hasUsernameError)
                usernameState = usernameState.copy(
                    error = registerResult.usernameError
                )
            if (registerResult.hasPasswordError)
                passwordState = passwordState.copy(
                    error = registerResult.passwordError
                )
            when (val result = registerResult.result) {
                is ApiResult.Success -> {
                    _eventFlow.emit(UiEvent.SnackbarEvent(UiText.StringResource(R.string.success_registeration)))
                    registerState = RegisterState.success()
                    clearTextFieldStates()
                }

                is ApiResult.Error -> {
                    _eventFlow.emit(UiEvent.SnackbarEvent(result.uiText.orUnknownError()))
                    registerState = RegisterState.error(result.uiText.orUnknownError())
                }

                else -> {
                    registerState = RegisterState.idle()
                }
            }
        }
    }

    private fun clearTextFieldStates() {
        usernameState = StandardTextFieldState()
        emailState = StandardTextFieldState()
        passwordState = PasswordTextFieldState()
    }

    private fun clearTextFieldErrorState() {
        usernameState = usernameState.copy(error = null)
        emailState = emailState.copy(error = null)
        passwordState = passwordState.copy(error = null)
    }

    sealed interface UiEvent {
        data class SnackbarEvent(val uiText: UiText) : UiEvent
    }
}