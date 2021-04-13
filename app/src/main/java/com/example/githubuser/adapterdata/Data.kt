package com.example.githubuser.adapterdata

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parceler
import kotlinx.android.parcel.Parcelize

@Parcelize
class Data (
    val username: String?,
    val company: String?,
    val location: String?,
    val avatar: Int?,
    val name: String?,
    val repository: String?,
    val followers: String?,
    val following: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    companion object : Parceler<Data> {

        override fun Data.write(parcel: Parcel, flags: Int) {
            parcel.writeString(username)
            parcel.writeString(company)
            parcel.writeString(location)
            parcel.writeValue(avatar)
            parcel.writeString(name)
            parcel.writeString(repository)
            parcel.writeString(followers)
            parcel.writeString(following)
        }

        override fun create(parcel: Parcel): Data {
            return Data(parcel)
        }
    }
}