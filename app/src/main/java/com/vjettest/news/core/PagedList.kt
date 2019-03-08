package com.vjettest.news.core

class PagedList<E> : ArrayList<E>() {

    var total = 0

    val hasNextPage
        get() = total != 0 && size < total

    override fun clear() {
        super.clear()
        total = 0
    }
}