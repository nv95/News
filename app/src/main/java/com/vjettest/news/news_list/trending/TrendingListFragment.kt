package com.vjettest.news.news_list.trending

import android.os.Bundle
import com.vjettest.news.R
import com.vjettest.news.core.Category
import com.vjettest.news.core.network.options.TopHeadlinesRequestOptions
import com.vjettest.news.news_list.NewsListFragment

class TrendingListFragment : NewsListFragment() {

    override val options = TopHeadlinesRequestOptions().apply {
        country = "us"
    }

    override val layoutId = R.layout.fragment_simple_list

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        options["category"] = arguments?.getString("category") ?: Category.GENERAL.value
        setHasOptionsMenu(false)
    }

    override fun onLoadContent() = apiService.getTopHeadlines(options)
}