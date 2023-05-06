package com.dracula.socialnetworktwitch.di

import com.dracula.socialnetworktwitch.feature_activity.data.ActivityRepositoryImpl
import com.dracula.socialnetworktwitch.feature_activity.data.data_source.remote.ActivityApi
import com.dracula.socialnetworktwitch.feature_activity.domain.repository.ActivityRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ActivityModule {
    @Provides
    @Singleton
    fun provideActivityApi(
        okHttpClient: OkHttpClient,
    ): ActivityApi {
        return Retrofit.Builder()
            .baseUrl(ActivityApi.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ActivityApi::class.java)
    }

    @Provides
    @Singleton
    fun provideActivityRepository(api: ActivityApi): ActivityRepository {
        return ActivityRepositoryImpl(api)
    }
}