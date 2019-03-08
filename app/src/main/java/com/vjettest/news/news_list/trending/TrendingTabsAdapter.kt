package com.vjettest.news.news_list.trending

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.vjettest.news.core.Category

class TrendingTabsAdapter(fm: FragmentManager?, private val categories: Array<Category>) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int) = TrendingListFragment().apply {
        val args = Bundle(1)
        args.putString("category", categories[position].value)
        arguments = args
    }

    override fun getCount() = categories.size

    override fun getPageTitle(position: Int) = categories[position].value
}