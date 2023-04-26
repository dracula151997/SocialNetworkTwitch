package com.dracula.socialnetworktwitch.feature_post.presentation.create_post

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dracula.socialnetworktwitch.core.presentation.utils.states.StandardTextFieldState
import com.dracula.socialnetworktwitch.feature_post.domain.use_case.CreatePostUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreatePostViewModel @Inject constructor(
    private val createPostUseCase: CreatePostUseCase
) : ViewModel() {
    var description by mutableStateOf(StandardTextFieldState())
        private set

    var chosenImageUri: Uri? by mutableStateOf(null)
        private set

    fun onEvent(event: CreatePostEvent) {
        when (event) {
            is CreatePostEvent.DescriptionEntered -> description = description.copy(
                text = event.text
            )

            is CreatePostEvent.PickImage -> chosenImageUri = event.uri
            CreatePostEvent.CreatePost -> createPost()
        }
    }

    private fun createPost() {
        chosenImageUri?.let { uri ->
            viewModelScope.launch {
                createPostUseCase(
                    description = description.text,
                    imageUri = uri
                )
            }
        }
    }


}