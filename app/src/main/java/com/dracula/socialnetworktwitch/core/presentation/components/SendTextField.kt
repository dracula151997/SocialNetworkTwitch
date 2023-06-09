package com.dracula.socialnetworktwitch.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.ImeAction
import com.dracula.socialnetworktwitch.core.presentation.Semantics
import com.dracula.socialnetworktwitch.core.presentation.theme.PaddingMedium
import com.dracula.socialnetworktwitch.core.presentation.utils.autoMirror
import com.dracula.socialnetworktwitch.core.presentation.utils.clearFocusOnKeyboardDismiss

@Composable
fun SendTextField(
    text: String,
    hint: String,
    onValueChange: (text: String) -> Unit,
    onSend: () -> Unit,
    enabled: Boolean = true,
    error: String? = null,
    focusRequester: FocusRequester = remember {
        FocusRequester()
    },
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .imePadding()
            .padding(PaddingMedium)
            .background(color = MaterialTheme.colors.surface),
        verticalAlignment = Alignment.CenterVertically
    ) {
        StandardTextField(
            text = text,
            onValueChanged = onValueChange,
            hint = hint,
            modifier = Modifier
                .weight(1f)
                .focusRequester(focusRequester)
                .clearFocusOnKeyboardDismiss(),
            imeAction = ImeAction.Send,
            keyboardActions = KeyboardActions(
                onSend = {
                    onSend()
                }
            ),
            error = error.orEmpty()

        )

        IconButton(
            onClick = onSend,
            enabled = enabled
        ) {
            Icon(
                imageVector = Icons.Default.Send,
                contentDescription = Semantics.ContentDescriptions.POST_PHOTO,
                modifier = Modifier.autoMirror(),
                tint = if (!enabled) MaterialTheme.colors.background else MaterialTheme.colors.primary
            )
        }
    }

}