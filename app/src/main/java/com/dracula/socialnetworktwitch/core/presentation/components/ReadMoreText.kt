package com.dracula.socialnetworktwitch.core.presentation.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.dracula.socialnetworktwitch.R
import com.dracula.socialnetworktwitch.core.presentation.theme.SocialNetworkTwitchTheme


@Composable
fun ReadMoreText(
    text: String,
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE,
    readMoreText: String = stringResource(id = R.string.read_more),
    readLessText: String = stringResource(id = R.string.read_less),
    readMoreTextColor: Color = MaterialTheme.colors.primary,
) {
    val isExpanded = remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier.animateContentSize()
    ) {
        Text(
            text = text,
            maxLines = if (isExpanded.value) Int.MAX_VALUE else maxLines,
            overflow = TextOverflow.Ellipsis,
            modifier = modifier
        )
        ClickableText(
            text = buildAnnotatedString {
                append(if (!isExpanded.value) readMoreText else readLessText)
                addStyle(
                    style = SpanStyle(
                        color = readMoreTextColor, textDecoration = TextDecoration.Underline
                    ), start = 0, end = readMoreText.length
                )
            },
            onClick = {
                isExpanded.value = !isExpanded.value
            }
        )
    }

}

@Preview
@Composable
fun ReadMoreTextPreview() {
    SocialNetworkTwitchTheme {
        ReadMoreText(
            text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed vitae elit arcu. Maecenas tincidunt aliquet sapien non laoreet. Quisque dignissim molestie justo, sit amet consequat sem blandit in. Nulla ac fringilla leo. Nunc eget orci quis ipsum aliquam posuere. Aliquam vestibulum ex sit amet velit malesuada, sit amet fringilla arcu facilisis. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae; Pellentesque sem ex, efficitur et massa non, gravida dictum libero. Etiam sit amet mauris et tortor lacinia volutpat. In sit amet sapien massa. Maecenas quis neque nec felis efficitur molestie. Donec sodales erat at pharetra mollis. Proin placerat elementum lacus, vel interdum metus molestie et. Aliquam lacinia tellus vel volutpat fermentum. Vivamus laoreet quam nec ante lacinia, eget semper ipsum lacinia.",
            maxLines = Int.MAX_VALUE,
            readMoreText = stringResource(id = R.string.read_more),
            readMoreTextColor = MaterialTheme.colors.primary
        )
    }
}