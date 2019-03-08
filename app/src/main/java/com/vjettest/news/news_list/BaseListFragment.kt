package com.vjettest.news.news_list

import android.os.Bundle
import android.os.Parcelable
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.vjettest.news.R
import com.vjettest.news.common.AppBaseFragment
import com.vjettest.news.common.lists.PaginationHelper
import com.vjettest.news.core.PagedList
import com.vjettest.news.core.request.RequestOptions

abstract class BaseListFragment<E : Parcelable, O : RequestOptions> : AppBaseFragment(),
    PaginationHelper.Callback {

    protected abstract val options: O

    protected val dataset = PagedList<E>()

    protected val recyclerView by bindView<RecyclerView>(R.id.recyclerView)
    protected val swipeRefreshLayout by bindView<SwipeRefreshLayout>(R.id.swipeRefreshLayout)
    protected val textViewError by bindView<TextView>(R.id.textView_error)
    protected val layoutError by bindView<LinearLayout>(R.id.layout_error)
    protected val buttonRetry by bindView<Button>(R.id.button_retry)

    /**
     * Lifecycle
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.let { state ->
            state.getBundle(STATE_KEY_OPTIONS)?.let { bundle ->
                bundle.keySet().forEach { key ->
                    bundle.getString(key)?.let { options[key] = it }
                }
            }
            dataset.readFromBundle(state, STATE_KEY_DATASET)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val bundle = Bundle(options.size)
        options.forEach { entry ->
            bundle.putString(entry.key, entry.value)
        }
        outState.putBundle(STATE_KEY_OPTIONS, bundle)
        dataset.writeToBundle(bundle, STATE_KEY_DATASET)
    }

    /**
     * Pagination
     */

    override fun isLastPage() = !dataset.hasNextPage

    override fun onNextPage() {
        options.page = (options.page ?: 1) + 1
        load()
    }

    /**
     * Loading
     */

    abstract fun load()

    companion object {

        private const val STATE_KEY_DATASET = "dataset"
        private const val STATE_KEY_OPTIONS = "options"
    }
}