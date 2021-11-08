package com.afifemwe.bookinggedung.utils

import android.view.View
import android.widget.EditText

object ValidateInput {
    fun validate(editText: EditText) : Boolean {
        if (editText.text.toString().isNotEmpty()) {
            return true
        } else {
            editText.error = "Data Wajib Diisi"
            return false
        }
    }

    fun validatePassword(editText: EditText) : Boolean {
        if (editText.text.toString().isNotEmpty()) {
            return true
        } else {
            editText.requestFocus()
            return false
        }
    }
}