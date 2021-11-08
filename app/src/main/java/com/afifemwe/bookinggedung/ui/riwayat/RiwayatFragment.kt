package com.afifemwe.bookinggedung.ui.riwayat

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.afifemwe.bookinggedung.databinding.FragmentRiwayatBinding
import com.afifemwe.bookinggedung.ui.detailbooking.DetailBookingActivity
import com.afifemwe.bookinggedung.ui.detailbooking.DetailBookingActivity.Companion.ID_BOOKING_KEY
import com.afifemwe.bookinggedung.ui.riwayat.adapter.RiwayatBookingAdapter
import com.afifemwe.bookinggedung.ui.riwayat.response.ListRiwayatBookingItem
import com.afifemwe.bookinggedung.ui.riwayat.viewmodel.RiwayatBookingCustomerViewModel
import com.afifemwe.bookinggedung.ui.riwayat.viewmodel.RiwayatBookingPemilikViewModel
import com.afifemwe.bookinggedung.utils.Const.Companion.CUSTOMER
import com.afifemwe.bookinggedung.utils.Const.Companion.PEMILIK
import com.afifemwe.bookinggedung.utils.UserPreference

class RiwayatFragment : Fragment() {

    private lateinit var bind: FragmentRiwayatBinding

    private var username = ""
    private var tipe_pengguna = ""


    private lateinit var customerViewModel: RiwayatBookingCustomerViewModel
    private lateinit var pemilikViewModel: RiwayatBookingPemilikViewModel

    private lateinit var riwayatBookingAdapter: RiwayatBookingAdapter

    private lateinit var riwayatBookingList: List<ListRiwayatBookingItem>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        bind = FragmentRiwayatBinding.inflate(inflater, container, false )
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUserPreference()

        bind.swipeContainer.setOnRefreshListener {
            if (tipe_pengguna == PEMILIK ) {
                initListRiwayatPemilik()
            } else if (tipe_pengguna == CUSTOMER) {
                initListRiwayatCustomer()
            }

            bind.swipeContainer.isRefreshing = false
        }

        if (tipe_pengguna == PEMILIK ) {
            initListRiwayatPemilik()
        } else if (tipe_pengguna == CUSTOMER) {
            initListRiwayatCustomer()
        }
    }

    override fun onResume() {
        super.onResume()

        if (tipe_pengguna == PEMILIK ) {
            initListRiwayatPemilik()
        } else if (tipe_pengguna == CUSTOMER) {
            initListRiwayatCustomer()
        }
    }

    private fun initListRiwayatCustomer() {
        customerViewModel = ViewModelProvider(this).get(RiwayatBookingCustomerViewModel::class.java)


        bind.apply {
            riwayatCustomerList = customerViewModel
            rvListRiwayat.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(activity)
                visibility = View.VISIBLE
            }
        }

        customerViewModel.apply {
            getRiwayatBookingCustomerList(context = requireContext(), username = username)

            riwayatBookingList.observe(viewLifecycleOwner, Observer {
                if (it != null) {
                    riwayatBookingAdapter = RiwayatBookingAdapter(it,requireContext())
                    bind.rvListRiwayat.adapter = riwayatBookingAdapter

                    riwayatBookingAdapter.setOnClickCallback(object: RiwayatBookingAdapter.OnItemClickCallback {
                        override fun onItemClicked(data: ListRiwayatBookingItem) {
                            Toast.makeText(requireContext(), data.id, Toast.LENGTH_SHORT).show()
                            Log.i("ID Booking", data.id.toString())
                            goToDetailBooking(data.id)
                        }
                    })
                }

            })
        }
    }

    private fun initListRiwayatPemilik() {
        pemilikViewModel = ViewModelProvider(this).get(RiwayatBookingPemilikViewModel::class.java)


        bind.apply {
            riwayatPemilikList = pemilikViewModel
            rvListRiwayat.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(activity)
                visibility = View.VISIBLE
            }
        }

        pemilikViewModel.apply {
            getRiwayatBookingPemilikList(context = requireContext(), username = username)

            riwayatBookingList.observe(viewLifecycleOwner, Observer {
                if (it != null) {
                    riwayatBookingAdapter = RiwayatBookingAdapter(it,requireContext())
                    bind.rvListRiwayat.adapter = riwayatBookingAdapter

                    riwayatBookingAdapter.setOnClickCallback(object: RiwayatBookingAdapter.OnItemClickCallback {
                        override fun onItemClicked(data: ListRiwayatBookingItem) {
//                            Toast.makeText(requireContext(), data.id, Toast.LENGTH_SHORT).show()
                            Log.i("ID Booking", data.id.toString())
                            goToDetailBooking(data.id)
                        }
                    })
                }

            })
        }
    }

    private fun goToDetailBooking(id: String?) {
        val i = Intent(activity, DetailBookingActivity::class.java)
        i.putExtra(ID_BOOKING_KEY, id)
        startActivity(i)
    }

    private fun initUserPreference() {
        val userPref = UserPreference(requireContext())
        username = userPref.getUsername().toString()
        tipe_pengguna = userPref.getTipePengguna().toString()
    }
}