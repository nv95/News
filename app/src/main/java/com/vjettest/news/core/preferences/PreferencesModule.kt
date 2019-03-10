package com.vjettest.news.core.preferences

import android.content.Context
import androidx.annotation.NonNull
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PreferencesModule(private val context: Context) {

    @Provides
    @NonNull
    fun providePreferences() = AppPreferences(context)
}