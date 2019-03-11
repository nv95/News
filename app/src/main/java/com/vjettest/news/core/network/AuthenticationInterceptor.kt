package com.vjettest.news.core.network

import okhttp3.Interceptor
import okhttp3.Response

/**
 * Adds api key into request headers
 */
internal class AuthenticationInterceptor(private val apiKey: String) : Interceptor {

    @Throws(Exception::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val request = original.newBuilder()
            .header("X-Api-Key", apiKey)
            .method(original.method(), original.body())
            .build()
        return chain.proceed(request)
    }
}