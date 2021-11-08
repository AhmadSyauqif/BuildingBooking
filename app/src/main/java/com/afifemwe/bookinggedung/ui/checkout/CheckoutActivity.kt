package com.afifemwe.bookinggedung.ui.checkout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.adapters.CompoundButtonBindingAdapter.setChecked
import com.afifemwe.bookinggedung.R
import com.afifemwe.bookinggedung.api.Api
import com.afifemwe.bookinggedung.api.ApiInterfaces
import com.afifemwe.bookinggedung.databinding.ActivityCheckoutBinding
import com.afifemwe.bookinggedung.model.GeneralResponse
import com.afifemwe.bookinggedung.ui.cekagenda.CekAgendaActivity.Companion.HARGA_SEWA_KEY
import com.afifemwe.bookinggedung.ui.cekagenda.response.CheckBookingResponse
import com.afifemwe.bookinggedung.ui.detailbooking.DetailBookingActivity
import com.afifemwe.bookinggedung.ui.detailbooking.DetailBookingActivity.Companion.ID_BOOKING_KEY
import com.afifemwe.bookinggedung.utils.Converter
import com.afifemwe.bookinggedung.utils.CustomToast
import com.afifemwe.bookinggedung.utils.NetworkUtility
import com.afifemwe.bookinggedung.utils.UserPreference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class CheckoutActivity : AppCompatActivity() {

    private lateinit var bind: ActivityCheckoutBinding

    companion object {
        const val ID_GEDUNG_KEY = "id_gedung_key"
        const val TANGGAL_SEWA_KEY  = "tanggal_sewa_key"
        const val WAKTU_SEWA_KEY = "waktu_sewa_key"
        const val BIAYA_SEWA_KEY = "biaya_sewa_key"
        const val NAMA_GEDUNG_KEY = "nama_gedung_key"

        fun getCurrentDate(): String{
            val sdf = SimpleDateFormat("yyyy-MM-dd")
            return sdf.format(Date())
        }
    }

    private var idGedung = ""
    private var idUser = ""
    private var tanggalSewa = ""
    private var waktuSewa = ""
    private var hargaSewa = ""
    private var biayaSewa = ""
    private var namaGedung = ""

    var listWaktuSewa = listOf<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bind = DataBindingUtil.setContentView(this, R.layout.activity_checkout)

        initIntent()

        var userPref = UserPreference(this)
        idUser = userPref.getUsername().toString()

        bind.apply {
            tvNamaGedung.text = namaGedung
            tvBiayaSewa.text = """${Converter.formatRupiah(hargaSewa)} X ${(listWaktuSewa.size)} = ${Converter.formatRupiah(biayaSewa)}"""
            tvTanggalSewa.text = tanggalSewa

            listWaktuSewa.forEach {
                when (it) {
                    getString(R.string.time_1) -> showTime(bind.tvTime1)
                    getString(R.string.time_2) -> showTime(bind.tvTime2)
                    getString(R.string.time_3) -> showTime(bind.tvTime3)
                    getString(R.string.time_4) -> showTime(bind.tvTime4)
                    getString(R.string.time_5) -> showTime(bind.tvTime5)
                    getString(R.string.time_6) -> showTime(bind.tvTime6)
                    getString(R.string.time_7) -> showTime(bind.tvTime7)
                    getString(R.string.time_8) -> showTime(bind.tvTime8)
                    getString(R.string.time_9) -> showTime(bind.tvTime9)
                    getString(R.string.time_10) -> showTime(bind.tvTime10)
                    getString(R.string.time_11) -> showTime(bind.tvTime11)
                    getString(R.string.time_12) -> showTime(bind.tvTime12)
                    getString(R.string.time_13) -> showTime(bind.tvTime13)
                    getString(R.string.time_14) -> showTime(bind.tvTime14)
                }
            }

            btnCancel.setOnClickListener { onBackPressed() }

            btnBooking.setOnClickListener { booking() }


        }
    }

    private fun booking() {
        if (NetworkUtility.isInternetAvailable(this)) {
            try {
                val service = Api.getApi()!!.create(ApiInterfaces::class.java)
                val call = service.bookingGedung(
                    idGedung = idGedung,
                    username = idUser,
                    tanggalSewa = tanggalSewa,
                    tanggalBooking = getCurrentDate(),
                    biaya = biayaSewa,
                    status = "Menunggu Pembayaran",
                    waktuSewa = waktuSewa
                )
                call.enqueue(object : Callback<GeneralResponse> {
                    override fun onResponse(
                        call: Call<GeneralResponse>,
                        response: Response<GeneralResponse>
                    ) {
                        if (response.body()?.status == "success") {
                            val i = Intent(this@CheckoutActivity, DetailBookingActivity::class.java)
                            i.putExtra(ID_BOOKING_KEY, response.body()?.message)
                            startActivity(i)
                            finish()
                        } else {
                            CustomToast.showMessage(this@CheckoutActivity, response.body()?.message.toString())
                        }
                    }

                    override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                        NetworkUtility.checkYourConnection(this@CheckoutActivity)
                    }
                })
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            NetworkUtility.checkYourConnection(this)
        }
    }

    private fun showTime(tvTime: TextView){
        tvTime.visibility = View.VISIBLE
    }

    private fun initIntent() {
        idGedung = intent.getStringExtra(ID_GEDUNG_KEY).toString()
        tanggalSewa = intent.getStringExtra(TANGGAL_SEWA_KEY).toString()
        biayaSewa = intent.getDoubleExtra(BIAYA_SEWA_KEY, 0.0 ).toString()
        hargaSewa = intent.getStringExtra(HARGA_SEWA_KEY).toString()
        waktuSewa = intent.getStringExtra(WAKTU_SEWA_KEY).toString()
        namaGedung = intent.getStringExtra(NAMA_GEDUNG_KEY).toString()

        Log.i("LWT", waktuSewa)
        waktuSewa = waktuSewa.replace("[","")
        waktuSewa = waktuSewa.replace("]","")
        Log.i("LWTS", waktuSewa)
        listWaktuSewa = waktuSewa.split(", ")?.map { it -> it.trim() }
    }
}