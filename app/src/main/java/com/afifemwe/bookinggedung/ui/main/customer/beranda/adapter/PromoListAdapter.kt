package com.afifemwe.bookinggedung.ui.main.customer.beranda.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.afifemwe.bookinggedung.R
import com.afifemwe.bookinggedung.databinding.LayoutItemPromoBinding
import com.bumptech.glide.Glide

class PromoListAdapter(
    private var listPromoPoster: List<Int>,
    private val context: Context
) : RecyclerView.Adapter<PromoListAdapter.ViewHolder>() {

    private var onItemItemClickCallback: OnItemClickCallback? = null

    init {
        Log.i("List Promo", listPromoPoster.toString())

        if (listPromoPoster.isEmpty()) {
            Toast.makeText(context, "Data Not Found", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PromoListAdapter.ViewHolder {
        val bind =
            LayoutItemPromoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(bind)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listPromoPoster[position])
    }

    override fun getItemCount(): Int = listPromoPoster.size

    inner class ViewHolder(private val bind: LayoutItemPromoBinding) :
        RecyclerView.ViewHolder(bind.root) {
        fun bind(gambarPoster: Int) {
            bind.apply {
                Log.i("Promo Data", gambarPoster.toString())

                Glide.with(bind.root)
                    .load(gambarPoster)
                    .error(R.drawable.dummy_image)
                    .into(ivItemPoster)

                itemView.setOnClickListener { onItemItemClickCallback?.onItemClicked(gambarPoster) }
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
