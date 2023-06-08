package com.dracula.socialnetworktwitch.core.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.AlertDialog
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.DialogProperties

@Composable
fun StandardAlertDialog(
    message: String,
    positiveButtonText: String,
    negativeButtonText: String?,
    title: String? = null,
    onDismissed: () -> Unit = {},
    dismissOnBack: Boolean = true,
    dismissOnTouchOutside: Boolean = true,
    onPositiveButtonClicked: () -> Unit = {},
    onNegativeButtonClicked: () -> Unit = {}
) {
    AlertDialog(
        onDismissRequest = onDismissed,
        properties = DialogProperties(
            dismissOnBackPress = dismissOnBack,
            dismissOnClickOutside = dismissOnTouchOutside,
        ),
        title = {
            if (!title.isNullOrEmpty()) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.body1
                )
            }
        },
        text = {
            Text(
                text = message,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        buttons = {
            Row(
                horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()
            ) {
                if (!negativeButtonText.isNullOrEmpty())
                    TextButton(
                        onClick = onNegativeButtonClicked,
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colors.primary
                        )
                    ) {
                        Text(text = negativeButtonText)
                    }

                TextButton(
                    onClick = onPositiveButtonClicked,
                ) {
                    Text(text = positiveButtonText)
                }
            }


        })

}