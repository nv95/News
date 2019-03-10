package com.vjettest.news.core.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.vjettest.news.core.model.SourceInfo
import io.reactivex.Observable

@Dao
abstract class SourcesDao {

    @Query("SELECT * FROM sources")
    abstract fun getAll(): Observable<List<SourceInfo>>

    @Insert
    abstract fun insert(sources: List<SourceInfo>): List<Long>

    @Query("DELETE FROM sources")
    abstract fun deleteAll()

    @Transaction
    open fun replaceWith(sources: List<SourceInfo>): List<Long> {
        deleteAll()
        return insert(sources)
    }
}