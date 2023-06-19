package com.dracula.socialnetworktwitch.core.presentation.utils

import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.dracula.socialnetworktwitch.core.utils.UiText
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

abstract class BaseViewModel<State : UiState, Event : UiEvent> : ViewModel() {
    private val _effectFlow = MutableSharedFlow<UiEffect>()
    val eventFlow = _effectFlow.asSharedFlow()

    private val initialState: State by lazy { initialState() }

    abstract fun initialState(): State

    var viewState by mutableStateOf(initialState)
        private set

    abstract fun onEvent(event: Event)

    protected fun setState(reducer: State.() -> State) {
        val newState = viewState.reducer()
        viewState = newState
    }

    protected suspend fun setEffect(builder: () -> UiEffect) {
        val effect = builder()
        _effectFlow.emit(effect)
    }


    protected suspend fun showSnackbar(uiText: UiText) {
        setEffect {
            CommonUiEffect.ShowSnackbar(uiText = uiText)
        }
    }

    protected suspend fun showSnackbar(@StringRes messageResId: Int) {
        _effectFlow.emit(
            CommonUiEffect.ShowSnackbar(
                UiText.StringResource(messageResId)
            )
        )
    }

    protected suspend fun showSnackbar(message: String) {
        _effectFlow.emit(
            CommonUiEffect.ShowSnackbar(
                UiText.DynamicString(message)
            )
        )
    }

    protected suspend fun showSnackbar(@StringRes messageResId: Int, vararg formatArgs: Any) {
        _effectFlow.emit(
            CommonUiEffect.ShowSnackbar(
                UiText.StringResourceWithArgs(messageResId, *formatArgs)
            )
        )
    }

    protected suspend fun navigate(route: String) {
        setEffect { CommonUiEffect.Navigate(route) }
    }

    protected suspend fun navigateUp() {
        setEffect { CommonUiEffect.NavigateUp }
    }


}