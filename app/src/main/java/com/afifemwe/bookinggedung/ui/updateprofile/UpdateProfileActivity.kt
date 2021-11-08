package com.afifemwe.bookinggedung.ui.updateprofile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.databinding.DataBindingUtil
import com.afifemwe.bookinggedung.R
import com.afifemwe.bookinggedung.api.Api
import com.afifemwe.bookinggedung.api.ApiInterfaces
import com.afifemwe.bookinggedung.databinding.ActivityUpdateProfileBinding
import com.afifemwe.bookinggedung.model.GeneralResponse
import com.afifemwe.bookinggedung.ui.akun.response.DetailAkunResponse
import com.afifemwe.bookinggedung.ui.main.customer.CustomerMainActivity
import com.afifemwe.bookinggedung.ui.main.pemilik.PemilikMainActivity
import com.afifemwe.bookinggedung.utils.Const
import com.afifemwe.bookinggedung.utils.NetworkUtility
import com.afifemwe.bookinggedung.utils.UserPreference
import com.afifemwe.bookinggedung.utils.ValidateInput
import retrofit2.Call
import retrofit2.Response

class UpdateProfileActivity : AppCompatActivity() {

    private lateinit var bind: ActivityUpdateProfileBinding
    private lateinit var tipePenggunaList: MutableList<String>
    private lateinit var tipeJenisKelaminList: MutableList<String>

    private var username                = ""
    private var tipePengguna            = ""
    private var password                = ""

    private var jenisKelamin: String    = ""

    private val tipePenggunaOpsi: ArrayList<String>         = ArrayList()
    private val tipeJenisKelaminOpsi: ArrayList<String>     = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bind    = DataBindingUtil.setContentView(this, R.layout.activity_update_profile)

        initSpinnerPengguna()
        initSpinnerJenisKelamin()
        initUserPreference()

        getDetailAkun()
    }

    private fun initSpinnerJenisKelamin() {
        tipeJenisKelaminList = getTipeJenisKelaminList()

        var adapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, tipeJenisKelaminList)

        bind.spinnerJenisKelamin.adapter = adapter

        Log.i("ListJK", tipeJenisKelaminList.toString())
    }

    private fun initSpinnerPengguna() {
        tipePenggunaList = getTipePenggunaList()

        var adapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, tipePenggunaList)

        bind.spinnerTipePengguna.setAdapter(adapter)

        Log.i("ListPg", tipePenggunaList.toString())
        Log.i("ListPg Opsi", tipePenggunaOpsi.toString())

//        Toast.makeText(this, tipePenggunaOpsi.toString(), Toast.LENGTH_SHORT).show()

        bind.spinnerTipePengguna.onItemSelectedListener =
            object: AdapterView.OnItemSelectedListener {
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

    private fun getDetailAkun() {
        if(NetworkUtility.isInternetAvailable(this)) {
            try {
                val service = Api.getApi()!!.create(ApiInterfaces::class.java)
                val call = service.getDetailAkun(
                    username = username
                )

                call.enqueue(object : retrofit2.Callback<DetailAkunResponse> {
                    override fun onResponse(
                        call: Call<DetailAkunResponse>,
                        response: Response<DetailAkunResponse>
                    ) {
                        if (response.body()?.status == Const.SUCCESS_RESPONSE) {
                            val data = response.body()?.akunDetail

                            if (data != null) {

                                Log.i("Res", data.toString())
                                bind.apply {
                                    etNama.setText(data.nama)
                                    etAlamat.setText(data.alamat)
                                    etEmail.setText(data.email)
//                                    tvJenisKelamin.text   = data.jenisKelamin
                                    etNoHp.setText(data.noHp)
                                    etNamaBank.setText(data.namaBank)
                                    etNomorRekening.setText(data.noRekening)
                                    etNamaPemilik.setText(data.namaPemilik)
                                    etUsername.setText(data.username)

                                    tipePengguna = data.tipeUser.toString()
                                    jenisKelamin = data.jenisKelamin.toString()

                                    if (tipePengguna == Const.PEMILIK) {
                                        layoutRekening.visibility = View.VISIBLE
                                    } else {
                                        layoutRekening.visibility = View.GONE
                                    }

                                    spinnerJenisKelamin.setSelection(tipeJenisKelaminOpsi.indexOf(data.jenisKelamin))
                                    spinnerTipePengguna.setSelection(tipePenggunaOpsi.indexOf(data.tipeUser))

                                    btnUpdateAkun.setOnClickListener {
                                        updateAkun()
                                        Log.i("Update", "Process ...")
                                    }
//                                    btnUpdateAkun.setOnClickListener {
//                                        val i = Intent(activity, UpdateProfileActivity::class.java)
//                                        i.putExtra(Const.ID_USER_KEY, data.id)
//                                        startActivity(i)
//                                    }
                                }
                            }

                        } else {
                            Toast.makeText(
                                this@UpdateProfileActivity,
                                response.body()?.message.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<DetailAkunResponse>, t: Throwable) {
                        NetworkUtility.checkYourConnection(this@UpdateProfileActivity)
                    }
                })
            } catch (e: Exception) {
                e.printStackTrace()
                NetworkUtility.checkYourConnection(this)
            }
        }
    }

    private fun updateAkun() {
        bind.apply {
            spinnerTipePengguna.onItemClickListener

            Log.i("Username", etUsername.text.toString())
            Log.i("Nama", etNama.text.toString())
            Log.i("Email", etEmail.text.toString())
            Log.i("Jenis Kelamin", jenisKelamin)
            Log.i("Tipe User", tipePengguna)

            ValidateInput.apply {
                validate(etNama)
                validate(etUsername)
                validate(etEmail)
            }

            if (ValidateInput.validate(etNama) && ValidateInput.validate(etUsername) && ValidateInput.validate(etEmail) && jenisKelamin != "" && tipePengguna != "") {
                if(NetworkUtility.isInternetAvailable(this@UpdateProfileActivity)) {
                    try {
                        Log.i("Update", "Proses ...")

                        val service = Api.getApi()!!.create(ApiInterfaces::class.java)
                        val call = service.updateAkun(
                            username = etUsername.text.toString(),
                            email = etEmail.text.toString(),
                            password = etChangePassword.text.toString(),
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
                                        val i = Intent(this@UpdateProfileActivity, PemilikMainActivity::class.java)
                                        startActivity(i)
                                        finish()
                                    } else if (tipePengguna == "customer") {
                                        val i = Intent(this@UpdateProfileActivity, CustomerMainActivity::class.java)
                                        startActivity(i)
                                        finish()
                                    }
                                } else {
                                    Toast.makeText(this@UpdateProfileActivity, response.body()?.message.toString(), Toast.LENGTH_SHORT).show()
                                }
                            }

                            override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                                NetworkUtility.checkYourConnection(this@UpdateProfileActivity)
                            }
                        })
                    } catch (e: Exception) {
                        e.printStackTrace()
                        NetworkUtility.checkYourConnection(this@UpdateProfileActivity)
                    }
                } else {
                    NetworkUtility.checkYourConnection(this@UpdateProfileActivity)
                }
            } else {
                Toast.makeText(this@UpdateProfileActivity, "Data Belum Lengkap", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initUserPreference() {
        val userPref        = UserPreference(this)
        username            = userPref.getUsername().toString()
        tipePengguna        = userPref.getTipePengguna().toString()
    }
}