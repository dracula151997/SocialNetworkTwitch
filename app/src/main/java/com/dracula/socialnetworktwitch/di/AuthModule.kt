package com.dracula.socialnetworktwitch.di

import android.content.SharedPreferences
import com.dracula.socialnetworktwitch.feature_auth.data.data_source.remote.AuthApi
import com.dracula.socialnetworktwitch.feature_auth.data.repository.AuthApiRepositoryImpl
import com.dracula.socialnetworktwitch.feature_auth.domain.repository.AuthRepository
import com.dracula.socialnetworktwitch.feature_auth.domain.use_case.AuthenticateUseCase
import com.dracula.socialnetworktwitch.feature_auth.domain.use_case.LoginUseCase
import com.dracula.socialnetworktwitch.feature_auth.domain.use_case.RegisterUseCase
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
object AuthModule {
    @Provides
    @Singleton
    fun provideAuthApi(
        okHttpClient: OkHttpClient
    ): AuthApi {
        return Retrofit.Builder()
            .baseUrl(AuthApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(AuthApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        api: AuthApi,
        sharedPreferences: SharedPreferences
    ): AuthRepository {
        return AuthApiRepositoryImpl(api, sharedPreferences)
    }

    @Provides
    @Singleton
    fun provideRegisterUseCase(repository: AuthRepository): RegisterUseCase {
        return RegisterUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideAuthenticateUseCase(repository: AuthRepository): AuthenticateUseCase {
        return AuthenticateUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideLoginUseCase(repository: AuthRepository): LoginUseCase {
        return LoginUseCase(repository)
    }
}