package com.vjettest.news.news_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.vjettest.news.R
import com.vjettest.news.article.ArticleActivity
import com.vjettest.news.common.lists.AppBaseViewHolder
import com.vjettest.news.common.setImageAsync
import com.vjettest.news.core.model.Article

class ArticleHolder(itemView: View) : AppBaseViewHolder<Article>(itemView) {

    private val textViewTitle = itemView.findViewById<TextView>(R.id.textView_title)
    private val textViewDescription = itemView.findViewById<TextView>(R.id.textView_description)
    private val imageView = itemView.findViewById<ImageView>(R.id.imageView)

    init {
        itemView.setOnClickListener { view ->
            boundData?.let { data ->
                ArticleActivity.open(view.context, data)
            }
        }
    }

    override fun bind(data: Article) {
        super.bind(data)
        textViewTitle.text = data.title
        textViewDescription.text = data.description
        imageView.setImageAsync(data.urlToImage)
    }

    companion object {

        fun create(parent: ViewGroup) = ArticleHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_article, parent, false)
        )
    }
}