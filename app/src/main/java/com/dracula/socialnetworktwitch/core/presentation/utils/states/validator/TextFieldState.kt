package com.dracula.socialnetworktwitch.core.presentation.utils.states.validator

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.dracula.socialnetworktwitch.core.utils.UiText

open class TextFieldState(
    private val validator: (String) -> Boolean = { true },
    private val errorFor: (String) -> UiText = { UiText.DynamicString("") }
) {
    var text by mutableStateOf("")
    private var isFocusedDirty by mutableStateOf(false)
    private var isFocused by mutableStateOf(false)
    private var displayErrors by mutableStateOf(false)

    open val isValid get() = validator(text)

    fun onFocusChange(focused: Boolean) {
        isFocused = focused
        if (focused) isFocusedDirty = true
    }

    fun onValueChange(newText: String) {
        text = newText
    }

    fun clearText() {
        text = ""
    }

    fun isEmpty(): Boolean {
        return text.isEmpty()
    }

    fun enableShowErrors() {
        if (isFocusedDirty)
            displayErrors = true
    }

    fun showErrors() = !isValid && displayErrors

    open fun getError(): UiText? {
        return if (showErrors())
            errorFor(text)
        else null
    }
}

