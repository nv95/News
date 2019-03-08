package com.vjettest.news.core.request

import java.text.SimpleDateFormat
import java.util.*

class EverythingRequestOptions : RequestOptions() {

    /**
     * A comma-seperated string of domains (eg bbc.co.uk, techcrunch.com, engadget.com) to restrict the search to.
     */
    var domains by prop({
        it.split(',')
    }, {
        it.joinToString(",")
    })
    /**
     * A comma-seperated string of domains (eg bbc.co.uk, techcrunch.com, engadget.com) to remove from the results.
     */
    var excludeDomains by prop({
        it.split(',')
    }, {
        it.joinToString(",")
    })

    /**
     * A date and optional time for the oldest article allowed.
     */
    var from by prop({
        dateFormat.parse(it)
    }, {
        dateFormat.format(it)
    })

    /**
     * A date and optional time for the newest article allowed.
     */
    var to by prop({
        dateFormat.parse(it)
    }, {
        dateFormat.format(it)
    })

    /**
     * The 2-letter ISO-639-1 code of the language you want to get headlines for.
     */
    var language by prop({ it }, { it })

    /**
     * The order to sort the articles in.
     */
    var sortBy by prop({ it }, { it })


    companion object {

        private val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.UK)
    }
}