package com.afifemwe.bookinggedung.ui.akun.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DetailAkunResponse(

	@field:SerializedName("akunDetail")
	val akunDetail: AkunDetail? = null,

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
) : Parcelable

@Parcelize
data class AkunDetail(

	@field:SerializedName("nama_pemilik")
	val namaPemilik: String? = null,

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("tipe_user")
	val tipeUser: String? = null,

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("no_hp")
	val noHp: String? = null,

	@field:SerializedName("no_rekening")
	val noRekening: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("nama_bank")
	val namaBank: String? = null,

	@field:SerializedName("jenis_kelamin")
	val jenisKelamin: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("username")
	val username: String? = null,

	@field:SerializedName("alamat")
	val alamat: String? = null
) : Parcelable
