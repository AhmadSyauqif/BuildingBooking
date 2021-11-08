package com.afifemwe.bookinggedung.ui.main.pemilik.beranda.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.afifemwe.bookinggedung.R
import com.afifemwe.bookinggedung.databinding.LayoutItemExploreGedungBinding
import com.afifemwe.bookinggedung.ui.main.pemilik.beranda.response.ListGedungItem
import com.afifemwe.bookinggedung.utils.Const
import com.afifemwe.bookinggedung.utils.Converter
import com.bumptech.glide.Glide
import java.util.*

class GedungListPemilikAdapter(
    private var listGedung: List<ListGedungItem>,
    context: Context
) : RecyclerView.Adapter<GedungListPemilikAdapter.ViewHolder>(), Filterable {

    private var onItemItemClickCallback: OnItemClickCallback? = null
    private var gedungPemilikFilterList: List<ListGedungItem> = emptyList()

    init {
        gedungPemilikFilterList = listGedung
        Log.i("List Gedung Pemilik", listGedung.toString())

        if (listGedung.isEmpty()) {
            Toast.makeText(context, "Data Not Found", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GedungListPemilikAdapter.ViewHolder {
        val bind = LayoutItemExploreGedungBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(bind)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(gedungPemilikFilterList[position])
    }

    override fun getItemCount(): Int = gedungPemilikFilterList.size

    inner class ViewHolder(private val bind: LayoutItemExploreGedungBinding) :
            RecyclerView.ViewHolder(bind.root) {
                fun bind(gedung: ListGedungItem) {
                    bind.apply {
                        Log.i("Gedung Data", gedung.toString())
                        tvItemNamaGedung.text = gedung.nama
                        tvItemHargaSewaGedung.text = Converter.formatRupiah(gedung.harga)
                        tvTotalRating.text = gedung.rating

//                        if (!gedung.gambar.isNullOrEmpty()) {
//
//                        } else {
//                        }

                        Glide.with(bind.root)
                            .load("${Const.PHOTO_URL}${gedung.gambar}")
                            .error(R.drawable.dummy_image)
                            .into(ivItemPhotoGedung)

                        itemView.setOnClickListener { onItemItemClickCallback?.onItemClicked(gedung) }
                    }
                }
            }

    fun setOnClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemItemClickCallback = onItemClickCallback
    }



    interface OnItemClickCallback {
        fun onItemClicked(data: ListGedungItem)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val textSearch = constraint.toString()
                if (textSearch.isEmpty()) {
                    gedungPemilikFilterList = listGedung
                } else {
                    var resultList: List<ListGedungItem> = listOf()
                    for (row in listGedung) {
                        if (row.nama!!.toLowerCase(Locale.ROOT)
                            .contains(textSearch.toLowerCase(Locale.ROOT))) {
                            resultList += row

                            Log.i("Gedung", row.toString())
                        }
                    }
                    gedungPemilikFilterList = resultList
                }
                val filterResult = FilterResults()
                filterResult.values = gedungPemilikFilterList
                return filterResult
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                gedungPemilikFilterList = results?.values as List<ListGedungItem>
                notifyDataSetChanged()
            }

            override fun convertResultToString(resultValue: Any?): CharSequence {
                return super.convertResultToString(resultValue)
            }
        }
    }
}