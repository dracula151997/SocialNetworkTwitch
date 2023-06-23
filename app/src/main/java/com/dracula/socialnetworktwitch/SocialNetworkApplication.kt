package com.dracula.socialnetworktwitch

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import com.dracula.socialnetworktwitch.core.utils.getCoilImageLoader
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class SocialNetworkApplication : Application(), ImageLoaderFactory {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }

    override fun newImageLoader(): ImageLoader {
        return getCoilImageLoader(this)
    }
}