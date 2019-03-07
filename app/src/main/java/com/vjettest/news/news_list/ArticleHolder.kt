package com.vjettest.news.news_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.vjettest.news.R
import com.vjettest.news.common.AppBaseViewHolder
import com.vjettest.news.core.model.Article

class ArticleHolder(itemView: View) : AppBaseViewHolder<Article>(itemView) {

    private val textViewTitle = itemView.findViewById<TextView>(R.id.textView_title)
    private val textViewDescription = itemView.findViewById<TextView>(R.id.textView_description)

    override fun bind(data: Article) {
        textViewTitle.text = data.title
        textViewDescription.text = data.description
    }

    companion object {

        fun create(parent: ViewGroup) = ArticleHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_article, parent, false)
        )
    }
}