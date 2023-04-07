package com.dracula.socialnetworktwitch.presentation.ui.activity

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dracula.socialnetworktwitch.R
import com.dracula.socialnetworktwitch.domain.model.Activity
import com.dracula.socialnetworktwitch.domain.model.ActivityAction
import com.dracula.socialnetworktwitch.presentation.components.StandardTopBar
import com.dracula.socialnetworktwitch.presentation.ui.theme.PaddingMedium
import com.dracula.socialnetworktwitch.presentation.ui.theme.SpaceMedium
import com.dracula.socialnetworktwitch.presentation.ui.theme.StandardElevation

@Composable
fun ActivityScreen(
    navController: NavController,
    viewModel: ActivityViewModel = hiltViewModel(),
) {
    Column(modifier = Modifier) {
        StandardTopBar(title = stringResource(id = R.string.your_activity))
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = PaddingMedium, vertical = PaddingMedium),
            verticalArrangement = Arrangement.spacedBy(SpaceMedium)
        ) {
            items(20) {
                ActivityItem(
                    activity = Activity.dummy()
                )
            }
        }
    }

}

@Composable
fun ActivityItem(
    activity: Activity,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        elevation = StandardElevation,
        backgroundColor = MaterialTheme.colors.surface,
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(PaddingMedium),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val activityText = when (activity.actionType) {
                ActivityAction.CommentOnPost -> stringResource(
                    id = R.string.commented_on
                )
                ActivityAction.LikedPost -> stringResource(
                    id = R.string.liked,
                )
            }
            Text(
                text = buildAnnotatedString {
                    val boldStyle = SpanStyle(
                        fontWeight = FontWeight.Bold,
                    )
                    withStyle(boldStyle) {
                        append(activity.username)
                    }
                    append(" $activityText ")
                    withStyle(boldStyle) {
                        append(
                            stringResource(
                                id = R.string.on_your_post,
                            ),
                        )
                    }
                },
                style = MaterialTheme.typography.body2
            )
            Text(
                text = activity.timestamp,
                color = MaterialTheme.colors.onBackground,
                style = MaterialTheme.typography.body2

            )
        }
    }

}