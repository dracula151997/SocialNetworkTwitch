package com.dracula.socialnetworktwitch.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.dracula.socialnetworktwitch.presentation.ui.Semantics

@Composable
fun StandardTextField(
    text: String,
    hint: String = "",
    isError: Boolean = false,
    maxLength: Int = 5,
    keyboardType: KeyboardType = KeyboardType.Text,
    onValueChanged: (newValue: String) -> Unit
) {
    val isPasswordToggleDisplayed by remember {
        mutableStateOf(
            keyboardType == KeyboardType.Password
                    || keyboardType == KeyboardType.NumberPassword
        )
    }
    var isPasswordVisible by remember {
        mutableStateOf(false)
    }
    TextField(
        value = text,
        onValueChange = {
            onValueChanged(it.take(maxLength))
        },
        singleLine = true,
        visualTransformation =
        if (isPasswordToggleDisplayed && !isPasswordVisible)
            PasswordVisualTransformation()
        else
            VisualTransformation.None,
        trailingIcon = {
            if (isPasswordToggleDisplayed)
                IconButton(onClick = {
                    isPasswordVisible = !isPasswordVisible
                }, modifier = Modifier.semantics {
                    testTag = Semantics.TestTags.PASSWORD_TOGGLE
                }) {
                    Icon(
                        imageVector = if (isPasswordVisible)
                            Icons.Filled.VisibilityOff
                        else
                            Icons.Filled.Visibility,
                        contentDescription = if (isPasswordVisible)
                            Semantics.ContentDescriptions.HIDE_PASSWORD_ICON
                        else
                            Semantics.ContentDescriptions.SHOW_PASSWORD_ICON
                    )
                }
        },
        placeholder = {
            Text(
                text = hint,
                style = MaterialTheme.typography.body1
            )
        },
        isError = isError,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        ),
        modifier = Modifier
            .fillMaxWidth()
            .semantics {
                testTag = Semantics.TestTags.STANDARD_TEXT_FIELD
            }
    )
}