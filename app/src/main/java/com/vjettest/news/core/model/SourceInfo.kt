package com.vjettest.news.core.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sources")
data class SourceInfo(
    @PrimaryKey(autoGenerate = false)
    var id: String,
    var name: String,
    var description: String?,
    var url: String,
    var category: String,
    var language: String,
    var country: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()?:"",
        parcel.readString()!!,
        parcel.readString(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeString(url)
        parcel.writeString(category)
        parcel.writeString(language)
        parcel.writeString(country)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SourceInfo> {
        override fun createFromParcel(parcel: Parcel): SourceInfo {
            return SourceInfo(parcel)
        }

        override fun newArray(size: Int): Array<SourceInfo?> {
            return arrayOfNulls(size)
        }
    }

}