package com.vjettest.news.common

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class AppBaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
    
    abstract fun bind(data: T)
}