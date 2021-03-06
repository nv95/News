package com.vjettest.news.news_list

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vjettest.news.common.lists.AppBaseViewHolder
import com.vjettest.news.common.lists.LoadingViewHolder
import com.vjettest.news.core.model.Article

class NewsListAdapter(private val dataset: List<Article>, private val paginate: Boolean = true) :
    RecyclerView.Adapter<AppBaseViewHolder<*>>() {

    var isLoading = false
        set(value) {
            if (paginate) {
                field = value
                notifyItemChanged(dataset.size)
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        ITEM_TYPE_ARTICLE -> ArticleHolder.create(parent)
        else -> LoadingViewHolder.create(parent)
    }

    override fun getItemCount() = if (dataset.isEmpty()) {
        0
    } else {
        dataset.size + if (paginate) 1 else 0 // list size plus isLoading footer
    }

    override fun onBindViewHolder(holder: AppBaseViewHolder<*>, position: Int) {
        when (holder) {
            is ArticleHolder -> holder.bind(dataset[position])
            is LoadingViewHolder -> holder.bind(isLoading && dataset.isNotEmpty())
        }
    }

    override fun getItemViewType(position: Int) = when (position) {
        in dataset.indices -> ITEM_TYPE_ARTICLE
        else -> ITEM_TYPE_LOADING
    }

    companion object {
        private const val ITEM_TYPE_ARTICLE = 0
        private const val ITEM_TYPE_LOADING = 1
    }
}