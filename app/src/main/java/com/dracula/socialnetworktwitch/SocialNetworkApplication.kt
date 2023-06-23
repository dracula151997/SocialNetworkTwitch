package com.dracula.socialnetworktwitch

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import com.dracula.socialnetworktwitch.core.utils.getCoilImageLoader
import com.dracula.socialnetworktwitch.feature_chat.data.remote.utils.ScarletInstance
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class SocialNetworkApplication : Application(), ImageLoaderFactory {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        ScarletInstance.init(this)
    }

    override fun newImageLoader(): ImageLoader {
        return getCoilImageLoader(this)
    }
}