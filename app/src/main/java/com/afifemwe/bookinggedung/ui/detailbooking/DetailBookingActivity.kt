package com.afifemwe.bookinggedung.ui.detailbooking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.afifemwe.bookinggedung.R
import com.afifemwe.bookinggedung.api.Api
import com.afifemwe.bookinggedung.api.ApiInterfaces
import com.afifemwe.bookinggedung.databinding.ActivityDetailBookingBinding
import com.afifemwe.bookinggedung.databinding.ActivityDetailGedungBinding
import com.afifemwe.bookinggedung.model.GeneralResponse
import com.afifemwe.bookinggedung.ui.detailbooking.response.DetailBookingResponse
import com.afifemwe.bookinggedung.ui.main.customer.CustomerMainActivity
import com.afifemwe.bookinggedung.ui.main.pemilik.PemilikMainActivity
import com.afifemwe.bookinggedung.utils.Const
import com.afifemwe.bookinggedung.utils.Const.Companion.CUSTOMER
import com.afifemwe.bookinggedung.utils.Const.Companion.PEMILIK
import com.afifemwe.bookinggedung.utils.Const.Companion.SUCCESS_RESPONSE
import com.afifemwe.bookinggedung.utils.NetworkUtility
import com.afifemwe.bookinggedung.utils.UserPreference
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class DetailBookingActivity : AppCompatActivity() {
    companion object {
        const val ID_BOOKING_KEY = "id_booking_key"
    }

    private lateinit var bind: ActivityDetailBookingBinding

    private var idBooking = ""
//    private var idBooking = "3"

    private var username: String = ""
    private var tipePengguna: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bind = DataBindingUtil.setContentView(this, R.layout.activity_detail_booking)

        initIntent()
        initUserPreference()

        getDetailBookingData()

        bind.swipeContainer.setOnRefreshListener {
            getDetailBookingData()
            bind.swipeContainer.isRefreshing = false
        }

        bind.btnCancelOrder.setOnClickListener { cancelBooking() }
        bind.btnCancel.setOnClickListener { cancelBooking() }

        bind.btnKonfirmasiSudahBayar.setOnClickListener { konfirmasiPembayaran() }


    }

    private fun konfirmasiPembayaran() {
        if(NetworkUtility.isInternetAvailable(this)) {
            try {
                val service = Api.getApi()!!.create(ApiInterfaces::class.java)
                val call = service.konfirmasiPembayaran(
                    idBooking = idBooking
                )

                call.enqueue(object: retrofit2.Callback<GeneralResponse> {
                    override fun onResponse(
                        call: Call<GeneralResponse>,
                        response: Response<GeneralResponse>
                    ) {
                        if (response.body()?.status == SUCCESS_RESPONSE) {
                            Toast.makeText(this@DetailBookingActivity, "Berhasil Konfirmasi Pembayaran", Toast.LENGTH_SHORT).show()
                            onBackPressed()
                            finish()
                        } else {
                            Toast.makeText(this@DetailBookingActivity, response.body()?.message.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                        NetworkUtility.checkYourConnection(this@DetailBookingActivity)
                    }
                })
            } catch (e: Exception) {
                e.printStackTrace()
                NetworkUtility.checkYourConnection(this)
            }
        } else {
            NetworkUtility.checkYourConnection(this)
        }
    }

    private fun cancelBooking() {
        if(NetworkUtility.isInternetAvailable(this)) {
            try {
                val service = Api.getApi()!!.create(ApiInterfaces::class.java)
                val call = service.cancelBooking(
                    idBooking = idBooking
                )

                call.enqueue(object: retrofit2.Callback<GeneralResponse> {
                    override fun onResponse(
                        call: Call<GeneralResponse>,
                        response: Response<GeneralResponse>
                    ) {
                        if (response.body()?.status == SUCCESS_RESPONSE) {
                                Toast.makeText(this@DetailBookingActivity, "Berhasil Cancel Booking", Toast.LENGTH_SHORT).show()
                                onBackPressed()
                                finish()
                        } else {
                            Toast.makeText(this@DetailBookingActivity, response.body()?.message.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                        NetworkUtility.checkYourConnection(this@DetailBookingActivity)
                    }
                })
            } catch (e: Exception) {
                e.printStackTrace()
                NetworkUtility.checkYourConnection(this)
            }
        } else {
            NetworkUtility.checkYourConnection(this)
        }
    }

    private fun initUserPreference() {
        val userPref = UserPreference(this)
        username = userPref.getUsername().toString()
        tipePengguna = userPref.getTipePengguna().toString()
    }

    private fun getDetailBookingData() {
        if(NetworkUtility.isInternetAvailable(this)) {
            try {
                val service = Api.getApi()!!.create(ApiInterfaces::class.java)
                val call = service.getDetailBookingGedung(
                    idBooking = idBooking
                )

                call.enqueue(object: retrofit2.Callback<DetailBookingResponse> {
                    override fun onResponse(
                        call: Call<DetailBookingResponse>,
                        response: Response<DetailBookingResponse>
                    ) {
                        if (response.body()?.status == Const.SUCCESS_RESPONSE) {
                            val data = response.body()?.bookingDetail

                            if (data != null) {

                                Log.i("Res", data.toString())
                                bind.apply {
                                    tvNamaGedung.text = data.namaGedung

                                    tvTanggalSewa.text = data.tanggalSewa
                                    tvCustomer.text = data.namaCustomer
                                    tvStatus.text = data.status
                                    tvIdBooking.text = """#${data.id}"""

                                    if (data.status == "Menunggu Pembayaran") {
                                        tvDetailRekening.text = """${data.noRekening} (${data.namaBank}) a.n ${data.namaPemilik}"""
                                        layoutPanduanTransfer.visibility = View.VISIBLE
                                    } else {
                                        layoutPanduanTransfer.visibility = View.GONE
                                    }

                                    if (data.status == Const.STATUS_BATAL) {
                                        btnKonfirmasiSudahBayar.visibility      = View.GONE
                                        btnCancelOrder.visibility               = View.GONE
                                    } else {
                                        btnKonfirmasiSudahBayar.visibility      = View.VISIBLE
                                        btnCancelOrder.visibility               = View.VISIBLE
                                    }

                                    val listJamSewa = data.jamSewa!!.split(", ")?.map { it -> it.trim() }

                                    when(tipePengguna) {
                                        PEMILIK -> {
                                            layoutButtonCustomer.visibility = View.GONE
                                            layoutButtonPemilik.visibility = View.VISIBLE
                                            layoutPanduanTransfer.visibility = View.GONE

                                        }

                                        CUSTOMER -> {
                                            layoutButtonPemilik.visibility = View.GONE
                                            layoutButtonCustomer.visibility = View.VISIBLE

                                        }
                                    }

                                    listJamSewa.forEach {
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
                                }
                            }

                        } else {
                            Toast.makeText(this@DetailBookingActivity, response.body()?.message.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<DetailBookingResponse>, t: Throwable) {
                        NetworkUtility.checkYourConnection(this@DetailBookingActivity)
                    }
                })
            } catch (e: Exception) {
                e.printStackTrace()
                NetworkUtility.checkYourConnection(this)
            }
        } else {
            NetworkUtility.checkYourConnection(this)
        }
    }

    private fun initIntent() {
        if (intent.getStringExtra(ID_BOOKING_KEY) != "" || intent.getStringExtra(ID_BOOKING_KEY) == null) {
            idBooking = intent.getStringExtra(ID_BOOKING_KEY).toString()
        }
    }

    private fun showTime(tvTime: TextView){
        tvTime.visibility = View.VISIBLE
    }
}