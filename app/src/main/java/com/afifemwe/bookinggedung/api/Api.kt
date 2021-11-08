package com.afifemwe.bookinggedung.api

import com.afifemwe.bookinggedung.utils.Const
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class Api {
    companion object {
        private var retrofit: Retrofit? = null

        fun getApi(): Retrofit? {
            if (retrofit == null) {
                val clientSetup = OkHttpClient.Builder()
                    .connectTimeout(1, TimeUnit.MINUTES)
                    .writeTimeout(1, TimeUnit.MINUTES) // write timeout
                    .readTimeout(1, TimeUnit.MINUTES) // read timeout
                    .build()

                val gson = GsonBuilder()
                    .setLenient()
                    .create()

                retrofit = Retrofit.Builder()
                    .baseUrl(Const.BASE_URL)
                    .client(clientSetup)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
            }

            return retrofit
        }

    }
}