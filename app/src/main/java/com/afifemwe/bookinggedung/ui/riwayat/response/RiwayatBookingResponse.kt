package com.afifemwe.bookinggedung.ui.riwayat.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RiwayatBookingResponse(

	@field:SerializedName("listRiwayatBooking")
	val listRiwayatBooking: List<ListRiwayatBookingItem?>? = null,

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
) : Parcelable

@Parcelize
data class ListRiwayatBookingItem(

	@field:SerializedName("tanggal_sewa")
	val tanggalSewa: String? = null,

	@field:SerializedName("nama_customer")
	val namaCustomer: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("nama_gedung")
	val namaGedung: String? = null,

	@field:SerializedName("status")
	val status: String? = null
) : Parcelable
