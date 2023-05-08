package com.dracula.socialnetworktwitch.feature_post.domain.repository

import android.net.Uri
import androidx.paging.PagingData
import com.dracula.socialnetworktwitch.core.domain.model.Comment
import com.dracula.socialnetworktwitch.core.domain.model.Post
import com.dracula.socialnetworktwitch.core.utils.ApiResult
import com.dracula.socialnetworktwitch.core.utils.UnitApiResult
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    val posts: Flow<PagingData<Post>>
    suspend fun createPost(description: String, imageUri: Uri): UnitApiResult
    suspend fun getPostDetails(postId: String): ApiResult<Post>
    suspend fun getCommentsForPost(postId: String): ApiResult<List<Comment>>

    suspend fun createComment(
        comment: String,
        postId: String
    ): UnitApiResult
}