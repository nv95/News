package com.vjettest.news.common.lists

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PaginationHelper(private val recyclerView: RecyclerView,
                       private val callback: Callback) : RecyclerView.OnScrollListener() {

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        val lastItem = (recyclerView.layoutManager as? LinearLayoutManager)?.findLastVisibleItemPosition() ?: return
        val total = recyclerView.adapter?.itemCount ?: return
        if (lastItem >= total - SCROLL_END_THRESHOLD) {
            if (!callback.isLoading() && !callback.isLastPage()) {
                callback.onNextPage()
            }
        }
    }

    companion object {
        private const val SCROLL_END_THRESHOLD = 3
    }

    interface Callback {

        fun isLoading(): Boolean

        fun isLastPage(): Boolean

        fun onNextPage()
    }
}