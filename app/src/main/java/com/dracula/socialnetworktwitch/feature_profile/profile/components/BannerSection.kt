package com.dracula.socialnetworktwitch.feature_profile.profile.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import com.dracula.socialnetworktwitch.core.presentation.components.StandardAsyncImage
import com.dracula.socialnetworktwitch.core.presentation.theme.BannerIconSize
import com.dracula.socialnetworktwitch.core.presentation.theme.PaddingSmall
import com.dracula.socialnetworktwitch.core.presentation.theme.SpaceMedium
import com.dracula.socialnetworktwitch.core.presentation.utils.toPx
import com.dracula.socialnetworktwitch.feature_profile.domain.model.Skill

@Composable
@Preview
fun BannerSection(
    modifier: Modifier = Modifier,
    iconSize: Dp = BannerIconSize,
    topSkillUrls: List<Skill> = emptyList(),
    githubUrl: String? = null,
    instagramUrl: String? = null,
    linkedinUrl: String? = null,
    bannerUrl: String? = null,
    onInstagramClick: () -> Unit = {},
    onGithubClick: () -> Unit = {},
    onLinkedinClick: () -> Unit = {},
) {
    BoxWithConstraints(modifier = modifier) {
        StandardAsyncImage(
            url = bannerUrl,
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
                .padding(PaddingSmall),
            topSkillUrls = topSkillUrls.map { it.imageUrl },
        )

        SocialMediaIcons(
            modifier = Modifier
                .height(iconSize)
                .align(Alignment.BottomEnd)
                .padding(PaddingSmall),
            githubUrl = githubUrl,
            instagramUrl = instagramUrl,
            linkedinUrl = linkedinUrl,
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
    topSkillUrls: List<String>
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        topSkillUrls.forEach { skillUrl ->
            StandardAsyncImage(
                url = skillUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(iconSize)
                    .aspectRatio(1f),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(SpaceMedium))

        }

    }
}

@Composable
fun SocialMediaIcons(
    modifier: Modifier = Modifier,
    iconSize: Dp = BannerIconSize,
    instagramUrl: String? = null,
    githubUrl: String? = null,
    linkedinUrl: String? = null,
    onInstagramClick: () -> Unit,
    onGithubClick: () -> Unit,
    onLinkedinClick: () -> Unit,
) {
    Row(
        modifier = modifier
    ) {
        if (!instagramUrl.isNullOrEmpty()) {
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

        }

        if (!githubUrl.isNullOrEmpty())
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


        if (!linkedinUrl.isNullOrEmpty())
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