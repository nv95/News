package com.vjettest.news.core.network

import com.vjettest.news.core.model.NewsList
import com.vjettest.news.core.request.RequestOptions
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface NewsApiService {

    @GET("top-headlines")
    fun getTopHeadlines(@QueryMap(encoded=true) options: RequestOptions): Observable<NewsList>

    @GET("everything")
    fun getEverything(@QueryMap(encoded=true) options: RequestOptions): Observable<NewsList>
}