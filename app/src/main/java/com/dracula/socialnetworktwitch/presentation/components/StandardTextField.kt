package com.dracula.socialnetworktwitch.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import com.dracula.socialnetworktwitch.presentation.ui.Semantics

@Composable
fun StandardTextField(
    text: String,
    hint: String = "",
    error: String = "",
    maxLength: Int = 5,
    keyboardType: KeyboardType = KeyboardType.Text,
    showPasswordToggle: Boolean = false,
    onPasswordToggleClicked: (Boolean) -> Unit = {},
    onValueChanged: (newValue: String) -> Unit
) {
    val isPasswordToggleDisplayed by remember {
        mutableStateOf(
            keyboardType == KeyboardType.Password
                    || keyboardType == KeyboardType.NumberPassword
        )
    }
    Column(modifier = Modifier.fillMaxWidth()) {
        TextField(
            value = text,
            onValueChange = {
                onValueChanged(it.take(maxLength))
            },
            singleLine = true,
            visualTransformation =
            if (isPasswordToggleDisplayed && !showPasswordToggle)
                PasswordVisualTransformation()
            else
                VisualTransformation.None,
            trailingIcon = {
                if (isPasswordToggleDisplayed)
                    IconButton(
                        onClick = {
                            onPasswordToggleClicked(!showPasswordToggle)
                        },
                        modifier = Modifier.semantics {
                            testTag = Semantics.TestTags.PASSWORD_TOGGLE
                        },
                    ) {
                        Icon(
                            imageVector = if (showPasswordToggle)
                                Icons.Filled.VisibilityOff
                            else
                                Icons.Filled.Visibility,
                            contentDescription = if (showPasswordToggle)
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
            isError = error.isNotEmpty(),
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType
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