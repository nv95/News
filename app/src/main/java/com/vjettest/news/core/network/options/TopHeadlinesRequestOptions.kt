package com.vjettest.news.core.network.options

import android.os.Bundle
import com.vjettest.news.core.Category

class TopHeadlinesRequestOptions : RequestOptions() {

    var country by prop({ it }, { it })

    var category by prop({
        Category.valueOf(it)
    }, {
        it.value
    })

    override fun fromBundle(bundle: Bundle) {
        super.fromBundle(bundle)
        fromBundle(bundle, "country")
        fromBundle(bundle, "category")
    }
}