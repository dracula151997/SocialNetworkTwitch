package com.dracula.socialnetworktwitch.presentation.components

import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.text.input.KeyboardType
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.dracula.socialnetworktwitch.presentation.MainActivity
import com.dracula.socialnetworktwitch.presentation.ui.Semantics
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class StandardTextFieldTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {

    }

    @Test
    fun enterTooLongString_maxLengthNotExceeded() {
        composeTestRule.activity.setContent {
            var text by remember {
                mutableStateOf("")
            }
            MaterialTheme {
                StandardTextField(text = text, onValueChanged = { text = it })
            }
        }
        composeTestRule.onNodeWithTag(Semantics.TestTags.STANDARD_TEXT_FIELD)
            .performTextInput("123456")
        val value = composeTestRule.onNodeWithTag(Semantics.TestTags.STANDARD_TEXT_FIELD)
//        value.assertTextEquals("12345")
        for ((key, value) in value.fetchSemanticsNode().config) {
            if (key.name == "EditableText") {
                assertEquals("12345", value.toString())
            }
        }
    }

    @Test
    fun passwordTextField_isPasswordToggleDisplayed() {
        composeTestRule.activity.setContent {
            var text by remember {
                mutableStateOf("")
            }
            MaterialTheme {
                StandardTextField(
                    text = text,
                    onValueChanged = { text = it },
                    keyboardType = KeyboardType.Password
                )
            }
        }
        composeTestRule.onNodeWithTag(Semantics.TestTags.PASSWORD_TOGGLE)
            .assertIsDisplayed()
    }

    @Test
    fun passwordTextField_clickOnPasswordToggle_isIconChanged() {
        composeTestRule.activity.setContent {
            var text by remember {
                mutableStateOf("")
            }
            MaterialTheme {
                StandardTextField(
                    text = text,
                    onValueChanged = { text = it },
                    keyboardType = KeyboardType.Password
                )
            }
        }
        composeTestRule.onNodeWithTag(Semantics.TestTags.STANDARD_TEXT_FIELD)
            .performTextInput("12345")
        composeTestRule.onNodeWithTag(Semantics.TestTags.PASSWORD_TOGGLE)
            .assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription(Semantics.ContentDescriptions.SHOW_PASSWORD_ICON)
            .performClick()
        composeTestRule.onNodeWithContentDescription(Semantics.ContentDescriptions.HIDE_PASSWORD_ICON)
            .assertExists()
    }
}