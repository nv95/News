package com.vjettest.news.core.database

import androidx.room.TypeConverter
import com.vjettest.news.core.model.Source
import java.util.*


class Converters {

    /**
     * Date
     */

    @TypeConverter
    fun fromTimestamp(value: Long?) = value?.let { Date(it) }

    @TypeConverter
    fun dateToTimestamp(date: Date?) = date?.time

    /**
     * Source
     */

    @TypeConverter
    fun fromSourceId(value: String?): Source? {
        val parts = value?.split("\n") ?: listOf(null, "")
        return Source(parts.first(), parts.last() ?: "")
    }

    @TypeConverter
    fun sourceToId(value: Source?) = value?.let { "${it.id}\n${it.name}" }
}