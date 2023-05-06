package com.dracula.socialnetworktwitch.feature_post.data.repository

import android.content.Context
import android.net.Uri
import androidx.core.net.toFile
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.dracula.socialnetworktwitch.R
import com.dracula.socialnetworktwitch.core.data.remote.PostApi
import com.dracula.socialnetworktwitch.core.domain.model.Post
import com.dracula.socialnetworktwitch.core.utils.ApiResult
import com.dracula.socialnetworktwitch.core.utils.Constants
import com.dracula.socialnetworktwitch.core.utils.UiText
import com.dracula.socialnetworktwitch.core.utils.UnitApiResult
import com.dracula.socialnetworktwitch.feature_post.data.data_source.paging.PostSource
import com.dracula.socialnetworktwitch.feature_post.data.data_source.remote.dto.request.CreatePostRequest
import com.dracula.socialnetworktwitch.feature_post.domain.repository.PostRepository
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okio.IOException
import retrofit2.HttpException

class PostRepositoryImpl(
    private val api: PostApi,
    private val gson: Gson,
    private val context: Context
) : PostRepository {

    override val posts: Flow<PagingData<Post>>
        get() = Pager(
            config = PagingConfig(
                pageSize = Constants.DEFAULT_PAGE_SIZE
            )
        ) {
            PostSource(api, source = PostSource.Source.Follows)
        }.flow

    override suspend fun createPost(description: String, imageUri: Uri): UnitApiResult {
        val request = CreatePostRequest(description)
        val file = imageUri.toFile()


        return try {
            val response = api.createPost(
                postData = MultipartBody.Part.createFormData("post_data", gson.toJson(request)),
                postImage = MultipartBody.Part.createFormData(
                    name = "post_image",
                    filename = file.name,
                    body = file.asRequestBody()
                )
            )
            if (response.successful) {
                ApiResult.Success(Unit)
            } else {
                response.message?.let { msg ->
                    ApiResult.Error(UiText.DynamicString(msg))
                } ?: ApiResult.Error(UiText.unknownError())
            }
        } catch (e: IOException) {
            ApiResult.Error(UiText.StringResource(R.string.error_couldnot_reach_server))
        } catch (e: HttpException) {
            ApiResult.Error(UiText.StringResource(R.string.error_something_went_wrong))
        }
    }
}