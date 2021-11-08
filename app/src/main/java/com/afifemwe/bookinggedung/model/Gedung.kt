package com.afifemwe.bookinggedung.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Gedung(

    @field:SerializedName("id")
    var id: Int? = null,

    @field:SerializedName("gambar")
    var gambar: String? = null,

    @field:SerializedName("nama")
    var nama: String? = null,

    @field:SerializedName("kapasitas")
    var kapasitas: Int? = null,

    @field:SerializedName("rating")
    var rating: Double? = null,

    @field:SerializedName("daftarFasilitas")
    var daftarFasilitas: List<Fasilitas>? = null,

    @field:SerializedName("jamOperasional")
    var jamOperasional: List<Time>? = null,

    @field:SerializedName("maps")
    var maps: String? = null,

    @field:SerializedName("harga")
    var harga: Int? = null,

    @field:SerializedName("pemilik")
    var pemilik: String? = null

    ) : Parcelable
