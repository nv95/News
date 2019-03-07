package com.vjettest.news.core.model

import java.util.*

data class Article(
    var source: Source,
    var author: String,
    var title: String,
    var description: String,
    var url: String,
    var urlToImage: String,
    var publishedAt: Date,
    var content: String
)