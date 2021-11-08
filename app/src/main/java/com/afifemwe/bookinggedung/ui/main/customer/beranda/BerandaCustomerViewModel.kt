package com.afifemwe.bookinggedung.ui.main.customer.beranda

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.afifemwe.bookinggedung.api.Api
import com.afifemwe.bookinggedung.api.ApiInterfaces
import com.afifemwe.bookinggedung.ui.main.pemilik.beranda.response.GetGedungListResponse
import com.afifemwe.bookinggedung.ui.main.pemilik.beranda.response.ListGedungItem
import com.afifemwe.bookinggedung.utils.NetworkUtility
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BerandaCustomerViewModel(application: Application): AndroidViewModel(application) {

    var gedungCustomerList = MutableLiveData<List<ListGedungItem>>()

    fun getGedungCustomerList(
        context: Context?
    ) {
        if (NetworkUtility.isInternetAvailable(context!!)) {
            try {
                val service = Api.getApi()!!.create(ApiInterfaces::class.java)
                val call = service.getGedungListCustomer()
                call.enqueue(object : Callback<GetGedungListResponse> {
                    override fun onResponse(
                        call: Call<GetGedungListResponse>,
                        response: Response<GetGedungListResponse>
                    ) {
                        if (response.body() != null) {
                            gedungCustomerList.postValue(response.body()?.listGedung as List<ListGedungItem>)
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


    fun getGedungCustomerListFilter(
        context: Context?,
        filterBy: String
    ) {
        if (NetworkUtility.isInternetAvailable(context!!)) {
            try {
                val service = Api.getApi()!!.create(ApiInterfaces::class.java)
                val call = service.getGedungListCustomerFilter(filterBy = filterBy)
                call.enqueue(object : Callback<GetGedungListResponse> {
                    override fun onResponse(
                        call: Call<GetGedungListResponse>,
                        response: Response<GetGedungListResponse>
                    ) {
                        if (response.body() != null) {
                            gedungCustomerList.postValue(response.body()?.listGedung as List<ListGedungItem>)
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



