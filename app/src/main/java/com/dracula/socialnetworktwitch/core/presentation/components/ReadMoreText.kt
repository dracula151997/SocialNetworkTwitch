package com.dracula.socialnetworktwitch.core.presentation.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
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
    readLessText: String = stringResource(id = R.string.read_less),
    readMoreTextColor: Color = MaterialTheme.colors.primary,
) {
    val isExpanded = remember {
        mutableStateOf(false)
    }
    val textLayoutResultState = remember { mutableStateOf<TextLayoutResult?>(null) }
    val finalText = remember { mutableStateOf(text) }
    val readMoreText = remember {
        mutableStateOf("Read More")
    }

    val textLayoutResult = textLayoutResultState.value
    LaunchedEffect(key1 = textLayoutResult) {
        if (textLayoutResult == null) return@LaunchedEffect

        when {
            isExpanded.value -> {
                readMoreText.value = "Read less"
            }

            !isExpanded.value && textLayoutResult.hasVisualOverflow -> {
                readMoreText.value = "Read more"
                val lastCharIndex = textLayoutResult.getLineEnd(maxLines - 1)
                val adjustedText = text.substring(startIndex = 0, endIndex = lastCharIndex)
                    .dropLast(readMoreText.value.length)
                    .dropLastWhile { it == ' ' || it == '.' }

                finalText.value = adjustedText
            }

            else -> {
                finalText.value = text
                readMoreText.value = ""
            }
        }
    }

    Column(
        modifier = Modifier.animateContentSize()
    ) {
        Text(
            text = finalText.value,
            maxLines = if (isExpanded.value) Int.MAX_VALUE else maxLines,
            overflow = TextOverflow.Ellipsis,
            modifier = modifier,
            onTextLayout = {
                textLayoutResultState.value = it
            }

        )


        ClickableText(
            text = buildAnnotatedString {
                append(readMoreText.value)
                addStyle(
                    style = SpanStyle(
                        color = readMoreTextColor, textDecoration = TextDecoration.Underline
                    ), start = 0, end = readMoreText.value.length
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
            readMoreTextColor = MaterialTheme.colors.primary
        )
    }
}