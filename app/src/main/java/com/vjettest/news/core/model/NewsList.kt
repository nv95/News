package com.vjettest.news.core.model

data class NewsList(
    var status: String,
    var totalResults: Int,
    var articles: List<Article>
)