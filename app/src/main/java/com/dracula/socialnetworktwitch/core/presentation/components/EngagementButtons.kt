package com.dracula.socialnetworktwitch.core.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.dracula.socialnetworktwitch.core.presentation.Semantics
import com.dracula.socialnetworktwitch.core.presentation.theme.TextWhite


@Composable
@Preview(showBackground = true)
fun EngagementButtons(
    modifier: Modifier = Modifier,
    isLiked: Boolean = false,
    iconSize: Dp = 30.dp,
    onLikeClicked: (Boolean) -> Unit = {},
    onCommentClicked: () -> Unit = {},
    onShareClicked: () -> Unit = {}
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        IconButton(onClick = { onLikeClicked(!isLiked) }) {
            Icon(
                imageVector = Icons.Filled.Favorite,
                contentDescription =
                if (isLiked)
                    Semantics.ContentDescriptions.UNLIKE
                else
                    Semantics.ContentDescriptions.LIKE,
                modifier = Modifier.size(iconSize),
                tint = if (isLiked) Color.Red else TextWhite
            )
        }

        IconButton(onClick = onCommentClicked) {
            Icon(
                imageVector = Icons.Filled.Comment,
                contentDescription = Semantics.ContentDescriptions.COMMENT,
                modifier = Modifier.size(iconSize)

            )
        }
        IconButton(onClick = onShareClicked) {
            Icon(
                imageVector = Icons.Filled.Share,
                contentDescription = Semantics.ContentDescriptions.SHARE,
                modifier = Modifier.size(iconSize)

            )
        }


    }
}

@Composable
fun PostActionRow(
    username: String,
    modifier: Modifier = Modifier,
    isLiked: Boolean = false,
    onCommentClicked: () -> Unit = {},
    onShareClicked: () -> Unit = {},
    onLikeClicked: (Boolean) -> Unit = {},
    onUsernameClicked: (String) -> Unit = {},

    ) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = username,
            color = MaterialTheme.colors.primary,
            style = TextStyle(
                fontWeight = FontWeight.Bold,
            ),
            modifier = Modifier.clickable { onUsernameClicked(username) }
        )
        EngagementButtons(
            isLiked = isLiked,
            onCommentClicked = onCommentClicked,
            onLikeClicked = onLikeClicked,
            onShareClicked = onShareClicked,
            iconSize = 24.dp,
            modifier = Modifier.wrapContentWidth()

        )

    }
}