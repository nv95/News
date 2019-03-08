package com.vjettest.news.news_list.trending

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.vjettest.news.R
import com.vjettest.news.common.AppBaseFragment
import com.vjettest.news.core.Category

class TrendingTabsFragment : AppBaseFragment() {

    override val layoutId = R.layout.fragment_tabs

    override val supportToolbar: Toolbar by bindView(R.id.toolbar)
    private val tabLayout by bindView<TabLayout>(R.id.tabLayout)
    private val pager by bindView<ViewPager>(R.id.pager)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        pager.adapter = TrendingTabsAdapter(fragmentManager, arrayOf(
            Category.GENERAL,
            Category.BUSINESS,
            Category.ENTERTAINMENT,
            Category.SCIENCE,
            Category.SPORTS
        ))
        tabLayout.setupWithViewPager(pager)
    }
}