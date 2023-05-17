package com.dracula.socialnetworktwitch.di

import android.content.SharedPreferences
import com.dracula.socialnetworktwitch.core.data.remote.PostApi
import com.dracula.socialnetworktwitch.feature_profile.data.data_source.remote.ProfileApi
import com.dracula.socialnetworktwitch.feature_profile.data.repository.ProfileRepositoryImpl
import com.dracula.socialnetworktwitch.feature_profile.domain.repository.GetSkillsUseCase
import com.dracula.socialnetworktwitch.feature_profile.domain.repository.ProfileRepository
import com.dracula.socialnetworktwitch.feature_profile.domain.use_case.GetProfileUseCase
import com.dracula.socialnetworktwitch.feature_profile.domain.use_case.GetUserPostsUseCase
import com.dracula.socialnetworktwitch.feature_profile.domain.use_case.SetSkillSelectedUseCase
import com.dracula.socialnetworktwitch.feature_profile.domain.use_case.ToggleFollowStateForUserUseCase
import com.dracula.socialnetworktwitch.feature_profile.domain.use_case.UpdateProfileUseCase
import com.dracula.socialnetworktwitch.feature_search.domain.SearchUserUseCase
import com.google.gson.Gson
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
object ProfileModule {

    @Provides
    @Singleton
    fun provideProfileApi(client: OkHttpClient): ProfileApi {
        return Retrofit.Builder()
            .baseUrl(ProfileApi.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ProfileApi::class.java)
    }

    @Provides
    @Singleton
    fun provideProfileRepository(
        api: ProfileApi,
        postApi: PostApi,
        gson: Gson,
        sharedPreferences: SharedPreferences,
    ): ProfileRepository {
        return ProfileRepositoryImpl(api, postApi, gson, sharedPreferences)
    }

    @Provides
    @Singleton
    fun provideGetProfileUseCase(repository: ProfileRepository): GetProfileUseCase {
        return GetProfileUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetSkillsUseCase(repository: ProfileRepository): GetSkillsUseCase {
        return GetSkillsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideUpdateProfileUseCase(repository: ProfileRepository): UpdateProfileUseCase {
        return UpdateProfileUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideSetSkillsSelectedUseCase(): SetSkillSelectedUseCase {
        return SetSkillSelectedUseCase()
    }

    @Provides
    @Singleton
    fun provideGetUserPostsUseCase(repository: ProfileRepository): GetUserPostsUseCase {
        return GetUserPostsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideSearchUsersUseCase(repository: ProfileRepository): SearchUserUseCase {
        return SearchUserUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideToggleFollowStateForUserUseCase(repository: ProfileRepository): ToggleFollowStateForUserUseCase {
        return ToggleFollowStateForUserUseCase(repository)
    }
}