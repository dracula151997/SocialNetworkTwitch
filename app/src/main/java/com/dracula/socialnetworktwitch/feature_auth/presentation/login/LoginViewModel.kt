package com.dracula.socialnetworktwitch.feature_auth.presentation.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dracula.socialnetworktwitch.core.presentation.utils.Screens
import com.dracula.socialnetworktwitch.core.presentation.utils.states.PasswordTextFieldState
import com.dracula.socialnetworktwitch.core.presentation.utils.states.StandardTextFieldState
import com.dracula.socialnetworktwitch.core.utils.ApiResult
import com.dracula.socialnetworktwitch.core.utils.UiText
import com.dracula.socialnetworktwitch.core.utils.isNotNull
import com.dracula.socialnetworktwitch.core.utils.orUnknownError
import com.dracula.socialnetworktwitch.feature_auth.domain.use_case.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {
    var emailState by mutableStateOf(StandardTextFieldState())
        private set

    var passwordState by mutableStateOf(PasswordTextFieldState())
        private set

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    var state by mutableStateOf(LoginState())
        private set

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.EmailEntered -> emailState = emailState.copy(text = event.email)
            is LoginEvent.PasswordEntered -> passwordState =
                passwordState.copy(text = event.password)

            LoginEvent.TogglePasswordVisibility -> passwordState = passwordState.copy(
                isPasswordToggleVisible = !passwordState.isPasswordToggleVisible
            )

            LoginEvent.Login -> login()
        }
    }

    private fun login() {
        viewModelScope.launch {
            clearTextFieldErrorStates()
            state = LoginState.loading()
            val loginResult = loginUseCase(email = emailState.text, password = passwordState.text)

            if (loginResult.emailError.isNotNull())
                emailState = emailState.copy(error = loginResult.emailError)
            if (loginResult.passwordError.isNotNull())
                passwordState = passwordState.copy(error = loginResult.passwordError)

            when (val result = loginResult.result) {
                is ApiResult.Success -> {
                    state = LoginState.success()
                    _eventFlow.emit(
                        UiEvent.Navigate(route = Screens.MainFeedScreen.route)
                    )
                }

                is ApiResult.Error -> {
                    state = LoginState.error(result.uiText.orUnknownError())
                    _eventFlow.emit(
                        UiEvent.SnackbarEvent(message = result.uiText.orUnknownError())
                    )
                }

                null -> {
                    state = LoginState.idle()
                }
            }
        }
    }

    private fun clearTextFieldErrorStates() {
        emailState = emailState.copy(error = null)
        passwordState = passwordState.copy(error = null)
    }

    sealed interface UiEvent {
        data class SnackbarEvent(val message: UiText) : UiEvent
        data class Navigate(val route: String) : UiEvent
    }
}