package com.vjettest.news.core.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*


@Entity(tableName = "articles")
data class Article(
    var source: Source,
    @ColumnInfo(name = "author")
    var author: String?,
    @ColumnInfo(name = "title")
    var title: String,
    @ColumnInfo(name = "description")
    var description: String?,
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "url")
    var url: String,
    @ColumnInfo(name = "image_url")
    var urlToImage: String?,
    @ColumnInfo(name = "published_at")
    var publishedAt: Date,
    @ColumnInfo(name = "content")
    var content: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(Source::class.java.classLoader)!!,
        parcel.readString(),
        parcel.readString()!!,
        parcel.readString(),
        parcel.readString()!!,
        parcel.readString(),
        Date(parcel.readLong()),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(source, 0)
        parcel.writeString(author)
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeString(url)
        parcel.writeString(urlToImage)
        parcel.writeLong(publishedAt.time)
        parcel.writeString(content)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Article> {
        override fun createFromParcel(parcel: Parcel): Article {
            return Article(parcel)
        }

        override fun newArray(size: Int): Array<Article?> {
            return arrayOfNulls(size)
        }
    }
}