package com.dracula.socialnetworktwitch.feature_auth.presentation.login

import android.util.Patterns
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dracula.socialnetworktwitch.R
import com.dracula.socialnetworktwitch.core.presentation.utils.Screens
import com.dracula.socialnetworktwitch.core.presentation.utils.states.TextFieldState
import com.dracula.socialnetworktwitch.core.utils.ApiResult
import com.dracula.socialnetworktwitch.core.utils.UiEvent
import com.dracula.socialnetworktwitch.core.utils.UiText
import com.dracula.socialnetworktwitch.core.utils.orUnknownError
import com.dracula.socialnetworktwitch.feature_auth.domain.use_case.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {
    var emailState by mutableStateOf(EmailState())
        private set

    var passwordState by mutableStateOf(PasswordFieldState())
        private set

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    val enableLoginButton by derivedStateOf {
        emailState.isValid && passwordState.isValid
    }

    var state by mutableStateOf(LoginState())
        private set

    fun onEvent(event: LoginAction) {
        when (event) {
            LoginAction.Login -> login()
            else -> {}
        }
    }

    private fun login() {
        viewModelScope.launch {
            state = LoginState.loading()
            val loginResult = loginUseCase(email = emailState.text, password = passwordState.text)

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
                        UiEvent.ShowSnackbar(result.uiText.orUnknownError())
                    )
                }

                null -> {
                    state = LoginState.idle()
                }
            }
        }
    }
}

class EmailState(email: String? = null) : TextFieldState(
    validator = ::isEmailValid,
    errorFor = ::emailValidationError
) {
    init {
        email?.let {
            text = it
        }
    }
}

private fun emailValidationError(email: String): UiText {
    if (email.isEmpty())
        return UiText.StringResource(R.string.error_this_field_cannot_be_empty)

    return UiText.StringResource(R.string.error_not_a_valid_email)
}

private fun isEmailValid(email: String): Boolean {
    return email.isNotEmpty() && Pattern.matches(Patterns.EMAIL_ADDRESS.pattern(), email)
}

class PasswordFieldState(password: String? = null) : TextFieldState(
    validator = ::isPasswordValid,
    errorFor = ::passwordValidationError
) {
    init {
        password?.let {
            text = it
        }
    }
}

private fun passwordValidationError(password: String): UiText {
    return UiText.StringResource(R.string.error_this_field_cannot_be_empty)
}

private fun isPasswordValid(password: String): Boolean {
    return password.isNotEmpty()
}