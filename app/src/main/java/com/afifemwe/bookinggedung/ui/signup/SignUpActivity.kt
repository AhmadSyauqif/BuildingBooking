    package com.afifemwe.bookinggedung.ui.signup

import android.content.Intent
import android.net.Network
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.*
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.afifemwe.bookinggedung.R
import com.afifemwe.bookinggedung.api.Api
import com.afifemwe.bookinggedung.api.ApiInterfaces
import com.afifemwe.bookinggedung.databinding.ActivitySignUpBinding
import com.afifemwe.bookinggedung.model.GeneralResponse
import com.afifemwe.bookinggedung.ui.main.customer.CustomerMainActivity
import com.afifemwe.bookinggedung.ui.main.pemilik.PemilikMainActivity
import com.afifemwe.bookinggedung.utils.Const
import com.afifemwe.bookinggedung.utils.NetworkUtility
import com.afifemwe.bookinggedung.utils.ValidateInput
import okhttp3.Callback
import retrofit2.Call
import retrofit2.Response

class SignUpActivity : AppCompatActivity() {

    private lateinit var bind: ActivitySignUpBinding
    private lateinit var tipePenggunaList: MutableList<String>
    private lateinit var tipeJenisKelaminList: MutableList<String>

    private lateinit var username: String
    private lateinit var password: String
    private lateinit var email: String

    private var jenisKelamin: String = ""
    private var tipePengguna: String = ""

    private val tipePenggunaOpsi: ArrayList<String> = ArrayList()
    private val tipeJenisKelaminOpsi: ArrayList<String> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bind = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)

        initSpinnerPengguna()
        initSpinnerJenisKelamin()

        bind.btnSignUp.setOnClickListener { registAccount() }



        bind.spinnerJenisKelamin.onItemSelectedListener =
            object : OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    jenisKelamin = tipeJenisKelaminOpsi[position]
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }
    }

    private fun registAccount() {
        bind.apply {

            spinnerTipePengguna.onItemSelectedListener

            ValidateInput.apply {
                validate(etNama)
                validate(etUsername)
                validate(etEmail)
                validatePassword(etPassword)
            }

            if (ValidateInput.validate(etNama) && ValidateInput.validate(etUsername) && ValidateInput.validate(etEmail) && ValidateInput.validatePassword(etPassword) && jenisKelamin != "" && tipePengguna != "") {
                if(NetworkUtility.isInternetAvailable(this@SignUpActivity)) {
                    try {

                        val service = Api.getApi()!!.create(ApiInterfaces::class.java)
                        val call = service.registerAkun(
                            username = etUsername.text.toString(),
                            email = etEmail.text.toString(),
                            password = etPassword.text.toString(),
                            nama = etNama.text.toString(),
                            no_hp = etNoHp.text.toString(),
                            alamat = etAlamat.text.toString(),
                            tipe_user = tipePengguna,
                            jenis_kelamin = jenisKelamin,
                            nama_bank = etNamaBank.text.toString(),
                            no_rek = etNomorRekening.text.toString(),
                            nama_pemilik = etNamaPemilik.text.toString()
                        )

                        call.enqueue(object: retrofit2.Callback<GeneralResponse> {
                            override fun onResponse(
                                call: Call<GeneralResponse>,
                                response: Response<GeneralResponse>
                            ) {
                                if (response.body()?.status == "success") {
                                    if (tipePengguna == "pemilik") {
                                        val i = Intent(this@SignUpActivity, PemilikMainActivity::class.java)
                                        startActivity(i)
                                        finish()
                                    } else if (tipePengguna == "customer") {
                                        val i = Intent(this@SignUpActivity, CustomerMainActivity::class.java)
                                        startActivity(i)
                                        finish()
                                    }
                                } else {
                                    Toast.makeText(this@SignUpActivity, response.body()?.message.toString(), Toast.LENGTH_SHORT).show()
                                }
                            }

                            override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                                NetworkUtility.checkYourConnection(this@SignUpActivity)
                            }
                        })
                    } catch (e: Exception) {
                        e.printStackTrace()
                        NetworkUtility.checkYourConnection(this@SignUpActivity)
                    }
                } else {
                    NetworkUtility.checkYourConnection(this@SignUpActivity)
                }
            } else {
                Toast.makeText(this@SignUpActivity, "Data Belum Lengkap", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initSpinnerPengguna() {
        tipePenggunaList = getTipePenggunaList()

        var adapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, tipePenggunaList)

        bind.spinnerTipePengguna.setAdapter(adapter)

        Log.i("ListPg", tipePenggunaList.toString())
        Log.i("ListPg Opsi", tipePenggunaOpsi.toString())

//        Toast.makeText(this, tipePenggunaOpsi.toString(), Toast.LENGTH_SHORT).show()

        bind.spinnerTipePengguna.onItemSelectedListener =
            object: OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    tipePengguna = tipePenggunaOpsi[position]

//                    Toast.makeText(this@SignUpActivity, tipePengguna + position, Toast.LENGTH_SHORT).show()

                    Log.i("Pg Selected", tipePengguna)

                    if (tipePengguna == Const.PEMILIK) {
                        bind.layoutRekening.visibility = View.VISIBLE
                    } else {
                        bind.layoutRekening.visibility = View.GONE
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    bind.layoutRekening.visibility = View.GONE
                }
            }
    }

    private fun initSpinnerJenisKelamin() {
        tipeJenisKelaminList = getTipeJenisKelaminList()

        var adapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, tipeJenisKelaminList)

        bind.spinnerJenisKelamin.adapter = adapter

        Log.i("ListJK", tipeJenisKelaminList.toString())
    }

    private fun getTipePenggunaList(): MutableList<String> {

        tipePenggunaOpsi.add(0,"pemilik")
        tipePenggunaOpsi.add(1,"pelanggan")

        return tipePenggunaOpsi
    }

    private fun getTipeJenisKelaminList(): MutableList<String> {

        tipeJenisKelaminOpsi.add(0,"Laki-Laki")
        tipeJenisKelaminOpsi.add(1,"Perempuan")

        return tipeJenisKelaminOpsi
    }
}