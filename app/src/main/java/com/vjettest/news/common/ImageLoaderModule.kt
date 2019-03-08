package com.vjettest.news.common

import android.content.Context
import androidx.annotation.NonNull
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache
import com.nostra13.universalimageloader.core.DisplayImageOptions
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer
import com.vjettest.news.R
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ImageLoaderModule(private val context: Context) {

    private val defaultOptions by lazy {
        DisplayImageOptions.Builder()
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .resetViewBeforeLoading(true)
            .showImageOnFail(R.drawable.placeholder)
            .showImageForEmptyUri(R.drawable.placeholder)
            .showImageOnLoading(R.drawable.placeholder)
            .displayer(FadeInBitmapDisplayer(500, true, true, false))
            .build()
    }

    @Provides
    @Singleton
    @NonNull
    fun provideImageLoader(): ImageLoader {
        val imageLoader = ImageLoader.getInstance()
        if (!imageLoader.isInited) {
            val config = ImageLoaderConfiguration.Builder(context)
                .diskCacheSize(10 * 1024 * 1024) // 10 Mb
                .memoryCache(LruMemoryCache(2 * 1024 * 1024)) // 2 Mb
                .defaultDisplayImageOptions(defaultOptions)
                .build()
            imageLoader.init(config)
        }
        return imageLoader
    }
}