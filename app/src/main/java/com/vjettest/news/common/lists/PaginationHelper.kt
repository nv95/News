package com.vjettest.news.common.lists

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PaginationHelper(private val callback: Callback) : RecyclerView.OnScrollListener() {

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        val layoutManager = (recyclerView.layoutManager as? LinearLayoutManager) ?: return
        val visibleItemCount = layoutManager.childCount
        val totalItemCount = layoutManager.itemCount
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

        if (!callback.isLoading() && !callback.isLastPage()) {
            if ((visibleItemCount + firstVisibleItemPosition) >=
                totalItemCount && firstVisibleItemPosition >= 0
            ) {
                callback.onNextPage()
            }
        }
    }

    interface Callback {

        fun isLoading(): Boolean

        fun isLastPage(): Boolean

        fun onNextPage()
    }
}