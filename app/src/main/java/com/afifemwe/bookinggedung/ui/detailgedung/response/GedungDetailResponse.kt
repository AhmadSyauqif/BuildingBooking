package com.afifemwe.bookinggedung.ui.detailgedung.response

import com.google.gson.annotations.SerializedName

data class GedungDetailResponse(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("gedungDetail")
	val gedungDetail: GedungDetail? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class GedungDetail(

	@field:SerializedName("flag")
	val flag: String? = null,

	@field:SerializedName("maps")
	val maps: String? = null,

	@field:SerializedName("rating")
	val rating: String? = null,

	@field:SerializedName("created_at")
	val createdAt: Any? = null,

	@field:SerializedName("fasilitas")
	val fasilitas: String? = null,

	@field:SerializedName("gambar")
	val gambar: String? = null,

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("harga")
	val harga: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: Any? = null,

	@field:SerializedName("jam_operasional")
	val jamOperasional: String? = null,

	@field:SerializedName("pemilik")
	val pemilik: Any? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("kapasitas")
	val kapasitas: String? = null
)
