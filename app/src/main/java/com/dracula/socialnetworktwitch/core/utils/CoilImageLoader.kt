package com.dracula.socialnetworktwitch.core.utils

import android.content.Context
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.util.DebugLogger

fun getCoilImageLoader(context: Context): ImageLoader {
    return ImageLoader.Builder(context)
        .logger(DebugLogger())
        .components {
            add(SvgDecoder.Factory())
        }
        .build()
}

