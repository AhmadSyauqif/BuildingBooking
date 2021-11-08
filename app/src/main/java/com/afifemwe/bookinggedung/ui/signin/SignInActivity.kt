package com.afifemwe.bookinggedung.ui.signin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.afifemwe.bookinggedung.R
import com.afifemwe.bookinggedung.api.Api
import com.afifemwe.bookinggedung.api.ApiInterfaces
import com.afifemwe.bookinggedung.databinding.ActivitySignInBinding
import com.afifemwe.bookinggedung.model.GeneralResponse
import com.afifemwe.bookinggedung.ui.main.customer.CustomerMainActivity
import com.afifemwe.bookinggedung.ui.main.pemilik.PemilikMainActivity
import com.afifemwe.bookinggedung.ui.signup.SignUpActivity
import com.afifemwe.bookinggedung.utils.*
import com.afifemwe.bookinggedung.utils.Const.Companion.CUSTOMER
import com.afifemwe.bookinggedung.utils.Const.Companion.PEMILIK
import retrofit2.Call
import retrofit2.Response

class SignInActivity : AppCompatActivity() {

    private lateinit var bind: ActivitySignInBinding
    private lateinit var tipePenggunaList: MutableList<String>

    private lateinit var username: String
    private lateinit var password: String
    private lateinit var tipePengguna: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bind = DataBindingUtil.setContentView(this, R.layout.activity_sign_in)

        initUserPreference()

        initSpinnerTipePengguna()

        bind.btnSignIn.setOnClickListener { verification() }

        bind.tvBuatAkun.setOnClickListener{ goToSignUp() }
    }

    private fun goToSignUp() {
        val i = Intent(this, SignUpActivity::class.java)
        startActivity(i)
    }

    private fun initUserPreference() {
        val userPreference = UserPreference(this)
        val username = userPreference.getUsername()
        val tipePengguna = userPreference.getTipePengguna()

        if (username != "" || username != null) {
            when(tipePengguna) {
                PEMILIK -> goToMain(PemilikMainActivity::class.java)
                CUSTOMER -> goToMain(CustomerMainActivity::class.java)
            }
        }
    }

    private fun initSpinnerTipePengguna() {
        tipePenggunaList = getTipePenggunaList()

        var adapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, tipePenggunaList)

        bind.spinnerTipePengguna.setAdapter(adapter)

        Log.i("ListPg", tipePenggunaList.toString())
    }

    private fun verification() {
        bind.apply {
            username = etUsername.text.toString()
            password = etPassword.text.toString()
            tipePengguna = spinnerTipePengguna.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty() && tipePengguna.isNotEmpty()) {

                if(NetworkUtility.isInternetAvailable(this@SignInActivity)) {
                    try {
                        val service = Api.getApi()!!.create(ApiInterfaces::class.java)
                        val call = service.checkLogin(
                            username = username,
                            password = password,
                            tipe_user = tipePengguna
                        )

                        call.enqueue(object: retrofit2.Callback<GeneralResponse> {
                            override fun onResponse(
                                call: Call<GeneralResponse>,
                                response: Response<GeneralResponse>
                            ) {

                                val userPref = UserPreference(this@SignInActivity)
                                userPref.setUsername(username)
                                userPref.setTipePengguna(tipePengguna)

//                                Toast.makeText(this@SignInActivity, tipePengguna, Toast.LENGTH_SHORT).show()

                                if (response.body()?.status == Const.SUCCESS_RESPONSE) {
                                    when(tipePengguna) {
                                        Const.PEMILIK -> {
                                            goToMain(PemilikMainActivity::class.java)

                                        }

                                        Const.CUSTOMER -> {
                                            goToMain(CustomerMainActivity::class.java)
//                                            val i = Intent(this@SignInActivity, CustomerMainActivity::class.java)
//                                            startActivity(i)
//                                            finish()
//                                            Toast.makeText(this@SignInActivity, " Customer Activty", Toast.LENGTH_SHORT).show()
                                        }
                                        else -> {
                                            NetworkUtility.checkYourConnection(this@SignInActivity)
                                        }
                                    }
                                } else {
                                    Toast.makeText(this@SignInActivity, response.body()?.message.toString(), Toast.LENGTH_SHORT).show()
                                }
                            }

                            override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                                NetworkUtility.checkYourConnection(this@SignInActivity)
                            }
                        })
                    } catch (e: Exception) {
                        e.printStackTrace()
                        NetworkUtility.checkYourConnection(this@SignInActivity)
                    }
                }



//                Toast.makeText(this@SignInActivity, "Berhasil Save UserPreference", Toast.LENGTH_SHORT).show()
//
//                when(tipePengguna) {
//                    Const.PEMILIK -> goToMain(CustomerMainActivity::class.java)
//                    Const.CUSTOMER -> goToMain(PemilikMainActivity::class.java)
//                }
            } else {
                CustomToast.showMessage(this@SignInActivity, "Data Belum Lengkap")
            }
        }
    }

    private fun getTipePenggunaList(): MutableList<String> {
        val pengguna: ArrayList<String> = ArrayList()

        pengguna.add(0, Const.PEMILIK)
        pengguna.add(1, Const.CUSTOMER)

        return pengguna
    }

    fun goToMain(activity: Class<*>) {
        val i = Intent(this, activity)
        startActivity(i)
        finish()
    }
}