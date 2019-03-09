package com.vjettest.news.core.network.options

import java.util.*
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

open class RequestOptions : HashMap<String, String>() {

    /**
     * Keywords or phrases to search for.
     */
    var q by prop({ it }, { it })

    /**
     * A comma-seperated string of identifiers (maximum 20) for the news sources or blogs you want headlines from.
     */
    var sources by prop({
        it.split(',')
    }, {
        it.joinToString(",")
    })

    /**
     * The number of results to return per page. 20 is the default, 100 is the maximum.
     */
    var pageSize by prop({
        it.toIntOrNull()
    }, {
        if (it in 1..100) {
            it.toString()
        } else {
            throw IllegalArgumentException("Page size must be between 1 and 100")
        }
    })

    /**
     * Use this to page through the results.
     */
    var page by prop({
        it.toIntOrNull()
    }, {
        it.toString()
    })

    protected inner class PropertyDelegate<T>(
        private val getterTransform: (String) -> T?,
        private val setterTransform: (T) -> String
    ) : ReadWriteProperty<RequestOptions, T?> {

        override fun getValue(thisRef: RequestOptions, property: KProperty<*>): T? {
            return thisRef[property.name]?.let { getterTransform(it) }
        }

        override fun setValue(thisRef: RequestOptions, property: KProperty<*>, value: T?) {
            if (value == null) {
                thisRef.remove(property.name)
            } else {
                thisRef[property.name] = setterTransform(value)
            }
        }
    }

    protected fun <T> prop(getterTransform: (String) -> T?, setterTransform: (T) -> String) =
        PropertyDelegate<T>(getterTransform, setterTransform)
}