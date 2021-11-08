package com.afifemwe.bookinggedung.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Time(
    @field:SerializedName("id")
    var id: Int? = null,

    @field:SerializedName("waktuMulai")
    var waktuMulai: String? = null,

    @field:SerializedName("waktuSelesai")
    var waktuSelesai: String? = null

) : Parcelable
