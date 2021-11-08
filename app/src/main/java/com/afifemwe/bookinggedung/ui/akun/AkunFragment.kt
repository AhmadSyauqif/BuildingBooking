package com.afifemwe.bookinggedung.ui.akun

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.afifemwe.bookinggedung.api.Api
import com.afifemwe.bookinggedung.api.ApiInterfaces
import com.afifemwe.bookinggedung.databinding.FragmentAkunBinding
import com.afifemwe.bookinggedung.ui.akun.response.DetailAkunResponse
import com.afifemwe.bookinggedung.ui.signin.SignInActivity
import com.afifemwe.bookinggedung.ui.updateprofile.UpdateProfileActivity
import com.afifemwe.bookinggedung.utils.Const
import com.afifemwe.bookinggedung.utils.Const.Companion.ID_USER_KEY
import com.afifemwe.bookinggedung.utils.Const.Companion.PEMILIK
import com.afifemwe.bookinggedung.utils.NetworkUtility
import com.afifemwe.bookinggedung.utils.UserPreference
import retrofit2.Call
import retrofit2.Response

class AkunFragment : Fragment() {

    private lateinit var bind: FragmentAkunBinding

    private var username = ""
    private var tipePengguna = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        bind = FragmentAkunBinding.inflate(inflater, container, false)
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUserPreference()

        getDetailAkun()

        bind.btnLogOut.setOnClickListener { logOut() }

        bind.swipeContainer.setOnRefreshListener {
            getDetailAkun()
            bind.swipeContainer.isRefreshing = false
        }
    }

    private fun logOut() {
        val userPreference = UserPreference(requireContext())
        userPreference.clearPreference()

        val i = Intent(activity, SignInActivity::class.java)
        startActivity(i)
        requireActivity().finish()

    }

    private fun getDetailAkun() {
        if(NetworkUtility.isInternetAvailable(requireContext())) {
            try {
                val service = Api.getApi()!!.create(ApiInterfaces::class.java)
                val call = service.getDetailAkun(
                    username = username
                )

                call.enqueue(object: retrofit2.Callback<DetailAkunResponse> {
                    override fun onResponse(
                        call: Call<DetailAkunResponse>,
                        response: Response<DetailAkunResponse>
                    ) {
                        if (response.body()?.status == Const.SUCCESS_RESPONSE) {
                            val data = response.body()?.akunDetail

                            if (data != null) {

                                Log.i("Res", data.toString())
                                bind.apply {
                                    tvNama.text                 = data.nama
                                    tvAlamat.text               = data.alamat
                                    tvEmail.text                = data.email
                                    tvJenisKelamin.text         = data.jenisKelamin
                                    tvNoHp.text                 = data.noHp
                                    tvNamaBank.text             = data.namaBank
                                    tvNoRekening.text           = data.noRekening
                                    tvNamaPemilikRekening.text  = data.namaPemilik
                                    tvUsername.text             = data.username

                                    if (tipePengguna == PEMILIK) {
                                        layoutRekening.visibility = View.VISIBLE
                                    } else {
                                        layoutRekening.visibility = View.GONE
                                    }

                                    btnUpdateAkun.setOnClickListener {
                                        val i   = Intent(activity, UpdateProfileActivity::class.java)
                                        i.putExtra(ID_USER_KEY, data.id)
                                        startActivity(i)
                                    }
                                }
                            }

                        } else {
                            Toast.makeText(requireContext(), response.body()?.message.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<DetailAkunResponse>, t: Throwable) {
                        NetworkUtility.checkYourConnection(requireContext())
                    }
                })
            } catch (e: Exception) {
                e.printStackTrace()
                NetworkUtility.checkYourConnection(requireContext())
            }
        }
    }

    private fun initUserPreference() {
        val userPref = UserPreference(requireContext())
        username = userPref.getUsername().toString()
        tipePengguna = userPref.getTipePengguna().toString()
    }
}