package com.dracula.socialnetworktwitch.feature_post.presentation.create_post

import android.net.Uri
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.dracula.socialnetworktwitch.R
import com.dracula.socialnetworktwitch.core.presentation.utils.BaseViewModel
import com.dracula.socialnetworktwitch.core.presentation.utils.states.validator.NonEmptyFieldState
import com.dracula.socialnetworktwitch.core.utils.ApiResult
import com.dracula.socialnetworktwitch.core.utils.isNotNull
import com.dracula.socialnetworktwitch.core.utils.orUnknownError
import com.dracula.socialnetworktwitch.feature_post.domain.use_case.CreatePostUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreatePostViewModel @Inject constructor(
    private val createPostUseCase: CreatePostUseCase
) : BaseViewModel<CreatePostState, CreatePostEvent>() {
    var description by mutableStateOf(NonEmptyFieldState())
        private set

    var chosenImageUri: Uri? by mutableStateOf(null)
        private set

    val enablePostButton by derivedStateOf {
        description.isValid && chosenImageUri.isNotNull() && !viewState.isLoading
    }

    override fun initialState(): CreatePostState {
        return CreatePostState()
    }

    override fun onEvent(event: CreatePostEvent) {
        when (event) {
            is CreatePostEvent.PickImage -> chosenImageUri = event.uri
            CreatePostEvent.CreatePost -> createPost()
            is CreatePostEvent.CropImage -> chosenImageUri = event.uri
        }
    }

    private fun createPost() {
        viewModelScope.launch {
            setState {
                copy(isLoading = true)
            }
            val apiResult = createPostUseCase(
                description = description.text,
                imageUri = chosenImageUri
            )

            if (apiResult.hasImageError)
                showSnackbar(R.string.error_no_image_picked)

            when (val result = apiResult.result) {
                is ApiResult.Success -> {
                    setState { copy(isLoading = false) }
                    showSnackbar(R.string.post_created)
                    navigateUp()
                }

                is ApiResult.Error -> {
                    setState { copy(isLoading = false) }
                    showSnackbar(uiText = result.uiText.orUnknownError())
                }

                else -> Unit
            }
        }
    }

}