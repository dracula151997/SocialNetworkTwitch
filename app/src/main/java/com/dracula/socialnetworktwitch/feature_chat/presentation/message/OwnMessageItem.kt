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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.dracula.socialnetworktwitch.core.presentation.theme.PaddingMedium
import com.dracula.socialnetworktwitch.core.presentation.theme.SpaceLarge

@Composable
fun OwnMessageItem(
    message: String,
    formattedTime: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.primary,
    triangleWidth: Dp = 30.dp,
    triangleHeight: Dp = 30.dp
) {
    Row(
        modifier = modifier.fillMaxWidth()
    ) {
        val bottomStartCornerRadius = MaterialTheme.shapes.medium.bottomEnd
        Text(
            text = formattedTime,
            color = MaterialTheme.colors.surface,
            modifier = Modifier.align(Alignment.Bottom)
        )
        Spacer(modifier = Modifier.width(SpaceLarge))
        Box(
            modifier = Modifier
                .weight(1f)
                .background(
                    color = color,
                    shape = MaterialTheme.shapes.medium
                )
                .padding(PaddingMedium)
                .drawBehind {
                    val path = Path().apply {
                        moveTo(
                            size.width,
                            size.height - bottomStartCornerRadius.toPx(
                                shapeSize = size,
                                density = Density(density)
                            )
                        )
                        lineTo(size.width, size.height + triangleHeight.toPx())
                        lineTo(
                            size.width - triangleWidth.toPx(),
                            size.height - bottomStartCornerRadius.toPx(
                                shapeSize = size,
                                density = Density(density)
                            )
                        )
                        close()
                    }
                    drawPath(path = path, color = color)
                }
        ) {
            Text(text = message, color = MaterialTheme.colors.onPrimary)

        }

    }

}