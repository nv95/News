package com.vjettest.news.common.lists

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.vjettest.news.R

class LoadingViewHolder(itemView: View) : AppBaseViewHolder<Boolean>(itemView) {

    private val progressBar = itemView.findViewById<ProgressBar>(R.id.progressBar)

    override fun bind(data: Boolean) {
        progressBar.visibility = if (data) View.VISIBLE else View.INVISIBLE
    }

    companion object {

        fun create(parent: ViewGroup) = LoadingViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_loading, parent, false)
        )
    }
}