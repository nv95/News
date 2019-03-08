package com.vjettest.news.common

import android.content.Context
import com.vjettest.news.App
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream

class ImagesStore(context: Context) {

    private val dir = context.getExternalFilesDir("images")

    fun put(url: String?): String? {
        if (url?.startsWith("file://") != false) {
            return url
        }
        val client = App.component.getHttpClient()
        val request = Request.Builder()
            .url(url)
            .build()
        val response = client.newCall(request).execute()
        val output = File(dir, url.hashCode().toString())
        response.body()?.use { body ->
            body.byteStream().copyTo(FileOutputStream(output))
        }
        return output.path
    }

    fun delete(url: String?) {
        if (url == null) {
            return
        }
        if (url.startsWith("file://")) {
            File(url.substring(7))
        } else {
            File(dir, url.hashCode().toString())
        }.takeIf { it.exists() }?.delete()
    }
}