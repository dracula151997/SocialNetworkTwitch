package com.dracula.socialnetworktwitch.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dracula.socialnetworktwitch.presentation.ui.theme.HintGray
import com.dracula.socialnetworktwitch.presentation.ui.theme.PaddingSmall

@Composable
fun RowScope.StandardBottomNavItem(
    icon: ImageVector?,
    selected: Boolean,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    alertCount: Int? = null,
    selectedColor: Color = MaterialTheme.colors.primary,
    unselectedColor: Color = HintGray,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    alertCount?.let {
        require(it >= 0) {
            "Alert count cannot to be negative"
        }
    }
    val lineWidth by animateFloatAsState(
        targetValue = if (selected) 1f else 0f,
        animationSpec = tween(
            durationMillis = 200,
        )
    )

    BottomNavigationItem(selected = selected,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        selectedContentColor = selectedColor,
        unselectedContentColor = unselectedColor,
        icon = {
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(PaddingSmall)
                .drawBehind {
                    if (selected) drawLine(
                        color = selectedColor,
                        start = Offset(
                            x = size.width / 2 - lineWidth * 15.dp.toPx(),
                            y = size.height
                        ),
                        end = Offset(
                            x = size.width / 2 + lineWidth * 15.dp.toPx(),
                            y = size.height
                        ),
                        strokeWidth = 2.dp.toPx(),
                        cap = StrokeCap.Round,
                    )
                }) {
                BadgedBox(
                    modifier = Modifier.align(Alignment.Center),
                    badge = {
                        if (alertCount != null) {
                            Badge(
                                backgroundColor = if (selected) selectedColor else unselectedColor,
                                contentColor = if (selected) MaterialTheme.colors.onPrimary else Color.White
                            ) {
                                val alertText =
                                    if (alertCount > 99) "+99" else alertCount.toString()
                                Text(
                                    text = alertText,
                                    fontWeight = FontWeight.Bold,
                                    color = if (selected) MaterialTheme.colors.onPrimary else Color.White
                                )
                            }
                        }
                    },
                ) {
                    if (icon != null) Icon(
                        imageVector = icon,
                        contentDescription = contentDescription,
                        modifier = Modifier.align(
                            Alignment.Center
                        )
                    )

                }
            }
        })

}