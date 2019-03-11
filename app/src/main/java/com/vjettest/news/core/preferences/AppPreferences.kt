package com.vjettest.news.core.preferences

import android.content.Context
import android.preference.PreferenceManager
import com.vjettest.news.core.network.options.EverythingRequestOptions
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class AppPreferences(context: Context) {

    private val prefs = PreferenceManager.getDefaultSharedPreferences(context.applicationContext)

    var defaultSortOrder by pref(EverythingRequestOptions.SORT_BY_PUBLISHED_AT)

    /**
     * Delegates
     */

    private inner class PreferenceDelegate(private val defValue: String) : ReadWriteProperty<AppPreferences, String> {

        override fun getValue(thisRef: AppPreferences, property: KProperty<*>) =
            thisRef.prefs.getString(property.name, defValue) ?: defValue

        override fun setValue(thisRef: AppPreferences, property: KProperty<*>, value: String) {
            thisRef.prefs.edit().putString(property.name, value).apply()
        }


    }

    private fun pref(defValue: String) = PreferenceDelegate(defValue)
}