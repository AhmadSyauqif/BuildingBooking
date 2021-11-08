package com.afifemwe.bookinggedung.ui.main.pemilik.beranda

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.PopupMenu
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.afifemwe.bookinggedung.R
import com.afifemwe.bookinggedung.databinding.FragmentBerandaPemilikBinding
import com.afifemwe.bookinggedung.ui.creategedung.CreateGedungActivity
import com.afifemwe.bookinggedung.ui.detailgedung.DetailGedungActivity
import com.afifemwe.bookinggedung.ui.detailgedung.DetailGedungActivity.Companion.ID_GEDUNG_KEY
import com.afifemwe.bookinggedung.ui.main.pemilik.beranda.adapter.GedungListPemilikAdapter
import com.afifemwe.bookinggedung.ui.main.pemilik.beranda.response.ListGedungItem
import com.afifemwe.bookinggedung.utils.Const
import com.afifemwe.bookinggedung.utils.UserPreference

class BerandaPemilikFragment : Fragment(), PopupMenu.OnMenuItemClickListener {

    private lateinit var bind: FragmentBerandaPemilikBinding

    private lateinit var viewModel: BerandaPemilikViewModel

    private lateinit var gedungListPemilikAdapter: GedungListPemilikAdapter

    private lateinit var gedungList: List<ListGedungItem>

    private lateinit var pemilik: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        bind = FragmentBerandaPemilikBinding.inflate(inflater, container, false)
        return bind.root
    }



    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(BerandaPemilikViewModel::class.java)
        bind.gedungList = viewModel

        bind.rvGedung.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity)
        }

        val userPreference = UserPreference(requireContext())
        pemilik = userPreference.getUsername().toString()


        showRecyclerView()
        onTextChange()

        bind.swipeContainer.setOnRefreshListener {
            showRecyclerView()
            bind.swipeContainer.isRefreshing = false
        }

        bind.fabAddGedung.setOnClickListener {
            val i = Intent(context, CreateGedungActivity::class.java)
            startActivity(i)
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
                if (gedungList.isNotEmpty()) gedungListPemilikAdapter.filter
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (gedungList.isNotEmpty()) gedungListPemilikAdapter.filter.filter(s)
            }

            override fun afterTextChanged(s: Editable?) {
                if (gedungList.isNotEmpty()) gedungListPemilikAdapter.filter.filter(s)
            }
        })
    }

    private fun showRecyclerView() {
        viewModel.apply {
            getGedungPemilikList(context = context, pemilik = pemilik)

            gedungPemilikList.observe(viewLifecycleOwner, Observer {
                gedungList = it

                if (it.isEmpty() || it == null) {
//                    Toast.makeText(context, "Data Not Found", Toast.LENGTH_SHORT).show()
                    bind.apply {
                        ivWaiting.visibility = View.VISIBLE
                        rvGedung.visibility = View.GONE
                    }
                } else {
                    bind.apply {
                        ivWaiting.visibility = View.GONE
                        rvGedung.visibility = View.VISIBLE
                    }

                    gedungListPemilikAdapter = GedungListPemilikAdapter(gedungList, requireContext())
                    bind.rvGedung.adapter = gedungListPemilikAdapter

                    gedungListPemilikAdapter.setOnClickCallback(object: GedungListPemilikAdapter.OnItemClickCallback {
                        override fun onItemClicked(data: ListGedungItem) {
                            val i = Intent(context, DetailGedungActivity::class.java)
                            i.putExtra(ID_GEDUNG_KEY, data.id)
                            startActivity(i)
                        }
                    })
                }
            })
        }

    }

    override fun onResume() {
        super.onResume()
        showRecyclerView()
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.filter_3_juta_up -> showRecyclerViewFilter(Const.PRICE_3_UP)
            R.id.filter_3_juta_down -> showRecyclerViewFilter(Const.PRICE_3_DOWN)
            R.id.filter_kapasitas_500_up -> showRecyclerViewFilter(Const.CAPACITY_500_UP)
            R.id.filter_kapasitas_500_down -> showRecyclerViewFilter(Const.CAPACITY_500_DOWN)
            else -> showRecyclerView()
        }
        return true
    }

    private fun showRecyclerViewFilter(filterBy: String) {
        viewModel.apply {
            getGedungPemilikListFilter(context = context, pemilik = pemilik, filterBy = filterBy)

            gedungPemilikList.observe(viewLifecycleOwner, Observer {
                gedungList = it

                if (it.isEmpty() || it == null) {
//                    Toast.makeText(context, "Data Not Found", Toast.LENGTH_SHORT).show()
                    bind.apply {
                        ivWaiting.visibility = View.VISIBLE
                        rvGedung.visibility = View.GONE
                    }
                } else {
                    bind.apply {
                        ivWaiting.visibility = View.GONE
                        rvGedung.visibility = View.VISIBLE
                    }

                    gedungListPemilikAdapter = GedungListPemilikAdapter(gedungList, requireContext())
                    bind.rvGedung.adapter = gedungListPemilikAdapter

                    gedungListPemilikAdapter.setOnClickCallback(object: GedungListPemilikAdapter.OnItemClickCallback {
                        override fun onItemClicked(data: ListGedungItem) {
                            val i = Intent(context, DetailGedungActivity::class.java)
                            i.putExtra(ID_GEDUNG_KEY, data.id)
                            startActivity(i)
                        }
                    })
                }
            })
        }
    }


}