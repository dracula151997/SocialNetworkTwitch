package com.dracula.socialnetworktwitch.feature_post.data.repository

import com.dracula.socialnetworktwitch.R
import com.dracula.socialnetworktwitch.core.utils.ApiResult
import com.dracula.socialnetworktwitch.core.utils.UiText
import com.dracula.socialnetworktwitch.feature_post.data.data_source.remote.PostApi
import com.dracula.socialnetworktwitch.feature_post.domain.Post
import com.dracula.socialnetworktwitch.feature_post.domain.repository.PostRepository
import okio.IOException
import retrofit2.HttpException

class PostRepositoryImpl(
    private val api: PostApi
): PostRepository {
    override suspend fun getPostsForFollows(page: Int, pageSize: Int): ApiResult<List<Post>> {
        return try {
            val posts = api.getPostsForFollows(page, pageSize)
            ApiResult.Success(posts)
        }catch (e: IOException){
            ApiResult.Error(UiText.StringResource(R.string.error_couldnot_reach_server))
        }catch (e: HttpException){
            ApiResult.Error(UiText.StringResource(R.string.error_couldnot_reach_server))
        }
    }
}