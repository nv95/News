package com.vjettest.news.core.database

import android.content.Context
import androidx.annotation.NonNull
import androidx.room.Room
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule(private val context: Context) {

    @Provides
    @Singleton
    @NonNull
    fun provideDatabase() = Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java, "local_store"
    ).build()

}