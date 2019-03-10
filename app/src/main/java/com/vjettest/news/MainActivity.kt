package com.vjettest.news

import android.os.Bundle
import android.view.MenuItem
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.vjettest.news.common.AppBaseActivity
import com.vjettest.news.common.AppBaseFragment
import com.vjettest.news.common.plusAssign
import com.vjettest.news.core.database.AppDatabase
import com.vjettest.news.core.model.SourceInfo
import com.vjettest.news.core.network.NewsApiService
import com.vjettest.news.core.network.options.EverythingRequestOptions
import com.vjettest.news.core.preferences.AppPreferences
import com.vjettest.news.news_list.NewsListFragment
import com.vjettest.news.news_list.favourites.FavouritesListFragment
import com.vjettest.news.news_list.trending.TrendingTabsFragment
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MainActivity : AppBaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    @Inject
    lateinit var apiService: NewsApiService
    @Inject
    lateinit var database: AppDatabase
    @Inject
    lateinit var preferences: AppPreferences

    private val drawer by bindView<DrawerLayout>(R.id.drawer)
    private val navigationView by bindView<NavigationView>(R.id.nav_view)

    private val disposables = CompositeDisposable()
    private var sources = emptyList<SourceInfo>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        App.component.inject(this)
        navigationView.setNavigationItemSelectedListener(this)

        setActiveFragment(TrendingTabsFragment(), getString(R.string.top_headlines))
        loadSourcesFromCache()
    }

    override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
    }

    private fun setActiveFragment(fragment: AppBaseFragment, fragmentTitle: CharSequence? = null) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.content, fragment)
            .runOnCommit {
                setSupportActionBar(fragment.supportToolbar)
                supportActionBar?.apply {
                    setDisplayHomeAsUpEnabled(true)
                    setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp)
                    subtitle = fragmentTitle
                }
            }
            .commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        item.isChecked = true
        setActiveFragment(when (item.itemId) {
            R.id.nav_top_headlines -> TrendingTabsFragment()
            R.id.nav_favourites -> FavouritesListFragment()
            R.id.nav_all -> NewsListFragment().apply {
                arguments = EverythingRequestOptions().run {
                    sortBy = preferences.defaultSortOrder
                    language = "en"
                    q = "*"
                    toBundle()
                }
            }
            else -> NewsListFragment().apply {
                arguments = EverythingRequestOptions().run {
                    sortBy = preferences.defaultSortOrder
                    sources = listOf(this@MainActivity.sources[item.order].id)
                    toBundle()
                }
            }
        }, item.title)
        drawer.closeDrawers()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?) = when (item?.itemId) {
        android.R.id.home -> {
            drawer.openDrawer(GravityCompat.START)
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun loadSourcesFromCache() {
        disposables += database.sourcesDao().getAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                fillSourcesMenu(result)
                loadSourcesFromNetwork()
            }, { e ->
                e.printStackTrace()
                loadSourcesFromNetwork()
            })
    }

    private fun loadSourcesFromNetwork() {
        disposables += apiService.getSources(language = "en", country = "us")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doAfterNext {
                disposables += Completable.fromAction { database.sourcesDao().replaceWith(it.sources) }
                    .subscribeOn(Schedulers.io())
                    .subscribe()
            }
            .subscribe({ result ->
                fillSourcesMenu(result.sources)
            }, { e ->
                e.printStackTrace()
            })
    }

    private fun fillSourcesMenu(sources: List<SourceInfo>) {
        this.sources = sources
        val navMenu = navigationView.menu.findItem(R.id.nav_sources).subMenu
        navMenu.removeGroup(R.id.group_sources)
        sources.forEachIndexed { i, it ->
            navMenu.add(R.id.group_sources, it.hashCode(), i, it.name)
        }
        navMenu.setGroupCheckable(R.id.group_sources, true, true)
    }
}