package com.dracula.socialnetworktwitch.feature_auth.data.repository

import com.dracula.socialnetworktwitch.R
import com.dracula.socialnetworktwitch.core.utils.ApiResult
import com.dracula.socialnetworktwitch.core.utils.SimpleResource
import com.dracula.socialnetworktwitch.core.utils.UiText
import com.dracula.socialnetworktwitch.feature_auth.data.dto.request.CreateAccountRequest
import com.dracula.socialnetworktwitch.feature_auth.data.remote.AuthApi
import com.dracula.socialnetworktwitch.feature_auth.domain.repository.AuthRepository
import okio.IOException
import retrofit2.HttpException

class AuthApiRepositoryImpl(
    private val api: AuthApi
) : AuthRepository {
    override suspend fun register(
        email: String,
        username: String,
        password: String
    ): SimpleResource {
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
}