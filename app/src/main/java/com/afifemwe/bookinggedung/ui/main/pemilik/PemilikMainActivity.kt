package com.afifemwe.bookinggedung.ui.main.pemilik

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.afifemwe.bookinggedung.R
import com.afifemwe.bookinggedung.databinding.ActivityPemilikMainBinding

class PemilikMainActivity : AppCompatActivity() {

    private lateinit var bind: ActivityPemilikMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bind = DataBindingUtil.setContentView(this, R.layout.activity_pemilik_main)


        val navController = Navigation.findNavController(this, R.id.nav_host_fragment_pemilik)
        NavigationUI.setupWithNavController(bind.navView, navController)


    }
}