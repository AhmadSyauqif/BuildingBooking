package com.afifemwe.bookinggedung.ui.splashscreen

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.afifemwe.bookinggedung.R
import com.afifemwe.bookinggedung.databinding.ActivitySplashScreenBinding
import com.afifemwe.bookinggedung.ui.main.customer.CustomerMainActivity
import com.afifemwe.bookinggedung.ui.main.pemilik.PemilikMainActivity
import com.afifemwe.bookinggedung.ui.signin.SignInActivity
import com.afifemwe.bookinggedung.ui.signup.SignUpActivity
import com.afifemwe.bookinggedung.utils.Const
import com.afifemwe.bookinggedung.utils.UserPreference

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var bind: ActivitySplashScreenBinding

    private lateinit var username: String
    private lateinit var tipePengguna: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bind = DataBindingUtil.setContentView(this, R.layout.activity_splash_screen)

        checkLogin()

        bind.btnSignUp.setOnClickListener {
            val i = Intent(this@SplashScreenActivity, SignUpActivity::class.java)
            startActivity(i)
        }

        bind.btnSignIn.setOnClickListener {
            val i = Intent(this@SplashScreenActivity, SignInActivity::class.java)
            startActivity(i)
        }
    }

    private fun checkLogin() {
        val userPref = UserPreference(this)

        if (!userPref.getTipePengguna().equals(null) && !userPref.getUsername().equals(null)) {
            tipePengguna = userPref.getTipePengguna().toString()

            when(tipePengguna) {
                Const.CUSTOMER -> goToMain(CustomerMainActivity::class.java)

                Const.PEMILIK -> goToMain(PemilikMainActivity::class.java)
            }
        }
    }

    private fun goToMain(activity: Class<*>) {
        val i = Intent(this, activity)
        startActivity(i)
        finish()
    }
}