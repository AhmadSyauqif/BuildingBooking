package com.afifemwe.bookinggedung.utils

import android.content.Context
import android.widget.Toast

class CustomToast() {
    companion object {
        fun showMessage(context: Context, message: String) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
}