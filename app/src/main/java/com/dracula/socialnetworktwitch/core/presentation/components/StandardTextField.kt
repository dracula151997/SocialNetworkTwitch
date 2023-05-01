package com.dracula.socialnetworktwitch.core.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import com.dracula.socialnetworktwitch.core.presentation.Semantics
import com.dracula.socialnetworktwitch.core.presentation.theme.IconSizeMedium
import com.dracula.socialnetworktwitch.core.presentation.theme.LightGray

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