package com.cognizant.news.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class Article(

    @field:SerializedName("title")
    val title:String,

    @field:SerializedName("content")
    val content:String,

    @field:SerializedName("urlToImage")
    val image:String
): Parcelable