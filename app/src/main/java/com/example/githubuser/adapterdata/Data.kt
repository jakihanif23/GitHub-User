package com.example.githubuser.adapterdata

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Data (
    val username: String?,
    val company: String?,
    val location: String?,
    val avatar: String?,
    val name: String?,
    val repository: String?,
    val followers: String?,
    val following: String?,
    val blog: String?
) : Parcelable