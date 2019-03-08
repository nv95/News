package com.vjettest.news.common

import android.content.Context
import android.text.format.DateUtils
import android.widget.ImageView
import com.vjettest.news.App
import com.vjettest.news.R
import java.util.*

fun Date.formatRelative(context: Context) = DateUtils.getRelativeTimeSpanString(context, this.time)

fun ImageView.setImageAsync(url: String?) {
    if (url == null) {
        setImageResource(R.drawable.placeholder)
    } else if (url != this.tag) {
        val uri = if (url.startsWith("http")) {
            url
        } else {
            "http://$url"
        }
        this.tag = url
        val imageLoader = App.component.getImageLoader()
        imageLoader.displayImage(uri, this)
    }
}