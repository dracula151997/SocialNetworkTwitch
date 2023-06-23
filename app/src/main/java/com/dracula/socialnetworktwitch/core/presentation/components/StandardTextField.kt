package com.dracula.socialnetworktwitch.core.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dracula.socialnetworktwitch.R
import com.dracula.socialnetworktwitch.core.presentation.Semantics
import com.dracula.socialnetworktwitch.core.presentation.theme.IconSizeMedium
import com.dracula.socialnetworktwitch.core.presentation.theme.LightGray
import com.dracula.socialnetworktwitch.core.presentation.utils.states.validator.TextFieldState

@Composable
fun StandardTextField(
    text: String,
    modifier: Modifier = Modifier,
    hint: String = "",
    error: String = "",
    maxLength: Int = Int.MAX_VALUE,
    maxLines: Int = Int.MAX_VALUE,
    singleLine: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    showPasswordToggle: Boolean = false,
    leadingIcon: ImageVector? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    imeAction: ImeAction = ImeAction.Next,
    isPasswordToggleDisplayed: Boolean = keyboardType == KeyboardType.Password,
    onPasswordToggleClicked: (Boolean) -> Unit = {},
    onValueChanged: (newValue: String) -> Unit
) {
    Column(modifier = modifier.fillMaxWidth()) {
        TextField(
            value = text,
            onValueChange = {
                onValueChanged(it.take(maxLength))
            },
            singleLine = singleLine,
            maxLines = maxLines,
            visualTransformation =
            if (isPasswordToggleDisplayed && !showPasswordToggle)
                PasswordVisualTransformation()
            else
                VisualTransformation.None,
            trailingIcon = if (isPasswordToggleDisplayed) {
                val passwordToggleButton: @Composable () -> Unit = {
                    TogglePasswordButton(
                        showPasswordToggle = showPasswordToggle,
                        onPasswordToggleClicked = {
                            onPasswordToggleClicked(!showPasswordToggle)
                        }
                    )

                }
                passwordToggleButton
            } else trailingIcon,
            placeholder = {
                Text(
                    text = hint,
                    style = MaterialTheme.typography.body1,
                    color = LightGray
                )
            },
            keyboardActions = keyboardActions,
            leadingIcon = if (leadingIcon != null) {
                val icon: @Composable () -> Unit = {
                    Icon(
                        imageVector = leadingIcon,
                        contentDescription = null,
                        tint = MaterialTheme.colors.onBackground,
                        modifier = Modifier.size(IconSizeMedium)
                    )
                }
                icon
            } else null,
            isError = error.isNotEmpty(),
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = imeAction
            ),
            modifier = Modifier
                .fillMaxWidth()
                .semantics {
                    testTag = Semantics.TestTags.STANDARD_TEXT_FIELD
                }
        )
        if (error.isNotEmpty()) {
            Text(
                text = error,
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.error,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun StandardTextField(
    state: TextFieldState,
    modifier: Modifier = Modifier,
    hint: String = "",
    doOnValueChanged: (newValue: String) -> Unit = {},
    maxLength: Int = Int.MAX_VALUE,
    maxLines: Int = Int.MAX_VALUE,
    singleLine: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    leadingIcon: ImageVector? = null,
    trailingIcon: @Composable() (() -> Unit)? = null,
    imeAction: ImeAction = ImeAction.Next,
) {
    TextField(
        value = state.text,
        onValueChange = {
            state.onValueChange(it.take(maxLength))
            doOnValueChanged(it)
        },
        singleLine = singleLine,
        maxLines = maxLines,
        trailingIcon = trailingIcon,
        placeholder = {
            Text(
                text = hint,
                style = MaterialTheme.typography.body1,
                color = LightGray
            )
        },
        keyboardActions = keyboardActions,
        leadingIcon = if (leadingIcon != null) {
            val icon: @Composable () -> Unit = {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    tint = MaterialTheme.colors.onBackground,
                    modifier = Modifier.size(IconSizeMedium)
                )
            }
            icon
        } else null,
        isError = state.showErrors(),
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction
        ),
        modifier = modifier
            .fillMaxWidth()
            .onFocusChanged { focusState ->
                state.onFocusChange(focusState.isFocused)
                if (!focusState.isFocused)
                    state.enableShowErrors()
            }
            .semantics {
                testTag = Semantics.TestTags.STANDARD_TEXT_FIELD
            }
    )

    state.getError()
        ?.let { error -> TextFieldError(textError = error.asString(context = LocalContext.current)) }
}

@Composable
fun PasswordTextField(
    state: TextFieldState,
    hint: String = "",
    maxLength: Int = Int.MAX_VALUE,
    maxLines: Int = Int.MAX_VALUE,
    singleLine: Boolean = true,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    imeAction: ImeAction = ImeAction.Next
) {
    var showPassword by rememberSaveable {
        mutableStateOf(false)
    }
    TextField(
        value = state.text,
        onValueChange = {
            state.onValueChange(it.take(maxLength))
        },
        singleLine = singleLine,
        maxLines = maxLines,
        visualTransformation =
        if (!showPassword)
            PasswordVisualTransformation()
        else
            VisualTransformation.None,

        trailingIcon = {
            if (showPassword)
                IconButton(onClick = { showPassword = false }) {
                    Icon(
                        imageVector = Icons.Filled.Visibility,
                        contentDescription = stringResource(id = R.string.hide_password)
                    )
                }
            else
                IconButton(onClick = { showPassword = true }) {
                    Icon(
                        imageVector = Icons.Filled.VisibilityOff,
                        contentDescription = stringResource(id = R.string.show_password)
                    )
                }

        },
        placeholder = {
            Text(
                text = hint,
                style = MaterialTheme.typography.body1,
                color = LightGray
            )
        },
        keyboardActions = keyboardActions,
        isError = state.showErrors(),
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = imeAction,
            keyboardType = KeyboardType.Password
        ),
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged { focusState ->
                state.onFocusChange(focusState.isFocused)
                if (!focusState.isFocused)
                    state.enableShowErrors()
            }
            .semantics {
                testTag = Semantics.TestTags.STANDARD_TEXT_FIELD
            }
    )

    state.getError()
        ?.let { error -> TextFieldError(textError = error.asString(context = LocalContext.current)) }
}

@Composable
private fun TextFieldError(textError: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = textError,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            color = MaterialTheme.colors.error
        )
    }
}

