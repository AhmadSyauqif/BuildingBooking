package com.afifemwe.bookinggedung.ui.cekagenda

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.afifemwe.bookinggedung.R
import com.afifemwe.bookinggedung.R.color.gray
import com.afifemwe.bookinggedung.api.Api
import com.afifemwe.bookinggedung.api.ApiInterfaces
import com.afifemwe.bookinggedung.databinding.ActivityCekAgendaBinding
import com.afifemwe.bookinggedung.ui.cekagenda.response.CheckBookingResponse
import com.afifemwe.bookinggedung.ui.checkout.CheckoutActivity
import com.afifemwe.bookinggedung.ui.checkout.CheckoutActivity.Companion.BIAYA_SEWA_KEY
import com.afifemwe.bookinggedung.ui.checkout.CheckoutActivity.Companion.NAMA_GEDUNG_KEY
import com.afifemwe.bookinggedung.ui.checkout.CheckoutActivity.Companion.WAKTU_SEWA_KEY
import com.afifemwe.bookinggedung.ui.detailgedung.DetailGedungActivity.Companion.ID_GEDUNG_KEY
import com.afifemwe.bookinggedung.ui.detailgedung.response.GedungDetailResponse
import com.afifemwe.bookinggedung.utils.Const
import com.afifemwe.bookinggedung.utils.Converter
import com.afifemwe.bookinggedung.utils.NetworkUtility
import com.bumptech.glide.Glide
import com.google.android.material.checkbox.MaterialCheckBox
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class CekAgendaActivity : AppCompatActivity() {

    companion object {
        const val TANGGAL_SEWA_KEY = "tanggal_sewa_key"
        const val HARGA_SEWA_KEY = "harga_sewa_key"
    }

    private lateinit var bind: ActivityCekAgendaBinding
    private var idGedung = ""
    private var tanggalCekKetersediaan = ""
    private var hargaSewa = ""
    private var namaGedung = ""

    private var listChecked = ""

    var listCheckedBox = mutableListOf<String>()



    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bind = DataBindingUtil.setContentView(this, R.layout.activity_cek_agenda)

        
        idGedung = intent.getStringExtra(ID_GEDUNG_KEY).toString()
        hargaSewa = intent.getStringExtra(HARGA_SEWA_KEY).toString()
        namaGedung = intent.getStringExtra(NAMA_GEDUNG_KEY).toString()

        val localeIndonesia = Locale("id", "ID")
        
        if (intent.getStringExtra(TANGGAL_SEWA_KEY).isNullOrEmpty()) {
            var formatDate = "yyyy-MM-dd"
            var sdf = SimpleDateFormat(formatDate, localeIndonesia)
            var myCalendar = Calendar.getInstance(localeIndonesia)

            bind.tvDate.text = sdf.format(myCalendar.time)

            sdf = SimpleDateFormat(formatDate, localeIndonesia)

            tanggalCekKetersediaan = sdf.format(myCalendar.time)
        } else {
            var tanggalIntent = intent.getStringExtra(TANGGAL_SEWA_KEY).toString()

            var formatDate = "yyyy-MM-dd"
            var sdf = SimpleDateFormat(formatDate, localeIndonesia)

            bind.tvDate.text = tanggalIntent

            sdf = SimpleDateFormat(formatDate, localeIndonesia)

            tanggalCekKetersediaan = tanggalIntent
            Log.i("TGLCK", tanggalCekKetersediaan)

        }

        checkKetersediaan(idGedung, tanggalCekKetersediaan)

        initCheckBox()

        bind.btnChangeDate.setOnClickListener { getDatePicker() }

        bind.swipeContainer.setOnRefreshListener {
            checkKetersediaan(idGedung, tanggalCekKetersediaan)
            bind.swipeContainer.isRefreshing = false
        }

        bind.btnBooking.setOnClickListener {
            Log.i("List", listCheckedBox.toString())
            if (listCheckedBox.isNullOrEmpty()){
                Toast.makeText(this, "Silahkan Pilih Jam Booking Terlebih Dahulu", Toast.LENGTH_SHORT).show()
            } else {
                var biayaSewa = hargaSewa.toDouble() * (listCheckedBox.size).toDouble()
                // PINDAH KE CHECKOUT
                val i = Intent(this, CheckoutActivity::class.java)
                i.putExtra(ID_GEDUNG_KEY, idGedung)
                i.putExtra(TANGGAL_SEWA_KEY, tanggalCekKetersediaan)
                i.putExtra(WAKTU_SEWA_KEY, listCheckedBox.toString())
                i.putExtra(BIAYA_SEWA_KEY, biayaSewa)
                i.putExtra(HARGA_SEWA_KEY, hargaSewa)
                i.putExtra(NAMA_GEDUNG_KEY, namaGedung)
                startActivity(i)
            }
        }
    }

    private fun initCheckBox() {
        bind.apply {
            cbTime1.setOnClickListener { setCheckedTime(cbTime1) }
            cbTime2.setOnClickListener { setCheckedTime(cbTime2) }
            cbTime3.setOnClickListener { setCheckedTime(cbTime3) }
            cbTime4.setOnClickListener { setCheckedTime(cbTime4) }
            cbTime5.setOnClickListener { setCheckedTime(cbTime5) }
            cbTime6.setOnClickListener { setCheckedTime(cbTime6) }
            cbTime7.setOnClickListener { setCheckedTime(cbTime7) }
            cbTime8.setOnClickListener { setCheckedTime(cbTime8) }
            cbTime9.setOnClickListener { setCheckedTime(cbTime9) }
            cbTime10.setOnClickListener { setCheckedTime(cbTime10) }
            cbTime11.setOnClickListener { setCheckedTime(cbTime11) }
            cbTime12.setOnClickListener { setCheckedTime(cbTime12) }
            cbTime13.setOnClickListener { setCheckedTime(cbTime13) }
            cbTime14.setOnClickListener { setCheckedTime(cbTime14) }
        }
    }

    fun setCheckedTime(cb: MaterialCheckBox){
        if (cb.isChecked) {
            listCheckedBox.add(cb.text.toString())
        } else {
            listCheckedBox.remove(cb.text.toString())
        }

        Log.i("Selected", listCheckedBox.toString())
    }

    private fun checkKetersediaan(idGedung: String, tanggalCekKetersediaan: String) {
        if (NetworkUtility.isInternetAvailable(this)) {
            try {
                val service = Api.getApi()!!.create(ApiInterfaces::class.java)
                val call = service.checkBooking(idGedung, tanggalCekKetersediaan)
                call.enqueue(object : Callback<CheckBookingResponse> {
                    override fun onResponse(
                        call: Call<CheckBookingResponse>,
                        response: Response<CheckBookingResponse>
                    ) {
                        if (response.body()?.data != null) {

                            hideAllTime()
                            
                            showJamOperasional(response.body()!!.data!!.jamOperasional)
                            showJamTersedia(response.body()!!.data!!.jamSudahBooking)
                        }
                    }

                    override fun onFailure(call: Call<CheckBookingResponse>, t: Throwable) {
                        NetworkUtility.checkYourConnection(this@CekAgendaActivity)
                    }
                })
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            NetworkUtility.checkYourConnection(this)
        }
    }

    private fun showJamTersedia(jamSudahBooking: String?) {
        if (!jamSudahBooking.isNullOrEmpty()){

            val listJamTersedia = jamSudahBooking!!.split(", ")?.map { it -> it.trim() }

            Log.i("JamTs", jamSudahBooking.toString())

            for (jam in listJamTersedia) {
                when(jam) {
                    getString(R.string.time_1) -> setChecked(bind.cbTime1)
                    getString(R.string.time_2) -> setChecked(bind.cbTime2)
                    getString(R.string.time_3) -> setChecked(bind.cbTime3)
                    getString(R.string.time_4) -> setChecked(bind.cbTime4)
                    getString(R.string.time_5) -> setChecked(bind.cbTime5)
                    getString(R.string.time_6) -> setChecked(bind.cbTime6)
                    getString(R.string.time_7) -> setChecked(bind.cbTime7)
                    getString(R.string.time_8) -> setChecked(bind.cbTime8)
                    getString(R.string.time_9) -> setChecked(bind.cbTime9)
                    getString(R.string.time_10) -> setChecked(bind.cbTime10)
                    getString(R.string.time_11) -> setChecked(bind.cbTime11)
                    getString(R.string.time_12) -> setChecked(bind.cbTime12)
                    getString(R.string.time_13) -> setChecked(bind.cbTime13)
                    getString(R.string.time_14) -> setChecked(bind.cbTime14)
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    fun setChecked(checkBox: MaterialCheckBox){
        checkBox.apply {
            text = "$text (Sudah Di Booking)"
            isClickable = false
            setTextColor(resources.getColor(gray))
        }
    }

    fun hideAllTime(){
        bind.apply {
            resetCbTime(cbTime1, getString(R.string.time_1))
            resetCbTime(cbTime2, getString(R.string.time_2))
            resetCbTime(cbTime3, getString(R.string.time_3))
            resetCbTime(cbTime4, getString(R.string.time_4))
            resetCbTime(cbTime5, getString(R.string.time_5))
            resetCbTime(cbTime6, getString(R.string.time_6))
            resetCbTime(cbTime7, getString(R.string.time_7))
            resetCbTime(cbTime8, getString(R.string.time_8))
            resetCbTime(cbTime9, getString(R.string.time_9))
            resetCbTime(cbTime10, getString(R.string.time_10))
            resetCbTime(cbTime11, getString(R.string.time_11))
            resetCbTime(cbTime12, getString(R.string.time_12))
            resetCbTime(cbTime13, getString(R.string.time_13))
            resetCbTime(cbTime14, getString(R.string.time_14))
        }
    }

    fun resetCbTime(checkBox: MaterialCheckBox, text: String){
        checkBox.apply {
            visibility = View.GONE
            isChecked = false
            isClickable = true
            setText(text)
            setTextColor(resources.getColor(R.color.black))
        }
    }

    private fun showJamOperasional(jamOperasional: String?) {
        if (!jamOperasional.isNullOrEmpty()) {

            var listJamOperasional = jamOperasional!!.split(", ")?.map { it -> it.trim() }

            Log.i("JamOp", jamOperasional.toString())


            for (jam in listJamOperasional){
                Log.i("Jam", jam)

                when(jam) {
                    getString(R.string.time_1) -> bind.cbTime1.visibility = View.VISIBLE
                    getString(R.string.time_2) -> bind.cbTime2.visibility = View.VISIBLE
                    getString(R.string.time_3) -> bind.cbTime3.visibility = View.VISIBLE
                    getString(R.string.time_4) -> bind.cbTime4.visibility = View.VISIBLE
                    getString(R.string.time_5) -> bind.cbTime5.visibility = View.VISIBLE
                    getString(R.string.time_6) -> bind.cbTime6.visibility = View.VISIBLE
                    getString(R.string.time_7) -> bind.cbTime7.visibility = View.VISIBLE
                    getString(R.string.time_8) -> bind.cbTime8.visibility = View.VISIBLE
                    getString(R.string.time_9) -> bind.cbTime9.visibility = View.VISIBLE
                    getString(R.string.time_10) -> bind.cbTime10.visibility = View.VISIBLE
                    getString(R.string.time_11) -> bind.cbTime11.visibility = View.VISIBLE
                    getString(R.string.time_12) -> bind.cbTime12.visibility = View.VISIBLE
                    getString(R.string.time_13) -> bind.cbTime13.visibility = View.VISIBLE
                    getString(R.string.time_14) -> bind.cbTime14.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun getDatePicker() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val c = Calendar.getInstance()
            c.timeZone = TimeZone.getDefault()

            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val localeIndonesia = Locale("id", "ID")

            var formatDate = "yyyy-MM-dd"
            var sdf = SimpleDateFormat(formatDate, localeIndonesia)
            var myCalendar = Calendar.getInstance(localeIndonesia)

            DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    myCalendar.set(Calendar.YEAR, year)
                    myCalendar.set(Calendar.MONTH, month)
                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)


                    bind.tvDate.text = sdf.format(myCalendar.time)

                    sdf = SimpleDateFormat(formatDate, localeIndonesia)

                    tanggalCekKetersediaan = sdf.format(myCalendar.time)

                    checkKetersediaan(idGedung, tanggalCekKetersediaan)
                },
                year, month, day
            ).show()
        }
    }
}