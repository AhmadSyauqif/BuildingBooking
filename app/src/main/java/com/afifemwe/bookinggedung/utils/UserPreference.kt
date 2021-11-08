package com.afifemwe.bookinggedung.utils

import android.content.Context

class UserPreference(context: Context) {
    companion object {
        private const val PREFS_NAME = "user_pref"
        private const val USERNAME_KEY = "username"
        private const val TIPE_PENGGUNA_KEY = "tipe_pengguna"
    }

    private val preference = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun setUsername(mUsername: String?) {
        val editor = preference.edit()
        editor.putString(USERNAME_KEY, mUsername)
        editor.apply()
    }

    fun getUsername() : String? {
        return preference.getString(USERNAME_KEY, "")
    }

    fun setTipePengguna(mTipePengguna: String?) {
        val editor = preference.edit()
        editor.putString(TIPE_PENGGUNA_KEY, mTipePengguna)
        editor.apply()
    }

    fun getTipePengguna() : String? {
        return preference.getString(TIPE_PENGGUNA_KEY, "")
    }

    fun clearPreference() {
        preference.edit().clear().apply()
    }

}