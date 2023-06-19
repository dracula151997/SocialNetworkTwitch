package com.dracula.socialnetworktwitch.feature_auth.presentation.register

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dracula.socialnetworktwitch.R
import com.dracula.socialnetworktwitch.core.presentation.utils.states.validator.EmailFieldState
import com.dracula.socialnetworktwitch.core.presentation.utils.states.validator.NonEmptyFieldState
import com.dracula.socialnetworktwitch.core.presentation.utils.states.validator.PasswordFieldState
import com.dracula.socialnetworktwitch.core.utils.ApiResult
import com.dracula.socialnetworktwitch.core.utils.UiEvent
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

    var usernameState by mutableStateOf(NonEmptyFieldState())
        private set
    var emailState by mutableStateOf(EmailFieldState())
        private set
    var passwordState by mutableStateOf(PasswordFieldState())
        private set

    val enableRegisterButton by derivedStateOf {
        (usernameState.isValid && emailState.isValid && passwordState.isValid) && !registerState.isLoading
    }

    var registerState by mutableStateOf(RegisterState())
        private set

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()


    fun onEvent(event: RegisterAction) {
        when (event) {
            RegisterAction.Register -> {
                register()
            }

        }
    }

    private fun register() {
        viewModelScope.launch {
            registerState = registerState.copy(isLoading = true)
            val registerResult = registerUseCase(
                email = emailState.text,
                username = usernameState.text,
                password = passwordState.text
            )
            registerState = registerState.copy(isLoading = false)
            when (registerResult) {
                is ApiResult.Success -> {
                    _eventFlow.emit(UiEvent.ShowSnackbar(UiText.StringResource(R.string.success_registeration)))
                    _eventFlow.emit(UiEvent.NavigateUp)
                }

                is ApiResult.Error -> {
                    _eventFlow.emit(UiEvent.ShowSnackbar(registerResult.uiText.orUnknownError()))
                }
            }
        }
    }
}

