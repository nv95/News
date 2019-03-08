package com.vjettest.news

import com.nostra13.universalimageloader.core.ImageLoader
import com.vjettest.news.common.ImageLoaderModule
import com.vjettest.news.core.network.NetworkModule
import com.vjettest.news.core.network.NewsApiService
import com.vjettest.news.news_list.trending.TrendingListFragment
import dagger.Component
import javax.inject.Singleton

@Component(modules = [NetworkModule::class, ImageLoaderModule::class])
@Singleton
interface AppComponent {

    fun inject(trendingListFragment: TrendingListFragment)

    fun getApiService(): NewsApiService
    fun getImageLoader(): ImageLoader
}