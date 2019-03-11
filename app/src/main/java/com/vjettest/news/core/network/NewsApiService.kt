package com.vjettest.news.core.network

import com.vjettest.news.core.Category
import com.vjettest.news.core.model.NewsList
import com.vjettest.news.core.model.SourcesList
import com.vjettest.news.core.network.options.RequestOptions
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface NewsApiService {

    @GET("top-headlines")
    fun getTopHeadlines(@QueryMap(encoded = true) options: RequestOptions): Observable<NewsList>

    @GET("everything")
    fun getEverything(@QueryMap(encoded = true) options: RequestOptions): Observable<NewsList>

    @GET("sources")
    fun getSources(
        @Query("language") language: String? = null,
        @Query("country") country: String? = null,
        @Query("category") category: Category? = null
    ): Observable<SourcesList>
}