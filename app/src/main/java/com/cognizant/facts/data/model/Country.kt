package com.cognizant.facts.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Country(

    @field:SerializedName("title")
    val title: String,

    @field:SerializedName("rows")
    val factList: List<Fact>

) : Parcelable
