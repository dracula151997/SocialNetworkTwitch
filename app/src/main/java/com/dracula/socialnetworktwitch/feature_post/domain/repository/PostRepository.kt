package com.dracula.socialnetworktwitch.feature_post.domain.repository

import android.net.Uri
import androidx.paging.PagingData
import com.dracula.socialnetworktwitch.core.utils.UnitApiResult
import com.dracula.socialnetworktwitch.feature_post.domain.Post
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    val posts: Flow<PagingData<Post>>
    suspend fun createPost(description: String, imageUri: Uri): UnitApiResult
}