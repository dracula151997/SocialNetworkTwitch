package com.dracula.socialnetworktwitch.feature_profile.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.dracula.socialnetworktwitch.R
import com.dracula.socialnetworktwitch.core.presentation.Semantics
import com.dracula.socialnetworktwitch.core.presentation.theme.BannerIconSize
import com.dracula.socialnetworktwitch.core.presentation.theme.PaddingSmall
import com.dracula.socialnetworktwitch.core.presentation.theme.SpaceMedium
import com.dracula.socialnetworktwitch.core.presentation.utils.toPx

@Composable
@Preview
fun BannerSection(
    modifier: Modifier = Modifier,
    iconSize: Dp = BannerIconSize,
    onInstagramClick: () -> Unit = {},
    onGithubClick: () -> Unit = {},
    onLinkedinClick: () -> Unit = {},
) {
    BoxWithConstraints(modifier = modifier) {
        Image(
            painter = painterResource(id = R.drawable.channelart),
            contentDescription = Semantics.ContentDescriptions.CHANNEL_ART,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()

        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black
                        ),
                        startY = constraints.maxHeight - iconSize.toPx() * 2f
                    )
                )
        ) {

        }
        SkillsIcons(
            modifier = Modifier
                .height(iconSize)
                .align(Alignment.BottomStart)
                .padding(PaddingSmall)
        )

        SocialMediaIcons(
            modifier = Modifier
                .height(iconSize)
                .align(Alignment.BottomEnd)
                .padding(PaddingSmall),
            onGithubClick = onGithubClick,
            onInstagramClick = onInstagramClick,
            onLinkedinClick = onLinkedinClick
        )
    }
}

@Composable
fun SkillsIcons(
    modifier: Modifier = Modifier,
    iconSize: Dp = BannerIconSize,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_js_logo),
            contentDescription = Semantics.ContentDescriptions.JAVASCRIPT,
            modifier = Modifier.height(iconSize)
        )
        Spacer(modifier = Modifier.width(SpaceMedium))
        Image(
            painter = painterResource(id = R.drawable.ic_csharp_logo),
            contentDescription = Semantics.ContentDescriptions.CSHARP,
            modifier = Modifier.height(iconSize)
        )
        Spacer(modifier = Modifier.width(SpaceMedium))
        Image(
            painter = painterResource(id = R.drawable.ic_kotlin_logo),
            contentDescription = Semantics.ContentDescriptions.KOTLIN,
            modifier = Modifier.height(iconSize)
        )
    }
}

@Composable
fun SocialMediaIcons(
    modifier: Modifier = Modifier,
    iconSize: Dp = BannerIconSize,
    onInstagramClick: () -> Unit,
    onGithubClick: () -> Unit,
    onLinkedinClick: () -> Unit,
) {
    Row(
        modifier = modifier
    ) {
        IconButton(
            onClick = onInstagramClick,
            modifier = Modifier.size(iconSize)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_instagram_glyph_1),
                contentDescription = Semantics.ContentDescriptions.JAVASCRIPT,
                modifier = Modifier.size(15.dp)
            )
        }
        IconButton(
            onClick = onGithubClick,
            modifier = Modifier.size(iconSize)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_github_icon_1),
                contentDescription = Semantics.ContentDescriptions.CSHARP,
                modifier = Modifier.size(15.dp)
            )
        }
        IconButton(
            onClick = onLinkedinClick,
            modifier = Modifier.size(iconSize)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_linkedin_icon_1),
                contentDescription = Semantics.ContentDescriptions.KOTLIN,
                modifier = Modifier.size(15.dp)
            )
        }
    }
}