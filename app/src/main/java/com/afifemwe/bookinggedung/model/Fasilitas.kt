package com.afifemwe.bookinggedung.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Fasilitas(
    @field:SerializedName("id")
    var id: Int? = null,

    @field:SerializedName("icon")
    var icon: Int? = null,

    @field:SerializedName("nama")
    var nama: String? = null
) : Parcelable