package com.vjettest.news

import com.vjettest.news.core.network.NetworkModule
import com.vjettest.news.news_list.NewsListActivity
import dagger.Component
import javax.inject.Singleton

@Component(modules = [NetworkModule::class])
@Singleton
interface AppComponent {

    fun inject(newsListActivity: NewsListActivity)
}