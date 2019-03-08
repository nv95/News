package com.vjettest.news.core.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.vjettest.news.core.model.Article
import io.reactivex.Maybe
import io.reactivex.Observable

@Dao
interface ArticlesDao {

    @Query("SELECT * FROM articles")
    fun getAll(): Observable<List<Article>>

    @Query("SELECT * FROM articles WHERE url = :url")
    fun findByUrl(url: String): Maybe<Article>

    @Insert
    fun insert(article: Article): Long

    @Delete
    fun delete(article: Article)
}