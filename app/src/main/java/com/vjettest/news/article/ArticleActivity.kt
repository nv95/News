package com.vjettest.news.article

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.ShareActionProvider
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.core.view.MenuItemCompat
import androidx.core.widget.TextViewCompat
import com.google.android.material.snackbar.Snackbar
import com.vjettest.news.App
import com.vjettest.news.R
import com.vjettest.news.common.AppBaseActivity
import com.vjettest.news.common.formatRelative
import com.vjettest.news.common.images.ImagesStore
import com.vjettest.news.common.plusAssign
import com.vjettest.news.common.setImageAsync
import com.vjettest.news.core.database.AppDatabase
import com.vjettest.news.core.model.Article
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ArticleActivity : AppBaseActivity() {

    @Inject
    lateinit var database: AppDatabase

    private val textViewTitle by bindView<TextView>(R.id.textView_title)
    private val textViewDescription by bindView<TextView>(R.id.textView_description)
    private val textViewDate by bindView<TextView>(R.id.textView_date)
    private val textViewContent by bindView<TextView>(R.id.textView_content)
    private val imageView by bindView<ImageView>(R.id.imageView)
    private val buttonFavourite by bindView<AppCompatButton>(R.id.button_favourite)
    private val buttonOpen by bindView<AppCompatButton>(R.id.button_open)

    private val disposables = CompositeDisposable()

    private lateinit var article: Article

    private var isFavoured = false
        set(value) {
            field = value
            if (value) {
                buttonFavourite.setText(R.string.in_favourites)
                TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    buttonFavourite,
                    R.drawable.ic_star_black_24dp,
                    0, 0, 0
                )
            } else {
                buttonFavourite.setText(R.string.favourite)
                TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    buttonFavourite,
                    R.drawable.ic_star_border_black_24dp,
                    0, 0, 0
                )
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article)
        App.component.inject(this)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        article = intent.getParcelableExtra(EXTRA_ARTICLE)
        buttonFavourite.setOnClickListener {
            if (isFavoured) {
                removeFromFavourites()
            } else {
                addToFavourites()
            }
        }
        buttonOpen.setOnClickListener {
            val intent = CustomTabsIntent.Builder()
                .addDefaultShareMenuItem()
                .setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary))
                .setShowTitle(true)
                .build()
            intent.launchUrl(this, Uri.parse(article.url))

        }
    }

    override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        // Fill layout with data
        title = article.title
        textViewTitle.text = article.title
        textViewDescription.text = article.description ?: article.title
        textViewDate.text = article.publishedAt.formatRelative()
        textViewContent.text = article.content
        if (article.urlToImage != null) {
            imageView.setImageAsync(article.urlToImage)
        } else {
            imageView.visibility = View.GONE
        }
        //Check if article in favourites
        disposables += database.articlesDao().findByUrl(article.url)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                isFavoured = true
            }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.options_article, menu)
        val item = menu.findItem(R.id.menu_item_share)
        val shareProvider = MenuItemCompat.getActionProvider(item) as ShareActionProvider
        shareProvider.setShareIntent(Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, "${article.title} - ${article.url}")
        })
        return super.onCreateOptionsMenu(menu)
    }

    /**
     * Favourites
     */

    private fun addToFavourites() {
        buttonFavourite.isEnabled = false
        disposables += Observable.fromCallable {
            article.urlToImage = ImagesStore(this).put(article.urlToImage)?.let {
                "file://$it"
            }
            database.articlesDao().insert(article)
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                isFavoured = true
                buttonFavourite.isEnabled = true
                Snackbar.make(textViewContent, R.string.added_to_favourites, Snackbar.LENGTH_SHORT).show()
            }, { e ->
                e.printStackTrace()
                Snackbar.make(textViewContent, R.string.unable_favourite_article, Snackbar.LENGTH_SHORT).show()
            })
    }

    private fun removeFromFavourites() {
        buttonFavourite.isEnabled = false
        disposables += Observable.fromCallable {
            database.articlesDao().delete(article)
            ImagesStore(this).delete(article.urlToImage)
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                isFavoured = false
                buttonFavourite.isEnabled = true
                Snackbar.make(textViewContent, R.string.removed_from_favourites, Snackbar.LENGTH_SHORT).show()
            }, { e ->
                e.printStackTrace()
            })
    }

    companion object {

        private const val EXTRA_ARTICLE = "article"

        fun open(context: Context, article: Article) {
            context.startActivity(
                Intent(context, ArticleActivity::class.java)
                    .putExtra(EXTRA_ARTICLE, article)
            )
        }
    }
}