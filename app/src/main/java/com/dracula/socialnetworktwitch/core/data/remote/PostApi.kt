package com.dracula.socialnetworktwitch.core.data.remote

import com.dracula.socialnetworktwitch.core.data.remote.dto.response.BasicApiResponse
import com.dracula.socialnetworktwitch.core.data.remote.dto.response.UnitApiResponse
import com.dracula.socialnetworktwitch.core.domain.model.Post
import com.dracula.socialnetworktwitch.feature_post.data.data_source.remote.dto.request.CreateCommentRequest
import com.dracula.socialnetworktwitch.feature_post.data.data_source.remote.dto.response.CommentResponse
import com.dracula.socialnetworktwitch.feature_post.data.data_source.remote.dto.response.PostResponse
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface PostApi {
    @GET("/api/post/get")
    suspend fun getPostsForFollows(
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int
    ): List<Post>

    @Multipart
    @POST("/api/post/create")
    suspend fun createPost(
        @Part postData: MultipartBody.Part,
        @Part postImage: MultipartBody.Part
    ): BasicApiResponse<Unit>

    @GET("/api/user/posts")
    suspend fun getUserPosts(
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int,
        @Query("userId") userId: String,
    ): List<Post>

    @GET("/api/post/details")
    suspend fun getPostDetails(
        @Query("postId") postId: String
    ): BasicApiResponse<PostResponse>

    @GET("/api/comment/get")
    suspend fun getCommentForPost(
        @Query("postId") postId: String,
    ): BasicApiResponse<List<CommentResponse>>

    @POST("/api/comment/create")
    suspend fun createComment(
        @Body request: CreateCommentRequest
    ): UnitApiResponse

    @POST("/api/like")
    suspend fun likePost(
        @Query("postId") postId: String,
        @Query("userId") userId: String
    ): UnitApiResponse

    companion object {
        const val BASE_URL = "http://10.0.2.2:8080"
    }
}