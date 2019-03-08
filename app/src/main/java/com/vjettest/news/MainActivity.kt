package com.vjettest.news

import android.os.Bundle
import com.vjettest.news.common.AppBaseActivity
import com.vjettest.news.common.AppBaseFragment
import com.vjettest.news.news_list.favourites.FavouritesListFragment

class MainActivity : AppBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setActiveFragment(FavouritesListFragment())
    }

    fun setActiveFragment(fragment: AppBaseFragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.content, fragment)
            .runOnCommit {
                setSupportActionBar(fragment.supportToolbar)
            }
            .commit()
    }
}