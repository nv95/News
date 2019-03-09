package com.vjettest.news.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.vjettest.news.core.model.Article
import com.vjettest.news.core.model.SourceInfo

@Database(entities = [Article::class, SourceInfo::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun articlesDao(): ArticlesDao
    abstract fun sourcesDao(): SourcesDao
}
