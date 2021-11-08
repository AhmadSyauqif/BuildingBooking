package com.afifemwe.bookinggedung.ui.detailgedung.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.afifemwe.bookinggedung.R
import com.afifemwe.bookinggedung.databinding.LayoutItemFasilitasBinding
import com.afifemwe.bookinggedung.model.Fasilitas
import com.afifemwe.bookinggedung.ui.main.pemilik.beranda.response.GetGedungListResponse
import com.bumptech.glide.Glide

class FasilitasListAdapter(
    private var listFasilitasFasilitas: List<Fasilitas>,
    context: Context
) : RecyclerView.Adapter<FasilitasListAdapter.ViewHolder>() {

    private var onItemItemClickCallback: OnItemClickCallback? = null

    init {
        Log.i("List Fasilitas", listFasilitasFasilitas.toString())

        if (listFasilitasFasilitas.isEmpty()) {
            Toast.makeText(context, "Data Not Found", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FasilitasListAdapter.ViewHolder {
        val bind =
            LayoutItemFasilitasBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(bind)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listFasilitasFasilitas[position])
    }

    override fun getItemCount(): Int = listFasilitasFasilitas.size

    inner class ViewHolder(private val bind: LayoutItemFasilitasBinding) :
        RecyclerView.ViewHolder(bind.root) {
        fun bind(fasilitas: Fasilitas) {
            bind.apply {
                Log.i("Fasilitas Data", fasilitas.toString())

                Glide.with(bind.root)
                    .load(fasilitas.icon)
                    .error(R.drawable.dummy_image)
                    .into(ivItemFasilitas)

                tvItemNamaFasilitas.text = fasilitas.nama

            }
        }
    }

    fun setOnClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemItemClickCallback = onItemClickCallback
    }


    interface OnItemClickCallback {
        fun onItemClicked(data: Int)
    }
}
