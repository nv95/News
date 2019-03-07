package com.vjettest.news.core.network

import com.vjettest.news.core.model.NewsList
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {

    @GET("top-headlines")
    fun getTopHeadlines(@Query("country") country: String): Observable<NewsList>
}