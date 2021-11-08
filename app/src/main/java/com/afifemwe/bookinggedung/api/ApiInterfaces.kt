package com.afifemwe.bookinggedung.api

import com.afifemwe.bookinggedung.model.GeneralResponse
import com.afifemwe.bookinggedung.ui.cekagenda.response.CheckBookingResponse
import com.afifemwe.bookinggedung.ui.detailbooking.response.DetailBookingResponse
import com.afifemwe.bookinggedung.ui.detailgedung.response.GedungDetailResponse
import com.afifemwe.bookinggedung.ui.akun.response.DetailAkunResponse
import com.afifemwe.bookinggedung.ui.riwayat.response.RiwayatBookingResponse
import com.afifemwe.bookinggedung.ui.main.pemilik.beranda.response.GetGedungListResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiInterfaces {

    /* Registrasi Akun Baru */
    @FormUrlEncoded
    @POST("registrasi_akun")
    fun registerAkun(
        @Field("nama") nama: String,
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("no_hp") no_hp: String,
        @Field("jenis_kelamin") jenis_kelamin: String,
        @Field("alamat") alamat: String,
        @Field("tipe_user") tipe_user: String,
        @Field("no_rek") no_rek: String?,
        @Field("nama_bank") nama_bank: String?,
        @Field("nama_pemilik") nama_pemilik: String?
    ): Call<GeneralResponse>

    /* Check Login */
    @FormUrlEncoded
    @POST("check_login")
    fun checkLogin(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("tipe_user") tipe_user: String,
    ): Call<GeneralResponse>

    /* Upload Photo Gedung */
    @FormUrlEncoded
    @POST("upload_photo")
    fun uploadPhoto(
        @Field("imgPath") imgPath: String?,
        @Field("imgName") imgName: String?
    ): Call<GeneralResponse>


    /* Registrasi Gedung Baru */
    @FormUrlEncoded
    @POST("buat_gedung_baru")
    fun buatGedungBaru(
        @Field("nama") nama: String,
        @Field("gambar") gambar: String,
        @Field("kapasitas") kapasitas: String,
        @Field("fasilitas") fasilitas: String,
        @Field("jam_operasional") jam_operasional: String,
        @Field("maps") maps: String,
        @Field("harga") harga: String,
        @Field("pemilik") pemilik: String
    ): Call<GeneralResponse>

    /* Get List Gedung Pemilik */
    @FormUrlEncoded
    @POST("get_gedung_list_pemilik")
    fun getGedungListPemilik(
        @Field("pemilik") pemilik: String
    ): Call<GetGedungListResponse>

    /* Get List Gedung Pemilik Filter*/
    @FormUrlEncoded
    @POST("get_gedung_list_pemilik_filter")
    fun getGedungListPemilikFilter(
        @Field("pemilik") pemilik: String,
        @Field("filterBy") filterBy: String
    ): Call<GetGedungListResponse>


    /* Get List Gedung Customer */
    @GET("get_gedung_list_customer")
    fun getGedungListCustomer(): Call<GetGedungListResponse>

    /* Get List Gedung Pemilik Filter*/
    @FormUrlEncoded
    @POST("get_gedung_list_customer_filter")
    fun getGedungListCustomerFilter(
        @Field("filterBy") filterBy: String
    ): Call<GetGedungListResponse>


    /* Get List Gedung Detail */
    @FormUrlEncoded
    @POST("get_gedung_detail")
    fun getGedungDetail(
        @Field("idGedung") idGedung: String
    ): Call<GedungDetailResponse>


    /* Cek Ketersediaan Gedung */
    @FormUrlEncoded
    @POST("check_booking_gedung")
    fun checkBooking(
        @Field("id_gedung") idGedung: String,
        @Field("tgl_sewa") tanggalCekKetersediaan: String
    ): Call<CheckBookingResponse>

    /* BookingGedung */
    @FormUrlEncoded
    @POST("booking_gedung")
    fun bookingGedung(
        @Field("id_gedung") idGedung: String,
        @Field("username") username: String,
        @Field("tanggal_booking") tanggalBooking: String,
        @Field("tanggal_sewa") tanggalSewa: String,
        @Field("waktu_sewa") waktuSewa: String,
        @Field("biaya") biaya: String,
        @Field("status") status: String
    ): Call<GeneralResponse>

    /* BookingGedung */
    @FormUrlEncoded
    @POST("get_detail_booking")
    fun getDetailBookingGedung(
        @Field("idBooking") idBooking: String
    ): Call<DetailBookingResponse>

    /* Detail Akun */
    @FormUrlEncoded
    @POST("get_detail_akun")
    fun getDetailAkun(
        @Field("username") username: String
    ): Call<DetailAkunResponse>

    /* Get List Riwayat Booking Customer */
    @FormUrlEncoded
    @POST("get_riwayat_booking_customer")
    fun getListRiwayatBookingCustomer(
        @Field("username") username: String
    ): Call<RiwayatBookingResponse>

    /* Get List Riwayat Booking Pemilik */
    @FormUrlEncoded
    @POST("get_riwayat_booking_pemilik")
    fun getListRiwayatBookingPemilik(
        @Field("username") username: String
    ): Call<RiwayatBookingResponse>

    /* Cancel Booking */
    @FormUrlEncoded
    @POST("cancel_booking")
    fun cancelBooking(
        @Field("id_booking") idBooking: String
    ): Call<GeneralResponse>

    /* Konfirmasi Pembayaran */
    @FormUrlEncoded
    @POST("konfirmasi_pembayaran")
    fun konfirmasiPembayaran(
        @Field("id_booking") idBooking: String
    ): Call<GeneralResponse>

    /* Hapus Gedung */
    @FormUrlEncoded
    @POST("hapus_gedung")
    fun hapusGedung(
        @Field("id_gedung") idGedung: String
    ): Call<GeneralResponse>


    /* Update Akun */
    @FormUrlEncoded
    @POST("update_akun")
    fun updateAkun(
        @Field("nama") nama: String,
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("no_hp") no_hp: String,
        @Field("jenis_kelamin") jenis_kelamin: String,
        @Field("alamat") alamat: String,
        @Field("tipe_user") tipe_user: String,
        @Field("no_rek") no_rek: String?,
        @Field("nama_bank") nama_bank: String?,
        @Field("nama_pemilik") nama_pemilik: String?
    ): Call<GeneralResponse>


}