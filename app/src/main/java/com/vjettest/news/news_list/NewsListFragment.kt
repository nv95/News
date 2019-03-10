package com.vjettest.news.news_list

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DividerItemDecoration
import com.vjettest.news.App
import com.vjettest.news.R
import com.vjettest.news.common.lists.PaginationHelper
import com.vjettest.news.core.model.Article
import com.vjettest.news.core.model.NewsList
import com.vjettest.news.core.network.NewsApiService
import com.vjettest.news.core.network.options.EverythingRequestOptions
import com.vjettest.news.core.network.options.RequestOptions
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import javax.inject.Inject

open class NewsListFragment : BaseListFragment<Article>(), Observer<NewsList> {

    @Inject
    lateinit var apiService: NewsApiService

    override val options = EverythingRequestOptions() as RequestOptions

    override val layoutId = R.layout.fragment_list_news
    override val supportToolbar by bindView<Toolbar>(R.id.toolbar)

    private lateinit var adapter: NewsListAdapter
    private var currentWorker: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            options.fromBundle(it)
        }
        setHasOptionsMenu(true)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        App.component.inject(this)

        buttonRetry.setOnClickListener {
            load()
        }
        swipeRefreshLayout.setOnRefreshListener {
            options.page = RequestOptions.PAGE_DEFAULT
            load()
        }

        adapter = NewsListAdapter(dataset)
        recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        recyclerView.addOnScrollListener(PaginationHelper(this))
        recyclerView.adapter = adapter

        load()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_FILTER && resultCode == Activity.RESULT_OK && data != null) {
            FilterActivity.handleResult(data, options as EverythingRequestOptions)
            options.page = RequestOptions.PAGE_DEFAULT
            load()
        }
    }

    override fun onDestroy() {
        currentWorker?.takeIf { !it.isDisposed }?.dispose()
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.options_newslist, menu)
        menu?.findItem(R.id.sort_publishedAt)?.isChecked = (options as? EverythingRequestOptions)?.sortBy ==
                EverythingRequestOptions.SORT_BY_PUBLISHED_AT
        menu?.findItem(R.id.sort_popularity)?.isChecked = (options as? EverythingRequestOptions)?.sortBy ==
                EverythingRequestOptions.SORT_BY_POPULARITY
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when(item.itemId) {
        R.id.sort_publishedAt -> if (!item.isChecked) {
            (options as? EverythingRequestOptions)?.sortBy = EverythingRequestOptions.SORT_BY_PUBLISHED_AT
            App.component.getPreferences().defaultSortOrder = EverythingRequestOptions.SORT_BY_PUBLISHED_AT
            options.page = RequestOptions.PAGE_DEFAULT
            item.isChecked = true
            load()
            true
        } else false
        R.id.sort_popularity -> if (!item.isChecked) {
            (options as? EverythingRequestOptions)?.sortBy = EverythingRequestOptions.SORT_BY_POPULARITY
            App.component.getPreferences().defaultSortOrder = EverythingRequestOptions.SORT_BY_POPULARITY
            options.page = RequestOptions.PAGE_DEFAULT
            item.isChecked = true
            load()
            true
        } else false
        R.id.action_filter -> {
            FilterActivity.open(this, options as EverythingRequestOptions, REQUEST_CODE_FILTER)
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun load() {
        swipeRefreshLayout.isRefreshing = dataset.isEmpty() || options.page == RequestOptions.PAGE_DEFAULT
        adapter.isLoading = true
        layoutError.visibility = View.GONE
        currentWorker?.takeIf { !it.isDisposed }?.dispose()
        onLoadContent()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this)
    }

    protected open fun onLoadContent() = apiService.getEverything(options)

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
        if (options.page == RequestOptions.PAGE_DEFAULT) {
            dataset.clear()
        }
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

    companion object {

        private const val REQUEST_CODE_FILTER = 100
    }
}