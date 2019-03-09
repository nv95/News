package com.vjettest.news.common

import android.content.Context
import android.text.format.DateUtils
import android.widget.ImageView
import com.vjettest.news.App
import com.vjettest.news.R
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.util.*

fun Date.formatRelative(): CharSequence = DateUtils.getRelativeTimeSpanString(
    this.time,
    System.currentTimeMillis(),
    0L,
    DateUtils.FORMAT_ABBREV_ALL
)

fun Date.formatDateRelative(context: Context): String = if (DateUtils.isToday(this.time)) {
    context.getString(R.string.today)
} else {
    DateUtils.formatDateTime(context, this.time, DateUtils.FORMAT_ABBREV_RELATIVE)
}

val Date.withoutTime: Date
    get() {
        val calendar = Calendar.getInstance()

        calendar.time = this
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        return calendar.time
    }

fun ImageView.setImageAsync(url: String?) {
    if (url == null) {
        setImageResource(R.drawable.placeholder)
    } else if (url != this.tag) {
        val uri = when {
            url.startsWith("//") -> "http:$url"
            url.contains("://") -> url.replace("https://", "http://")
            else -> "http://$url"
        }
        this.tag = url
        val imageLoader = App.component.getImageLoader()
        imageLoader.displayImage(uri, this)
    }
}

operator fun CompositeDisposable.plusAssign(d: Disposable) {
    this.add(d)
}