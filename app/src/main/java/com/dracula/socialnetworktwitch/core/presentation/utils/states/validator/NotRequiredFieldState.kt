package com.dracula.socialnetworktwitch.core.presentation.utils.states.validator

import com.dracula.socialnetworktwitch.core.utils.UiText

class NotRequiredFieldState : TextFieldState(
    validator = { true },
    errorFor = { UiText.DynamicString("") }
)