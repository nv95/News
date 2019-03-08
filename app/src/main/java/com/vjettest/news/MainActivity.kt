package com.vjettest.news

import android.os.Bundle
import com.vjettest.news.common.AppBaseActivity
import com.vjettest.news.common.AppBaseFragment
import com.vjettest.news.news_list.trending.TrendingTabsFragment

class MainActivity : AppBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val fragment = TrendingTabsFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.content, fragment)
            .runOnCommit {
                setToolbarFromFragment(fragment)
            }
            .commit()
    }

    private fun setToolbarFromFragment(fragment: AppBaseFragment?) {
        setSupportActionBar(fragment?.supportToolbar)
    }
}