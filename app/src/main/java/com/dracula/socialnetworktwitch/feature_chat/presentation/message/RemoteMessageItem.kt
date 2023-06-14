package com.dracula.socialnetworktwitch.feature_chat.presentation.message

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.dracula.socialnetworktwitch.core.presentation.theme.PaddingMedium
import com.dracula.socialnetworktwitch.core.presentation.theme.SpaceLarge

@Composable
fun RemoteMessageItem(
    message: String,
    formattedTime: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.surface,
) {
    Row(
        modifier = modifier.fillMaxWidth()
    ) {

        Box(
            modifier = Modifier
                .weight(1f)
                .background(
                    color = color,
                    shape = MaterialTheme.shapes.medium
                )
                .padding(PaddingMedium)
        ) {
            Text(text = message)

        }
        Spacer(modifier = Modifier.width(SpaceLarge))
        Text(
            text = formattedTime,
            modifier = Modifier.align(Alignment.Bottom),
            style = MaterialTheme.typography.body1.copy(
                color = MaterialTheme.colors.surface,
                fontSize = 12.sp
            )
        )
    }

}