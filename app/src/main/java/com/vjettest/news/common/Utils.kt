package com.vjettest.news.common

import android.content.Context
import android.text.format.DateUtils
import java.util.*

fun Date.formatRelative(context: Context) = DateUtils.getRelativeTimeSpanString(context, this.time)