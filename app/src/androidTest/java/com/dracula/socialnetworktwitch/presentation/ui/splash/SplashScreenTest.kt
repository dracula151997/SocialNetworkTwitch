package com.dracula.socialnetworktwitch.presentation.ui.splash

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.navigation.NavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.dracula.socialnetworktwitch.core.utils.Constants
import com.dracula.socialnetworktwitch.feature_splash.presentation.SplashScreen
import com.dracula.socialnetworktwitch.core.presentation.Semantics
import com.dracula.socialnetworktwitch.core.presentation.theme.SocialNetworkTwitchTheme
import com.dracula.socialnetworktwitch.core.presentation.utils.Screens
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
    val composeRule = createComposeRule()

    @RelaxedMockK
    lateinit var navController: NavController

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxed = true)
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun splashScreen_displaysAndDisappears() = testDispatcher.runBlockingTest {
        composeRule.apply {
            setContent {
                SocialNetworkTwitchTheme {
                    SplashScreen(navController = navController, testDispatcher)
                }
            }
            onNodeWithContentDescription(Semantics.ContentDescriptions.SPLASH_LOGO)
            testScheduler.advanceTimeBy(Constants.SPLASH_SCREEN_DURATION)
            verify {
                navController.navigate(Screens.LoginScreen.route){
                    popUpTo(Screens.SplashScreen.route){
                        inclusive = true
                    }
                }
            }
        }


    }

}