package com.vjettest.news.core.model

import com.vjettest.news.core.Status

data class NewsList(
    var status: Status,
    var totalResults: Int,
    var articles: List<Article>
)