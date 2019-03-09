package com.vjettest.news.common.lists

import android.view.View
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.RecyclerView

abstract class AppBaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var boundData: T? = null
        private set

    @CallSuper
    open fun bind(data: T) {
        boundData = data
    }
}