package com.vjettest.news.news_list.favourites

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.vjettest.news.App
import com.vjettest.news.R
import com.vjettest.news.common.AppBaseFragment
import com.vjettest.news.common.getErrorMessage
import com.vjettest.news.core.database.AppDatabase
import com.vjettest.news.core.model.Article
import com.vjettest.news.news_list.NewsListAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class FavouritesListFragment : AppBaseFragment() {

    @Inject
    lateinit var database: AppDatabase

    override val layoutId = R.layout.fragment_list

    override val supportToolbar by bindView<Toolbar>(R.id.toolbar)
    private val recyclerView by bindView<RecyclerView>(R.id.recyclerView)
    private val textViewError by bindView<TextView>(R.id.textView_error)
    private val layoutError by bindView<LinearLayout>(R.id.layout_error)
    private val buttonRetry by bindView<Button>(R.id.button_retry)
    private val texViewHolder by bindView<TextView>(R.id.textView_holder)

    private val dataset = ArrayList<Article>()
    private lateinit var adapter: NewsListAdapter
    private var currentWorker: Disposable? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        texViewHolder.setText(R.string.no_favourites_found)
    }

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
        currentWorker = database.articlesDao().getAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::onNext, this::onError, this::onComplete)
    }

    /**
     * Loading callback
     */

    private fun onComplete() {
        adapter.isLoading = false
        texViewHolder.visibility = if (dataset.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun onNext(t: List<Article>) {
        dataset.clear()
        dataset += t
        adapter.notifyDataSetChanged()
    }

    private fun onError(e: Throwable) {
        texViewHolder.visibility = View.GONE
        if (dataset.isEmpty()) {
            textViewError.text = context?.getErrorMessage(e)
            layoutError.visibility = View.VISIBLE
        } else {
            Snackbar.make(recyclerView, requireContext().getErrorMessage(e), Snackbar.LENGTH_SHORT)
                .setAction(R.string.retry) { load() }
                .show()
        }
        e.printStackTrace()
    }


}