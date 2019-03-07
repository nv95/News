package com.vjettest.news.core.network

import okhttp3.Interceptor
import okhttp3.Response

internal class AuthenticationInterceptor(private val apiKey: String) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val request = original.newBuilder()
            .header("X-Api-Key", apiKey)
            .method(original.method(), original.body())
            .build()
        return chain.proceed(request)
    }
}