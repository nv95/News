package com.vjettest.news.news_list

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.vjettest.news.App
import com.vjettest.news.R
import com.vjettest.news.common.AppBaseActivity
import com.vjettest.news.common.lists.PaginationHelper
import com.vjettest.news.core.Category
import com.vjettest.news.core.PagedList
import com.vjettest.news.core.model.Article
import com.vjettest.news.core.model.NewsList
import com.vjettest.news.core.network.NewsApiService
import com.vjettest.news.core.request.TopHeadlinesRequestOptions
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class NewsListActivity : AppBaseActivity(), Observer<NewsList>, PaginationHelper.Callback {

    @Inject
    lateinit var apiService: NewsApiService

    private val recyclerView by bindView<RecyclerView>(R.id.recyclerView)
    private val swipeRefreshLayout by bindView<SwipeRefreshLayout>(R.id.swipeRefreshLayout)
    private val textViewError by bindView<TextView>(R.id.textView_error)
    private val layoutError by bindView<LinearLayout>(R.id.layout_error)
    private val buttonRetry by bindView<Button>(R.id.button_retry)

    private lateinit var adapter: NewsListAdapter
    private var currentWorker: Disposable? = null

    private val options = TopHeadlinesRequestOptions()
    private val dataset = PagedList<Article>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_newslist)
        App.component.inject(this)
        setSupportActionBar(toolbar)

        buttonRetry.setOnClickListener {
            load()
        }
        swipeRefreshLayout.setOnRefreshListener {
            load()
        }

        adapter = NewsListAdapter(dataset)
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        recyclerView.addOnScrollListener(PaginationHelper(this))
        recyclerView.adapter = adapter

        options.country = "ru"
        options.category = Category.GENERAL
        load()
    }

    override fun onDestroy() {
        currentWorker?.takeIf { !it.isDisposed }?.dispose()
        super.onDestroy()
    }

    private fun load() {
        swipeRefreshLayout.isRefreshing = (options.page == 0)
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

    /**
     * Pagination
     */

    override fun isLoading() = adapter.isLoading

    override fun isLastPage() = !dataset.hasNextPage

    override fun onNextPage() {
        options.page = (options.page ?: 1) + 1
        load()
    }
}