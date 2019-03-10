package com.vjettest.news

import android.app.Application
import com.vjettest.news.common.images.ImageLoaderModule
import com.vjettest.news.core.database.DatabaseModule
import com.vjettest.news.core.network.NetworkModule
import com.vjettest.news.core.preferences.PreferencesModule

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        component = DaggerAppComponent.builder()
            .networkModule(NetworkModule())
            .imageLoaderModule(ImageLoaderModule(this))
            .databaseModule(DatabaseModule(this))
            .preferencesModule(PreferencesModule(this))
            .build()
    }

    companion object {

        lateinit var component: AppComponent
            private set
    }
}