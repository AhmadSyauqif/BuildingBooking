package com.afifemwe.bookinggedung.ui.detailbooking.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DetailBookingResponse(
	@field:SerializedName("code")
	val code: Int? = null,
	@field:SerializedName("bookingDetail")
	val bookingDetail: BookingDetail? = null,
	@field:SerializedName("message")
	val message: String? = null,
	@field:SerializedName("status")
	val status: String? = null
) : Parcelable

@Parcelize
data class BookingDetail(
	@field:SerializedName("tanggal_sewa")
	val tanggalSewa: String? = null,

	@field:SerializedName("nama_pemilik")
	val namaPemilik: String? = null,

	@field:SerializedName("nama_customer")
	val namaCustomer: String? = null,

	@field:SerializedName("no_rekening")
	val noRekening: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("nama_bank")
	val namaBank: String? = null,

	@field:SerializedName("jam_sewa")
	val jamSewa: String? = null,

	@field:SerializedName("nama_gedung")
	val namaGedung: String? = null,

	@field:SerializedName("status")
	val status: String? = null
) : Parcelable
