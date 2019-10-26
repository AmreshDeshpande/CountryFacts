package com.cognizant.news.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class News(
    @field:SerializedName("articles")
    val newsList : List<Article>

): Parcelable
