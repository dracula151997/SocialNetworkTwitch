package com.dracula.socialnetworktwitch.feature_auth.data.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import com.dracula.socialnetworktwitch.R
import com.dracula.socialnetworktwitch.core.utils.ApiResult
import com.dracula.socialnetworktwitch.core.utils.Constants
import com.dracula.socialnetworktwitch.core.utils.UiText
import com.dracula.socialnetworktwitch.core.utils.UnitApiResult
import com.dracula.socialnetworktwitch.feature_auth.data.data_source.remote.AuthApi
import com.dracula.socialnetworktwitch.feature_auth.data.data_source.remote.dto.request.CreateAccountRequest
import com.dracula.socialnetworktwitch.feature_auth.data.data_source.remote.dto.request.LoginRequest
import com.dracula.socialnetworktwitch.feature_auth.domain.repository.AuthRepository
import okio.IOException
import retrofit2.HttpException
import timber.log.Timber

class AuthApiRepositoryImpl(
    private val api: AuthApi,
    private val sharedPreferences: SharedPreferences
) : AuthRepository {
    override suspend fun register(
        email: String,
        username: String,
        password: String
    ): UnitApiResult {
        val request = CreateAccountRequest(email, username, password)
        return try {
            val response = api.register(request)
            if (response.successful) {
                ApiResult.Success(Unit)
            } else {
                response.message?.let { msg ->
                    ApiResult.Error(UiText.DynamicString(msg))
                } ?: ApiResult.Error(UiText.unknownError())
            }
        } catch (e: IOException) {
            ApiResult.Error(
                UiText.StringResource(R.string.error_couldnot_reach_server)
            )

        } catch (e: HttpException) {
            ApiResult.Error(UiText.StringResource(R.string.error_something_went_wrong))
        }
    }

    override suspend fun login(email: String, password: String): UnitApiResult {
        val request = LoginRequest(email, password)
        return try {
            val response = api.login(request)
            if (response.successful) {
                response.data?.let { authResponse ->
                    sharedPreferences.edit {
                        putString(Constants.SharedPrefKeys.KEY_TOKEN, authResponse.token)
                        putString(Constants.SharedPrefKeys.KEY_USER_ID, authResponse.userId)
                        Timber.d("loginApi: ${authResponse.userId}")
                    }
                }

                ApiResult.Success(Unit)
            } else {
                response.message?.let { msg ->
                    ApiResult.Error(UiText.DynamicString(msg))
                } ?: ApiResult.Error(UiText.unknownError())
            }
        } catch (e: IOException) {
            ApiResult.Error(
                UiText.StringResource(R.string.error_couldnot_reach_server)
            )

        } catch (e: HttpException) {
            ApiResult.Error(UiText.StringResource(R.string.error_something_went_wrong))
        }
    }

    override suspend fun authenticate(): UnitApiResult {
        return try {
            api.authenticate()
            ApiResult.Success(Unit)
        } catch (e: IOException) {
            e.printStackTrace()
            ApiResult.Error(
                UiText.StringResource(R.string.error_couldnot_reach_server)
            )

        } catch (e: HttpException) {
            e.printStackTrace()
            ApiResult.Error(UiText.StringResource(R.string.error_something_went_wrong))
        }
    }

    override val ownUserId: String
        get() = sharedPreferences.getString(Constants.SharedPrefKeys.KEY_USER_ID, "").orEmpty()
    override val userToken: String
        get() = sharedPreferences.getString(Constants.SharedPrefKeys.KEY_USER_ID, "").orEmpty()
}