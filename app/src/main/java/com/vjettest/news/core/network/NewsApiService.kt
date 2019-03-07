package com.vjettest.news.core.network

import com.vjettest.news.BuildConfig
import com.vjettest.news.core.model.NewsList
import io.reactivex.Observable
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {

    @GET("top-headlines")
    fun getTopHeadlines(@Query("country") country: String): Observable<NewsList>

    companion object Factory {

        fun create() = Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://newsapi.org/v2/")
            .client(createClient())
            .build()
            .create(NewsApiService::class.java)

        private fun createClient() = OkHttpClient.Builder()
            .addInterceptor(AuthenticationInterceptor(BuildConfig.API_KEY))
            .build()
    }
}