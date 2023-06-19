package com.dracula.socialnetworktwitch.feature_auth.presentation.login

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.dracula.socialnetworktwitch.core.presentation.utils.BaseViewModel
import com.dracula.socialnetworktwitch.core.presentation.utils.Screens
import com.dracula.socialnetworktwitch.core.presentation.utils.states.validator.EmailFieldState
import com.dracula.socialnetworktwitch.core.presentation.utils.states.validator.NonEmptyFieldState
import com.dracula.socialnetworktwitch.core.utils.ApiResult
import com.dracula.socialnetworktwitch.core.utils.orUnknownError
import com.dracula.socialnetworktwitch.feature_auth.domain.use_case.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : BaseViewModel<LoginState, LoginEvent>() {
    var emailState by mutableStateOf(EmailFieldState())
        private set

    var passwordState by mutableStateOf(NonEmptyFieldState())
        private set

    val enableLoginButton by derivedStateOf {
        emailState.isValid && passwordState.isValid
    }

    var state by mutableStateOf(LoginState())
        private set

    override fun initialState(): LoginState {
        return LoginState()
    }

    override fun onEvent(event: LoginEvent) {
        when (event) {
            LoginEvent.Login -> login()
        }
    }

    private fun login() {
        viewModelScope.launch {
            setState {
                copy(
                    isLoading = true
                )
            }
            val response = loginUseCase(email = emailState.text, password = passwordState.text)
            setState { copy(isLoading = false) }
            when (response) {
                is ApiResult.Success -> {
                    navigate(route = Screens.MainFeedScreen.route)
                }

                is ApiResult.Error -> {
                    showSnackbar(response.uiText.orUnknownError())
                }
            }
        }
    }
}



