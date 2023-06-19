package com.dracula.socialnetworktwitch.feature_auth.presentation.register

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.dracula.socialnetworktwitch.R
import com.dracula.socialnetworktwitch.core.presentation.utils.BaseViewModel
import com.dracula.socialnetworktwitch.core.presentation.utils.states.validator.EmailFieldState
import com.dracula.socialnetworktwitch.core.presentation.utils.states.validator.NonEmptyFieldState
import com.dracula.socialnetworktwitch.core.presentation.utils.states.validator.PasswordFieldState
import com.dracula.socialnetworktwitch.core.utils.ApiResult
import com.dracula.socialnetworktwitch.core.utils.orUnknownError
import com.dracula.socialnetworktwitch.feature_auth.domain.use_case.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : BaseViewModel<RegisterState, RegisterEvent>() {

    var usernameState by mutableStateOf(NonEmptyFieldState())
        private set
    var emailState by mutableStateOf(EmailFieldState())
        private set
    var passwordState by mutableStateOf(PasswordFieldState())
        private set

    val enableRegisterButton by derivedStateOf {
        (usernameState.isValid && emailState.isValid && passwordState.isValid) && !viewState.isLoading
    }


    override fun initialState(): RegisterState {
        return RegisterState()
    }


    override fun onEvent(event: RegisterEvent) {
        when (event) {
            RegisterEvent.Register -> {
                register()
            }

        }
    }

    private fun register() {
        viewModelScope.launch {
            setState {
                copy(isLoading = true)
            }
            val registerResult = registerUseCase(
                email = emailState.text,
                username = usernameState.text,
                password = passwordState.text
            )
            setState {
                copy(isLoading = false)
            }
            when (registerResult) {
                is ApiResult.Success -> {
                    showSnackbar(R.string.success_registeration)
                    navigateUp()
                }

                is ApiResult.Error -> {
                    showSnackbar(registerResult.uiText.orUnknownError())
                }
            }
        }
    }
}

