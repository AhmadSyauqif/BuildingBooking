package com.afifemwe.bookinggedung.ui.main.customer.beranda

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.PopupMenu
import android.widget.SimpleAdapter
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.afifemwe.bookinggedung.R
import com.afifemwe.bookinggedung.databinding.FragmentBerandaCustomerBinding
import com.afifemwe.bookinggedung.ui.detailgedung.DetailGedungActivity
import com.afifemwe.bookinggedung.ui.main.customer.beranda.adapter.GedungListCustomerAdapter
import com.afifemwe.bookinggedung.ui.main.customer.beranda.adapter.PromoListAdapter
import com.afifemwe.bookinggedung.ui.main.pemilik.beranda.BerandaPemilikViewModel
import com.afifemwe.bookinggedung.ui.main.pemilik.beranda.adapter.GedungListPemilikAdapter
import com.afifemwe.bookinggedung.ui.main.pemilik.beranda.response.ListGedungItem
import com.afifemwe.bookinggedung.utils.Const
import com.afifemwe.bookinggedung.utils.UserPreference
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BerandaCustomerFragment : Fragment(), PopupMenu.OnMenuItemClickListener  {

    private lateinit var bind: FragmentBerandaCustomerBinding

    private lateinit var viewModel: BerandaCustomerViewModel

    private lateinit var gedungListCustomerAdapter: GedungListCustomerAdapter

    private lateinit var gedungList: List<ListGedungItem>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        bind = FragmentBerandaCustomerBinding.inflate(inflater, container, false )
        return bind.root
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel = ViewModelProvider(this).get(BerandaCustomerViewModel::class.java)
        bind.gedungList = viewModel

        initRvPromo()
        initRvGedung()

        onTextChange()

        bind.swipeContainer.setOnRefreshListener {
            initRvGedung()
            bind.swipeContainer.isRefreshing = false
        }


        bind.ivFilter.setOnClickListener {
            val popupMenu = PopupMenu(this.context, it)
            popupMenu.setOnMenuItemClickListener(this)
            popupMenu.inflate(R.menu.filter_harga_menu)
            popupMenu.gravity = Gravity.CENTER_HORIZONTAL
            popupMenu.show()
        }
    }

    private fun onTextChange() {
        bind.etSearch.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                if (gedungList.isNotEmpty()) gedungListCustomerAdapter.filter
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (gedungList.isNotEmpty()) gedungListCustomerAdapter.filter.filter(s)
            }

            override fun afterTextChanged(s: Editable?) {
                if (gedungList.isNotEmpty()) gedungListCustomerAdapter.filter.filter(s)
            }
        })
    }

    private fun initRvGedung() {

        bind.rvListGedung.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity)
        }

        viewModel.apply {
            getGedungCustomerList(context = context)

            gedungCustomerList.observe(viewLifecycleOwner, Observer {
                gedungList = it

                if (it.isEmpty() || it == null) {
//                    Toast.makeText(context, "Data Not Found", Toast.LENGTH_SHORT).show()
                    bind.apply {
                        ivWaiting.visibility = View.VISIBLE
                        rvListGedung.visibility = View.GONE
                    }
                } else {
                    bind.apply {
                        ivWaiting.visibility = View.GONE
                        rvListGedung.visibility = View.VISIBLE
                    }

                    gedungListCustomerAdapter = GedungListCustomerAdapter(gedungList, requireContext())
                    bind.rvListGedung.adapter = gedungListCustomerAdapter

                    gedungListCustomerAdapter.setOnClickCallback(object: GedungListCustomerAdapter.OnItemClickCallback {
                        override fun onItemClicked(data: ListGedungItem) {
                            val i = Intent(context, DetailGedungActivity::class.java)
                            i.putExtra(DetailGedungActivity.ID_GEDUNG_KEY, data.id)
                            startActivity(i)
                        }
                    })
                }
            })
        }

    }

    private fun initRvPromo() {
        Glide.with(bind.root)
            .load(R.drawable.dumm_gedung)
            .into(bind.ivHero)

        val listPromoDummy = listOf(
            R.drawable.poster_1,
            R.drawable.poster_2,
            R.drawable.poster_3
        )

        var promoAdapter = PromoListAdapter(context = requireContext(), listPromoPoster = listPromoDummy )

        bind.rvPromo.apply {
            setHasFixedSize(true)
            adapter = promoAdapter
        }

    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.filter_3_juta_up -> showRecyclerViewFilter(Const.PRICE_3_UP)
            R.id.filter_3_juta_down -> showRecyclerViewFilter(Const.PRICE_3_DOWN)
            R.id.filter_kapasitas_500_up -> showRecyclerViewFilter(Const.CAPACITY_500_UP)
            R.id.filter_kapasitas_500_down -> showRecyclerViewFilter(Const.CAPACITY_500_DOWN)
            else -> initRvGedung()
        }
        return true
    }


    private fun showRecyclerViewFilter(filterBy: String) {
        viewModel.apply {
            getGedungCustomerListFilter(context = context, filterBy = filterBy)

            gedungCustomerList.observe(viewLifecycleOwner, Observer {
                gedungList = it

                if (it.isEmpty() || it == null) {
//                    Toast.makeText(context, "Data Not Found", Toast.LENGTH_SHORT).show()
                    bind.apply {
                        ivWaiting.visibility = View.VISIBLE
                        rvListGedung.visibility = View.GONE
                    }
                } else {
                    bind.apply {
                        ivWaiting.visibility = View.GONE
                        rvListGedung.visibility = View.VISIBLE
                    }

                    gedungListCustomerAdapter = GedungListCustomerAdapter(gedungList, requireContext())
                    bind.rvListGedung.adapter = gedungListCustomerAdapter

                    gedungListCustomerAdapter.setOnClickCallback(object: GedungListCustomerAdapter.OnItemClickCallback {
                        override fun onItemClicked(data: ListGedungItem) {
                            val i = Intent(context, DetailGedungActivity::class.java)
                            i.putExtra(DetailGedungActivity.ID_GEDUNG_KEY, data.id)
                            startActivity(i)
                        }
                    })
                }
            })
        }
    }
}