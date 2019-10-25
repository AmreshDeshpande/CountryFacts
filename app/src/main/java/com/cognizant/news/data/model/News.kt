package com.cognizant.news.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class News(
    val title:String,
    val news:String
): Parcelable
