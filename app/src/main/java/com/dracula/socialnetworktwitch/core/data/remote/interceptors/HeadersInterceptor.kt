package com.dracula.socialnetworktwitch.core.data.remote.interceptors

import android.content.SharedPreferences
import com.dracula.socialnetworktwitch.core.utils.Constants
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class HeadersInterceptor @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = sharedPreferences.getString(Constants.SharedPrefKeys.KEY_TOKEN, "")
        val newRequest = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()
        return chain.proceed(newRequest)
    }
}