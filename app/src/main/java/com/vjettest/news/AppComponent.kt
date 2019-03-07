package com.vjettest.news

import com.vjettest.news.core.network.NetworkModule
import dagger.Component
import javax.inject.Singleton

@Component(modules = [NetworkModule::class])
@Singleton
interface AppComponent {

    fun inject(activity: MainActivity)
}