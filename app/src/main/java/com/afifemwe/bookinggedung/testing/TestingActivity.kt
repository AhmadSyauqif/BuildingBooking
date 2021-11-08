package com.afifemwe.bookinggedung.testing

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.afifemwe.bookinggedung.R
import com.afifemwe.bookinggedung.databinding.ActivityTestingBinding

class TestingActivity : AppCompatActivity() {

    private lateinit var bind: ActivityTestingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bind = DataBindingUtil.setContentView(this, R.layout.activity_testing)

        bind.btnTest.text = "Test"

        bind.btnTest.setOnClickListener {
            val link = "https://goo.gl/maps/zy1GiB4M6mgNxz2dA"

            val i = Intent(Intent.ACTION_VIEW).setData(Uri.parse(link))
            startActivity(i)
        }
    }
}