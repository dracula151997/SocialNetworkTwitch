package com.dracula.socialnetworktwitch.presentation.ui.splash

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.navigation.NavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.dracula.socialnetworktwitch.Constants
import com.dracula.socialnetworktwitch.presentation.MainActivity
import com.dracula.socialnetworktwitch.presentation.ui.Semantics
import com.dracula.socialnetworktwitch.presentation.ui.theme.SocialNetworkTwitchTheme
import com.dracula.socialnetworktwitch.presentation.ui.utils.Screen
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SplashScreenTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @RelaxedMockK
    lateinit var navController: NavController

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }


    @Test
    fun splashScreen_displaysAndDisappears() = runTest {
        composeRule.setContent {
            SocialNetworkTwitchTheme {
                SplashScreen(navController = navController)
            }
        }
        composeRule.onNodeWithContentDescription(Semantics.ContentDescriptions.SPLASH_LOGO)
            .assertExists()
        advanceTimeBy(Constants.SPLASH_SCREEN_TIME)
        verify {
            navController.navigate(Screen.LoginScreen.route)
        }
    }

}