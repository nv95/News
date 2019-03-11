package com.vjettest.news.news_list.trending

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.vjettest.news.core.Category

class TrendingTabsAdapter(fm: FragmentManager?, private val categories: Array<Pair<Category, CharSequence>>) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int) = TrendingListFragment().apply {
        val args = Bundle(1)
        args.putString("category", categories[position].first.value)
        arguments = args
    }

    override fun getCount() = categories.size

    override fun getPageTitle(position: Int) = categories[position].second
}