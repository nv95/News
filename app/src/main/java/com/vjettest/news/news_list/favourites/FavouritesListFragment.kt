package com.vjettest.news.news_list.favourites

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.vjettest.news.App
import com.vjettest.news.R
import com.vjettest.news.common.AppBaseFragment
import com.vjettest.news.core.database.AppDatabase
import com.vjettest.news.core.model.Article
import com.vjettest.news.news_list.NewsListAdapter
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class FavouritesListFragment : AppBaseFragment(), Observer<List<Article>> {

    @Inject
    lateinit var database: AppDatabase

    override val layoutId = R.layout.fragment_list

    override val supportToolbar by bindView<Toolbar>(R.id.toolbar)
    protected val recyclerView by bindView<RecyclerView>(R.id.recyclerView)
    protected val textViewError by bindView<TextView>(R.id.textView_error)
    protected val layoutError by bindView<LinearLayout>(R.id.layout_error)
    protected val buttonRetry by bindView<Button>(R.id.button_retry)

    protected val dataset = ArrayList<Article>()
    private lateinit var adapter: NewsListAdapter
    private var currentWorker: Disposable? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        App.component.inject(this)

        buttonRetry.setOnClickListener {
            load()
        }

        adapter = NewsListAdapter(dataset, false)
        recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        recyclerView.adapter = adapter

        load()
    }

    override fun onDestroy() {
        currentWorker?.takeIf { !it.isDisposed }?.dispose()
        super.onDestroy()
    }

    /**
     * Content
     */

    private fun load() {
        layoutError.visibility = View.GONE
        currentWorker?.takeIf { !it.isDisposed }?.dispose()
        database.articlesDao().getAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this)
    }

    /**
     * Loading callback
     */

    override fun onComplete() {
        adapter.isLoading = false
    }

    override fun onSubscribe(d: Disposable) {
        currentWorker = d
    }

    override fun onNext(t: List<Article>) {
        dataset.clear()
        dataset += t
        adapter.notifyDataSetChanged()
    }

    override fun onError(e: Throwable) {
        e.printStackTrace()
        textViewError.text = e.message
        layoutError.visibility = View.VISIBLE
    }


}