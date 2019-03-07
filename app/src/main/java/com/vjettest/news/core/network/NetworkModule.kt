package com.vjettest.news.core.network

import androidx.annotation.NonNull
import com.vjettest.news.BuildConfig
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Singleton
    @NonNull
    fun provideApiService() = Retrofit.Builder()
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl("https://newsapi.org/v2/")
        .client(
            OkHttpClient.Builder()
                .addInterceptor(AuthenticationInterceptor(BuildConfig.API_KEY))
                .build()
        )
        .build()
        .create(NewsApiService::class.java)!!
}