package com.vjettest.news.core.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sources")
data class SourceInfo(
    @PrimaryKey(autoGenerate = false)
    var id: String,
    var name: String,
    var description: String?,
    var url: String,
    var category: String,
    var language: String,
    var country: String
)