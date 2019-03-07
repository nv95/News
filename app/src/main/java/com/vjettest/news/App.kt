package com.vjettest.news

import android.app.Application
import com.vjettest.news.core.network.NetworkModule

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        component = DaggerAppComponent.builder()
            .networkModule(NetworkModule())
            .build()
    }

    companion object {

        lateinit var component: AppComponent
            private set
    }
}