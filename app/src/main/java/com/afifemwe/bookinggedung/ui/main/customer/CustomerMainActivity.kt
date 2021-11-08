package com.afifemwe.bookinggedung.ui.main.customer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.afifemwe.bookinggedung.R
import com.afifemwe.bookinggedung.databinding.ActivityCustomerMainBinding

class CustomerMainActivity : AppCompatActivity() {

    private lateinit var bind: ActivityCustomerMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bind = DataBindingUtil.setContentView(this, R.layout.activity_customer_main)

        val navController = Navigation.findNavController(this, R.id.nav_host_fragment_customer)
        NavigationUI.setupWithNavController(bind.navView, navController)

    }
}