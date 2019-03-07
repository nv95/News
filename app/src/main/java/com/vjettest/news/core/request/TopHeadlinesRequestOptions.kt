package com.vjettest.news.core.request

import com.vjettest.news.core.Category

class TopHeadlinesRequestOptions : RequestOptions() {

    var country by prop({ it }, { it })

    var category by prop({
        Category.valueOf(it)
    }, {
        it.value
    })
}