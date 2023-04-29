package com.dracula.socialnetworktwitch.feature_profile.data.repository

import com.dracula.socialnetworktwitch.R
import com.dracula.socialnetworktwitch.core.utils.ApiResult
import com.dracula.socialnetworktwitch.core.utils.UiText
import com.dracula.socialnetworktwitch.feature_profile.data.data_source.remote.ProfileApi
import com.dracula.socialnetworktwitch.feature_profile.domain.model.Profile
import com.dracula.socialnetworktwitch.feature_profile.domain.model.toProfile
import com.dracula.socialnetworktwitch.feature_profile.domain.repository.ProfileRepository
import okio.IOException
import retrofit2.HttpException

class ProfileRepositoryImpl(
    private val api: ProfileApi,
) : ProfileRepository {
    override suspend fun getProfile(userId: String): ApiResult<Profile> {
        return try {
            val response = api.getProfile(userId)
            if (response.successful) {
                ApiResult.Success(data = response.data?.toProfile())
            } else {
                response.message?.let { msg ->
                    ApiResult.Error(UiText.DynamicString(msg))
                } ?: ApiResult.Error(UiText.unknownError())
            }
        } catch (e: IOException) {
            ApiResult.Error(message = UiText.StringResource(R.string.error_couldnot_reach_server))
        } catch (e: HttpException) {
            ApiResult.Error(message = UiText.StringResource(R.string.error_unknown))
        }
    }
}