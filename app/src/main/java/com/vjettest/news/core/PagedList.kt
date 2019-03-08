package com.vjettest.news.core

import android.os.Bundle
import android.os.Parcelable

class PagedList<E:Parcelable> : ArrayList<E>() {

    var total = 0

    val hasNextPage
        get() = total != 0 && size < total

    override fun clear() {
        super.clear()
        total = 0
    }

    fun readFromBundle(bundle: Bundle, key: String) {
        bundle.getParcelableArrayList<E>(key)?.let {
            addAll(it)
        }
        total = bundle.getInt(key + KEY_SUFFIX_TOTAL, 0)
    }

    fun writeToBundle(bundle: Bundle, key: String) {
        bundle.putParcelableArrayList(key, this)
        bundle.putInt(key + KEY_SUFFIX_TOTAL, total)
    }

    companion object {
        private const val KEY_SUFFIX_TOTAL = "_total"
    }
}