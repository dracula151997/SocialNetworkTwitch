package com.dracula.socialnetworktwitch.di

import android.content.Context
import com.dracula.socialnetworktwitch.core.data.remote.PostApi
import com.dracula.socialnetworktwitch.feature_post.data.repository.PostRepositoryImpl
import com.dracula.socialnetworktwitch.feature_post.domain.repository.PostRepository
import com.dracula.socialnetworktwitch.feature_post.domain.use_case.CreatePostUseCase
import com.dracula.socialnetworktwitch.feature_post.domain.use_case.GetPostsForFollowsUseCase
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PostModule {

    @Provides
    @Singleton
    fun providePostApi(client: OkHttpClient): PostApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(PostApi.BASE_URL)
            .client(client)
            .build()
            .create(PostApi::class.java)
    }

    @Provides
    @Singleton
    fun providePostRepository(
        api: PostApi,
        gson: Gson,
        @ApplicationContext context: Context
    ): PostRepository {
        return PostRepositoryImpl(api, gson, context)
    }

    @Provides
    @Singleton
    fun providePostUseCase(repository: PostRepository): GetPostsForFollowsUseCase {
        return GetPostsForFollowsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideCreatePostUseCase(repository: PostRepository): CreatePostUseCase {
        return CreatePostUseCase(repository)
    }
}