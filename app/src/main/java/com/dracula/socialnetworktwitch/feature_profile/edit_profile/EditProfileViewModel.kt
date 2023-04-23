package com.dracula.socialnetworktwitch.feature_profile.edit_profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.dracula.socialnetworktwitch.core.presentation.utils.states.StandardTextFieldState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(

) : ViewModel() {

    var usernameState by mutableStateOf(StandardTextFieldState())
        private set

    var githubTextFieldState by mutableStateOf(StandardTextFieldState())
        private set

    var instagramTextFieldState by mutableStateOf(StandardTextFieldState())
        private set

    var linkedInTextFieldState by mutableStateOf(StandardTextFieldState())
        private set

    var bioState by mutableStateOf(StandardTextFieldState())
        private set

    fun setBio(state: StandardTextFieldState) {
        bioState = state
    }

    fun setLinkedIn(state: StandardTextFieldState) {
        linkedInTextFieldState = state
    }

    fun setInstagram(state: StandardTextFieldState) {
        instagramTextFieldState = state
    }

    fun setGithub(state: StandardTextFieldState) {
       githubTextFieldState = state
    }

    fun setUsername(state: StandardTextFieldState) {
        usernameState = state
    }
}