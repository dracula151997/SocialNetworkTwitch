package com.dracula.socialnetworktwitch.feature_auth.presentation.login

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dracula.socialnetworktwitch.core.presentation.utils.Screens
import com.dracula.socialnetworktwitch.core.presentation.utils.states.validator.EmailFieldState
import com.dracula.socialnetworktwitch.core.presentation.utils.states.validator.NonEmptyFieldState
import com.dracula.socialnetworktwitch.core.utils.ApiResult
import com.dracula.socialnetworktwitch.core.utils.UiEvent
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
    var emailState by mutableStateOf(EmailFieldState())
        private set

    var passwordState by mutableStateOf(NonEmptyFieldState())
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
            state = state.copy(isLoading = true)
            val response = loginUseCase(email = emailState.text, password = passwordState.text)
            state = state.copy(isLoading = false)
            when (response) {
                is ApiResult.Success -> {
                    _eventFlow.emit(
                        UiEvent.Navigate(route = Screens.MainFeedScreen.route)
                    )
                }

                is ApiResult.Error -> {
                    _eventFlow.emit(
                        UiEvent.ShowSnackbar(response.uiText.orUnknownError())
                    )
                }
            }
        }
    }
}



