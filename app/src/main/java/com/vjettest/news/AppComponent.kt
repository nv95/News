package com.vjettest.news

import com.nostra13.universalimageloader.core.ImageLoader
import com.vjettest.news.article.ArticleActivity
import com.vjettest.news.common.images.ImageLoaderModule
import com.vjettest.news.core.database.AppDatabase
import com.vjettest.news.core.database.DatabaseModule
import com.vjettest.news.core.network.NetworkModule
import com.vjettest.news.core.network.NewsApiService
import com.vjettest.news.core.preferences.AppPreferences
import com.vjettest.news.core.preferences.PreferencesModule
import com.vjettest.news.news_list.NewsListFragment
import com.vjettest.news.news_list.favourites.FavouritesListFragment
import com.vjettest.news.news_list.trending.TrendingListFragment
import dagger.Component
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Component(modules = [NetworkModule::class, ImageLoaderModule::class, DatabaseModule::class, PreferencesModule::class])
@Singleton
interface AppComponent {

    fun inject(mainActivity: MainActivity)
    fun inject(articleActivity: ArticleActivity)
    fun inject(newsListFragment: NewsListFragment)
    fun inject(trendingListFragment: TrendingListFragment)
    fun inject(favouritesListFragment: FavouritesListFragment)

    fun getApiService(): NewsApiService
    fun getImageLoader(): ImageLoader
    fun getDatabase(): AppDatabase
    fun getHttpClient(): OkHttpClient
    fun getPreferences(): AppPreferences
}