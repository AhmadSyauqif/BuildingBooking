package com.afifemwe.bookinggedung.ui.cekagenda.response

import com.google.gson.annotations.SerializedName

data class CheckBookingResponse(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class Data(

	@field:SerializedName("jam_operasional")
	val jamOperasional: String? = null,

	@field:SerializedName("jam_sudah_booking")
	val jamSudahBooking: String? = null
)
