package com.vjettest.news.core

import java.text.SimpleDateFormat
import java.util.*

class RequestOptions() : HashMap<String, String>() {

    /**
     * Keywords or phrases to search for.
     */
    var q: String by this

    /**
     * A comma-seperated string of identifiers (maximum 20) for the news sources or blogs you want headlines from.
     */
    var sources: List<String>
        get() = this["sources"]?.split(',') ?: emptyList()
        set(value) {
            this["sources"] = value.joinToString(",")
        }

    /**
     * A comma-seperated string of domains (eg bbc.co.uk, techcrunch.com, engadget.com) to restrict the search to.
     */
    var domains: List<String>
        get() = this["domains"]?.split(',') ?: emptyList()
        set(value) {
            this["domains"] = value.joinToString(",")
        }

    /**
     * A comma-seperated string of domains (eg bbc.co.uk, techcrunch.com, engadget.com) to remove from the results.
     */
    var excludeDomains: List<String>
        get() = this["excludeDomains"]?.split(',') ?: emptyList()
        set(value) {
            this["excludeDomains"] = value.joinToString(",")
        }

    /**
     * A date and optional time for the oldest article allowed.
     */
    var from: Date?
        get() = this["from"]?.let { dateFormat.parse(it) }
        set(value) {
            if (value == null) {
                this.remove("from")
            } else {
                this["from"] = dateFormat.format(value)
            }
        }

    /**
     * A date and optional time for the newest article allowed.
     */
    var to: Date?
        get() = this["to"]?.let { dateFormat.parse(it) }
        set(value) {
            if (value == null) {
                this.remove("to")
            } else {
                this["to"] = dateFormat.format(value)
            }
        }

    /**
     * The 2-letter ISO-639-1 code of the language you want to get headlines for.
     */
    var language by this

    /**
     * The order to sort the articles in.
     */
    var sortBy by this

    /**
     * The number of results to return per page. 20 is the default, 100 is the maximum.
     */
    var pageSize: Int
        get() = this["pageSize"]?.toIntOrNull() ?: 20
        set(value) {
            if (value in 1..100) {
                this["pageSize"] = value.toString()
            } else {
                throw IllegalArgumentException("Page size must be between 1 and 100")
            }

        }

    /**
     * Use this to page through the results.
     */
    var page: Int
        get() = this["page"]?.toIntOrNull() ?: 1
        set(value) {
            this["page"] = value.toString()
        }

    companion object {

        private val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.UK)
    }
}