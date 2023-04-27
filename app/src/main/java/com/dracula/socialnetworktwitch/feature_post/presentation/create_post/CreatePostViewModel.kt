package com.dracula.socialnetworktwitch.feature_post.presentation.create_post

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dracula.socialnetworktwitch.R
import com.dracula.socialnetworktwitch.core.presentation.utils.states.StandardTextFieldState
import com.dracula.socialnetworktwitch.core.utils.ApiResult
import com.dracula.socialnetworktwitch.core.utils.UiEvent
import com.dracula.socialnetworktwitch.core.utils.UiText
import com.dracula.socialnetworktwitch.core.utils.orUnknownError
import com.dracula.socialnetworktwitch.feature_post.domain.use_case.CreatePostUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
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

    var state by mutableStateOf(CreatePostState())
        private set

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onEvent(event: CreatePostEvent) {
        when (event) {
            is CreatePostEvent.DescriptionEntered -> description = description.copy(
                text = event.text
            )

            is CreatePostEvent.PickImage -> chosenImageUri = event.uri
            CreatePostEvent.CreatePost -> createPost()
            is CreatePostEvent.CropImage -> chosenImageUri = event.uri
        }
    }

    private fun createPost() {

        viewModelScope.launch {
            clearAllFieldErrorState()
            state = CreatePostState.loading()
            val apiResult = createPostUseCase(
                description = description.text,
                imageUri = chosenImageUri
            )
            if (apiResult.hasDescriptionError)
                description =
                    description.copy(error = apiResult.descriptionError)

            if (apiResult.hasImageError)
                _eventFlow.emit(UiEvent.SnackbarEvent(UiText.StringResource(R.string.error_no_image_picked)))

            when (val result = apiResult.result) {
                is ApiResult.Success -> {
                    clearAllFieldState()
                    state = CreatePostState.success()
                    _eventFlow.emit(UiEvent.SnackbarEvent(UiText.StringResource(R.string.post_created)))
                    _eventFlow.emit(UiEvent.NavigateUp)
                }

                is ApiResult.Error -> {
                    state = CreatePostState.error()
                    _eventFlow.emit(
                        UiEvent.SnackbarEvent(
                            uiText = result.uiText.orUnknownError()
                        )
                    )
                }

                else -> state = CreatePostState.idle()
            }
        }
    }

    private fun clearAllFieldState() {
        description = description.defaultState()
    }

    private fun clearAllFieldErrorState() {
        description = description.copy(
            error = null
        )
    }


}