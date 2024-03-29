package com.cognizant.facts.data.model

import android.os.Parcelable
import com.cognizant.facts.ui.FactsAdapter.Companion.FactItem
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Fact(

    @field:SerializedName("title")
    val title: String?,

    @field:SerializedName("description")
    val description: String?,

    @field:SerializedName("imageHref")
    val image: String?

) : Parcelable {
    @IgnoredOnParcel
    var itemType: Int = FactItem
}