package com.vjettest.news.article

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.vjettest.news.R
import com.vjettest.news.common.AppBaseActivity
import com.vjettest.news.common.formatRelative
import com.vjettest.news.common.setImageAsync
import com.vjettest.news.core.model.Article

class ArticleActivity : AppBaseActivity() {

    private val textViewDescription by bindView<TextView>(R.id.textView_description)
    private val textViewDate by bindView<TextView>(R.id.textView_date)
    private val textViewContent by bindView<TextView>(R.id.textView_content)
    private val imageView by bindView<ImageView>(R.id.imageView)

    private lateinit var article: Article

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        article = intent.getParcelableExtra(EXTRA_ARTICLE)

    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        title = article.title
        textViewDescription.text = article.description
        textViewDate.text = article.publishedAt.formatRelative(this)
        textViewContent.text = article.content
        imageView.setImageAsync(article.urlToImage)
    }

    companion object {

        private const val EXTRA_ARTICLE = "article";

        fun open(context: Context, article: Article) {
            context.startActivity(Intent(context, ArticleActivity::class.java)
                .putExtra(EXTRA_ARTICLE, article))
        }
    }
}