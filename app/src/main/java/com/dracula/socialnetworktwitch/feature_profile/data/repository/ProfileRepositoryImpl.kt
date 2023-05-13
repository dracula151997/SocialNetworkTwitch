package com.dracula.socialnetworktwitch.feature_profile.data.repository

import android.net.Uri
import androidx.core.net.toFile
import com.dracula.socialnetworktwitch.R
import com.dracula.socialnetworktwitch.core.data.remote.PostApi
import com.dracula.socialnetworktwitch.core.domain.model.Post
import com.dracula.socialnetworktwitch.core.domain.model.UserItem
import com.dracula.socialnetworktwitch.core.utils.ApiResult
import com.dracula.socialnetworktwitch.core.utils.UiText
import com.dracula.socialnetworktwitch.core.utils.UnitApiResult
import com.dracula.socialnetworktwitch.feature_profile.data.data_source.remote.ProfileApi
import com.dracula.socialnetworktwitch.feature_profile.data.data_source.remote.dto.request.FollowUpdateRequest
import com.dracula.socialnetworktwitch.feature_profile.data.data_source.remote.dto.request.UpdateProfileRequest
import com.dracula.socialnetworktwitch.feature_profile.data.data_source.remote.dto.response.toSkillList
import com.dracula.socialnetworktwitch.feature_profile.domain.model.Profile
import com.dracula.socialnetworktwitch.feature_profile.domain.model.Skill
import com.dracula.socialnetworktwitch.feature_profile.domain.model.toProfile
import com.dracula.socialnetworktwitch.feature_profile.domain.repository.ProfileRepository
import com.google.gson.Gson
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okio.IOException
import retrofit2.HttpException

class ProfileRepositoryImpl(
    private val api: ProfileApi,
    private val postApi: PostApi,
    private val gson: Gson,
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

    override suspend fun getSkills(): ApiResult<List<Skill>> {
        return try {
            val response = api.getSkills()
            if (response.successful) {
                ApiResult.Success(response.data?.toSkillList())
            } else {
                response.message?.let { msg ->
                    ApiResult.Error(UiText.DynamicString(msg))
                } ?: ApiResult.Error(UiText.unknownError())
            }
        } catch (e: IOException) {
            ApiResult.Error(message = UiText.StringResource(R.string.error_couldnot_reach_server))
        } catch (e: HttpException) {
            ApiResult.Error(UiText.unknownError())
        }
    }

    override suspend fun updateProfile(
        updateProfileData: UpdateProfileRequest,
        bannerImageUri: Uri?,
        profilePictureUri: Uri?
    ): UnitApiResult {
        val bannerFile = bannerImageUri?.toFile()
        val profilePictureFile = profilePictureUri?.toFile()

        return try {
            val response = api.updateProfile(
                bannerImage = bannerFile?.let {
                    MultipartBody.Part.createFormData(
                        "banner_image",
                        bannerFile.name,
                        it.asRequestBody()
                    )
                },
                profilePicture = profilePictureFile?.let {
                    MultipartBody.Part.createFormData(
                        "profile_picture",
                        profilePictureFile.name,
                        it.asRequestBody()
                    )
                },
                updateProfileData = MultipartBody.Part.createFormData(
                    "update_profile_data",
                    gson.toJson(updateProfileData)
                )
            )
            if (response.successful) {
                ApiResult.Success(Unit)
            } else {
                response.message?.let { msg ->
                    ApiResult.Error(UiText.DynamicString(msg))
                } ?: ApiResult.Error(UiText.unknownError())
            }

        } catch (e: IOException) {
            ApiResult.Error(UiText.StringResource(R.string.error_couldnot_reach_server))
        } catch (e: HttpException) {
            ApiResult.Error(UiText.StringResource(R.string.error_something_went_wrong))
        }
    }

    override suspend fun getPostPaged(
        userId: String,
        page: Int,
        pageSize: Int
    ): ApiResult<List<Post>> {
        return try {
            val response = postApi.getUserPosts(page = page, pageSize, userId)
            ApiResult.Success(response.map { it.toPost() })
        } catch (e: IOException) {
            ApiResult.Error(UiText.StringResource(R.string.error_couldnot_reach_server))
        } catch (e: HttpException) {
            ApiResult.Error(UiText.StringResource(R.string.error_something_went_wrong))
        }
    }

    override suspend fun searchUser(username: String): ApiResult<List<UserItem>> {
        return try {
            val response = api.searchUser(username)
            if (response.successful) {
                ApiResult.Success(response.data?.map { it.toUserItem() })
            } else {
                response.message?.let { msg ->
                    ApiResult.Error(UiText.DynamicString(msg))
                } ?: ApiResult.Error(UiText.unknownError())
            }
        } catch (e: IOException) {
            ApiResult.Error(UiText.StringResource(R.string.error_couldnot_reach_server))
        } catch (e: HttpException) {
            ApiResult.Error(UiText.StringResource(R.string.error_something_went_wrong))
        }
    }

    override suspend fun followUser(userId: String): UnitApiResult {
        return try {
            val response = api.followUser(FollowUpdateRequest(userId))
            if (response.successful) {
                ApiResult.Success(Unit)
            } else {
                response.message?.let { msg ->
                    ApiResult.Error(UiText.DynamicString(msg))
                } ?: ApiResult.Error(UiText.unknownError())
            }
        } catch (e: IOException) {
            ApiResult.Error(UiText.StringResource(R.string.error_couldnot_reach_server))
        } catch (e: HttpException) {
            ApiResult.Error(UiText.StringResource(R.string.error_something_went_wrong))
        }
    }

    override suspend fun unfollowUser(userId: String): UnitApiResult {
        return try {
            val response = api.unfollowUser(userId)
            if (response.successful) {
                ApiResult.Success(Unit)
            } else {
                response.message?.let { msg ->
                    ApiResult.Error(UiText.DynamicString(msg))
                } ?: ApiResult.Error(UiText.unknownError())
            }
        } catch (e: IOException) {
            ApiResult.Error(UiText.StringResource(R.string.error_couldnot_reach_server))
        } catch (e: HttpException) {
            ApiResult.Error(UiText.StringResource(R.string.error_something_went_wrong))
        }
    }
}