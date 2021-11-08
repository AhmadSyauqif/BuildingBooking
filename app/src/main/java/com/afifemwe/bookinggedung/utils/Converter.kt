package com.afifemwe.bookinggedung.utils

import java.text.NumberFormat
import java.util.*

object Converter {
    fun formatRupiah(number: String?): String {
        val nominal = number?.toDouble()

        val localeID = Locale("in", "ID")
        val numberFormat = NumberFormat.getCurrencyInstance(localeID)

        return numberFormat.format(nominal).toString()

    }
}