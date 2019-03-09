package com.vjettest.news.news_list

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vjettest.news.common.AppBaseViewHolder
import com.vjettest.news.common.LoadingViewHolder
import com.vjettest.news.common.lists.HeaderViewHolder
import com.vjettest.news.common.withoutTime
import com.vjettest.news.core.model.Article
import java.util.*

class GroupingNewsAdapter : RecyclerView.Adapter<AppBaseViewHolder<*>>() {

    private val dataset = ArrayList<Any>()

    fun append(articles: List<Article>): Int {
        val lastSize = dataset.size
        val firstArticle = articles.first()
        if ((dataset.lastOrNull() as? Article)?.publishedAt?.withoutTime != firstArticle.publishedAt.withoutTime) {
            dataset += firstArticle.publishedAt
        }
        dataset += firstArticle
        for (i in 1 until articles.size) {
            val article = articles[i]
            if (articles[i-1].publishedAt.withoutTime != article.publishedAt.withoutTime) {
                dataset += article.publishedAt
            }
            dataset += article
        }
        return dataset.size - lastSize
    }

    fun clear() = dataset.clear()

    val size
        get() = dataset.size

    var isLoading = false
        set(value) {
            field = value
            notifyItemChanged(dataset.size)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        ITEM_TYPE_HEADER -> HeaderViewHolder.create(parent)
        ITEM_TYPE_ARTICLE -> ArticleHolder.create(parent)
        else -> LoadingViewHolder.create(parent)
    }

    override fun getItemCount() = if (dataset.isEmpty()) {
        0
    } else {
        dataset.size + 1  // list size plus isLoading footer
    }

    override fun onBindViewHolder(holder: AppBaseViewHolder<*>, position: Int) {
        when (holder) {
            is HeaderViewHolder -> holder.bind(dataset[position] as Date)
            is ArticleHolder -> holder.bind(dataset[position] as Article)
            is LoadingViewHolder -> holder.bind(isLoading && dataset.isNotEmpty())
        }
    }

    override fun getItemViewType(position: Int) = when (dataset.getOrNull(position)) {
        is Article -> ITEM_TYPE_ARTICLE
        is Date -> ITEM_TYPE_HEADER
        null -> ITEM_TYPE_LOADING
        else -> throw RuntimeException()
    }

    companion object {
        private const val ITEM_TYPE_ARTICLE = 0
        private const val ITEM_TYPE_HEADER = 1
        private const val ITEM_TYPE_LOADING = 2
    }
}