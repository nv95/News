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
import com.vjettest.news.news_list.favourites.FavouritesListFragment
import com.vjettest.news.news_list.trending.TrendingTabsFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MainActivity : AppBaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    @Inject
    lateinit var apiService: NewsApiService
    @Inject
    lateinit var database: AppDatabase

    private val drawer by bindView<DrawerLayout>(R.id.drawer)
    private val navigationView by bindView<NavigationView>(R.id.nav_view)

    private val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        App.component.inject(this)
        navigationView.setNavigationItemSelectedListener(this)

        setActiveFragment(TrendingTabsFragment())
        loadSourcesFromCache()
    }

    override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
    }

    fun setActiveFragment(fragment: AppBaseFragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.content, fragment)
            .runOnCommit {
                setSupportActionBar(fragment.supportToolbar)
                supportActionBar?.apply {
                    setDisplayHomeAsUpEnabled(true)
                    setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp)
                }
            }
            .commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        item.isChecked = true
        drawer.closeDrawers()
        when(item.itemId) {
            R.id.nav_top_headlines ->setActiveFragment(TrendingTabsFragment())
            R.id.nav_favourites -> setActiveFragment(FavouritesListFragment())
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?) = when(item?.itemId) {
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
            .doAfterNext { database.sourcesDao().replaceWith(it.sources) }
            .subscribe({ result ->
                fillSourcesMenu(result.sources)
            }, { e ->
                e.printStackTrace()
            })
    }

    private fun fillSourcesMenu(sources: List<SourceInfo>) {
        val navMenu = navigationView.menu.findItem(R.id.nav_sources).subMenu
        navMenu.removeGroup(R.id.group_sources)
        sources.forEachIndexed { i, it ->
            navMenu.add(R.id.group_sources, it.hashCode(), i, it.name)
        }
        navMenu.setGroupCheckable(R.id.group_sources, true, true)
    }
}