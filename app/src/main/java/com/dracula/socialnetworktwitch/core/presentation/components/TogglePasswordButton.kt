package com.dracula.socialnetworktwitch.core.presentation.components

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import com.dracula.socialnetworktwitch.core.presentation.Semantics

@Composable
fun TogglePasswordButton(
    showPasswordToggle: Boolean,
    onPasswordToggleClicked: () -> Unit,
) {
    IconButton(
        onClick = {
            onPasswordToggleClicked()
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
                Semantics.ContentDescriptions.SHOW_PASSWORD_ICON,
            tint = Color.White
        )
    }

}