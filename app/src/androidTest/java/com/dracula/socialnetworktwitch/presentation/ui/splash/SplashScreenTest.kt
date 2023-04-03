package com.dracula.socialnetworktwitch.presentation.ui.splash

import androidx.activity.compose.setContent
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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
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

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxed = true)
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun splashScreen_displaysAndDisappears() = testDispatcher.run {
        composeRule.activity.setContent {
            SocialNetworkTwitchTheme {
                SplashScreen(navController = navController, testDispatcher)
            }
        }
        composeRule.onNodeWithContentDescription(Semantics.ContentDescriptions.SPLASH_LOGO)
            .assertExists()
        testDispatcher.scheduler.advanceTimeBy(Constants.SPLASH_SCREEN_DURATION)
        verify {
            navController.navigate(Screen.LoginScreen.route){
                popUpTo(Screen.SplashScreen.route){
                    inclusive = true
                }
            }
        }
    }

}