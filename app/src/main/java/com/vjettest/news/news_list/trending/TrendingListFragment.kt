package com.vjettest.news.news_list.trending

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import com.vjettest.news.App
import com.vjettest.news.R
import com.vjettest.news.common.lists.PaginationHelper
import com.vjettest.news.core.Category
import com.vjettest.news.core.model.Article
import com.vjettest.news.core.model.NewsList
import com.vjettest.news.core.network.NewsApiService
import com.vjettest.news.core.request.TopHeadlinesRequestOptions
import com.vjettest.news.news_list.BaseListFragment
import com.vjettest.news.news_list.NewsListAdapter
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class TrendingListFragment : BaseListFragment<Article, TopHeadlinesRequestOptions>(), Observer<NewsList> {

    @Inject
    lateinit var apiService: NewsApiService

    override val options = TopHeadlinesRequestOptions().apply {
        country = "ua"
    }

    override val layoutId = R.layout.fragment_simple_list

    private lateinit var adapter: NewsListAdapter
    private var currentWorker: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        options["category"] = arguments?.getString("category") ?: Category.GENERAL.value
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        App.component.inject(this)

        buttonRetry.setOnClickListener {
            load()
        }
        swipeRefreshLayout.setOnRefreshListener {
            load()
        }

        adapter = NewsListAdapter(dataset)
        recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        recyclerView.addOnScrollListener(PaginationHelper(this))
        recyclerView.adapter = adapter

        options.country = "ru"
        load()
    }

    override fun onDestroy() {
        currentWorker?.takeIf { !it.isDisposed }?.dispose()
        super.onDestroy()
    }

    override fun load() {
        swipeRefreshLayout.isRefreshing = dataset.isEmpty()
        adapter.isLoading = true
        layoutError.visibility = View.GONE
        currentWorker?.takeIf { !it.isDisposed }?.dispose()
        apiService.getTopHeadlines(options)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this)
    }

    /**
     * Loading callback
     */

    override fun onComplete() {
        adapter.isLoading = false
        swipeRefreshLayout.isRefreshing = false
    }

    override fun onSubscribe(d: Disposable) {
        currentWorker = d
    }

    override fun onNext(t: NewsList) {
        val startPos = dataset.size
        dataset += t.articles
        dataset.total = t.totalResults
        if (startPos == 0) {
            adapter.notifyDataSetChanged()
        } else {
            adapter.notifyItemRangeInserted(startPos, t.articles.size)
        }
    }

    override fun onError(e: Throwable) {
        e.printStackTrace()
        textViewError.text = e.message
        layoutError.visibility = View.VISIBLE
    }

    override fun isLoading() = adapter.isLoading
}