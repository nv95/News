package com.vjettest.news.common.lists

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.vjettest.news.R
import com.vjettest.news.common.AppBaseViewHolder
import com.vjettest.news.common.formatDateRelative
import java.util.*

class HeaderViewHolder(itemView: View) : AppBaseViewHolder<Date>(itemView) {

    private val textView = itemView.findViewById<TextView>(R.id.textView)

    override fun bind(data: Date) {
        super.bind(data)
        textView.text = data.formatDateRelative(textView.context)
    }

    companion object {

        fun create(parent: ViewGroup) = HeaderViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_header, parent, false)
        )
    }
}