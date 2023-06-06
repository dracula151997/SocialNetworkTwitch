package com.dracula.socialnetworktwitch.feature_post.domain.repository

import android.net.Uri
import com.dracula.socialnetworktwitch.core.domain.model.Comment
import com.dracula.socialnetworktwitch.core.domain.model.Post
import com.dracula.socialnetworktwitch.core.domain.model.UserItem
import com.dracula.socialnetworktwitch.core.utils.ApiResult
import com.dracula.socialnetworktwitch.core.utils.Constants
import com.dracula.socialnetworktwitch.core.utils.UnitApiResult

interface PostRepository {
    suspend fun createPost(description: String, imageUri: Uri): UnitApiResult
    suspend fun getPostDetails(postId: String): ApiResult<Post>
    suspend fun getCommentsForPost(postId: String): ApiResult<List<Comment>>

    suspend fun getPostsForFollowers(
        page: Int,
        pageSize: Int = Constants.DEFAULT_PAGE_SIZE
    ): ApiResult<List<Post>>

    suspend fun createComment(
        comment: String,
        postId: String
    ): UnitApiResult

    suspend fun likeParent(
        parentId: String,
        parentType: Int
    ): UnitApiResult

    suspend fun unlikeParent(parentId: String, parentType: Int): UnitApiResult

    suspend fun getLikesForParent(parentId: String): ApiResult<List<UserItem>>

    suspend fun deletePost(postId: String): UnitApiResult
}