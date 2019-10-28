package com.cognizant.facts.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class Fact(

    @field:SerializedName("title")
    val title:String?,

    @field:SerializedName("description")
    val description:String?,

    @field:SerializedName("imageHref")
    val image:String?
): Parcelable