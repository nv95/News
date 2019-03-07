package com.vjettest.news.core.network

import com.vjettest.news.core.model.NewsList
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface NewsApiService {

    @GET("top-headlines")
    fun getTopHeadlines(@QueryMap(encoded=true) options: Map<String, String>): Observable<NewsList>
}