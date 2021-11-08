package com.afifemwe.bookinggedung.ui.main.pemilik.beranda

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.afifemwe.bookinggedung.api.Api
import com.afifemwe.bookinggedung.api.ApiInterfaces
import com.afifemwe.bookinggedung.model.Gedung
import com.afifemwe.bookinggedung.ui.main.pemilik.beranda.response.GetGedungListResponse
import com.afifemwe.bookinggedung.ui.main.pemilik.beranda.response.ListGedungItem
import com.afifemwe.bookinggedung.utils.NetworkUtility
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BerandaPemilikViewModel(application: Application): AndroidViewModel(application) {

    var gedungPemilikList = MutableLiveData<List<ListGedungItem>>()

    fun getGedungPemilikList(
        context: Context?,
        pemilik: String
    ) {
        if (NetworkUtility.isInternetAvailable(context!!)) {
            try {
                val service = Api.getApi()!!.create(ApiInterfaces::class.java)
                val call = service.getGedungListPemilik(pemilik = pemilik)
                call.enqueue(object: Callback<GetGedungListResponse> {
                    override fun onResponse(
                        call: Call<GetGedungListResponse>,
                        response: Response<GetGedungListResponse>
                    ) {
                        if (response.body()!= null) {
                            gedungPemilikList.postValue(response.body()?.listGedung as List<ListGedungItem>)
                        } else {
                            NetworkUtility.checkYourConnection(context)
                        }
                    }

                    override fun onFailure(call: Call<GetGedungListResponse>, t: Throwable) {
                        NetworkUtility.checkYourConnection(context)
                    }
                })
            } catch (e: Exception) {
                e.printStackTrace()
                NetworkUtility.checkYourConnection(context)
            }
        } else {
            NetworkUtility.checkYourConnection(context)
        }
    }


    fun getGedungPemilikListFilter(
        context: Context?,
        pemilik: String,
        filterBy: String
    ) {
        if (NetworkUtility.isInternetAvailable(context!!)) {
            try {
                val service = Api.getApi()!!.create(ApiInterfaces::class.java)
                val call = service.getGedungListPemilikFilter(pemilik = pemilik, filterBy = filterBy)
                call.enqueue(object: Callback<GetGedungListResponse> {
                    override fun onResponse(
                        call: Call<GetGedungListResponse>,
                        response: Response<GetGedungListResponse>
                    ) {
                        if (response.body()!= null) {
                            gedungPemilikList.postValue(response.body()?.listGedung as List<ListGedungItem>)
                        } else {
                            NetworkUtility.checkYourConnection(context)
                        }
                    }

                    override fun onFailure(call: Call<GetGedungListResponse>, t: Throwable) {
                        NetworkUtility.checkYourConnection(context)
                    }
                })
            } catch (e: Exception) {
                e.printStackTrace()
                NetworkUtility.checkYourConnection(context)
            }
        } else {
            NetworkUtility.checkYourConnection(context)
        }
    }



}