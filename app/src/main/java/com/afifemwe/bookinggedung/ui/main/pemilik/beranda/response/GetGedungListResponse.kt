package com.afifemwe.bookinggedung.ui.main.pemilik.beranda.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GetGedungListResponse(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("listGedung")
	val listGedung: List<ListGedungItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
) : Parcelable

@Parcelize
data class ListGedungItem(

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("harga")
	val harga: String? = null,

	@field:SerializedName("rating")
	val rating: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("kapasitas")
	val kapasitas: String? = null,

	@field:SerializedName("gambar")
	val gambar: String? = null
) : Parcelable
