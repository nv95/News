package com.vjettest.news.core

class PagedList <E> : ArrayList<E>() {

    var hasNextPage = true
        private set

    fun appendPage(items: List<E>) {
        if (items.isEmpty()) {
            hasNextPage = false
        } else {
            addAll(items)
        }
    }

    override fun clear() {
        super.clear()
        hasNextPage = true
    }
}