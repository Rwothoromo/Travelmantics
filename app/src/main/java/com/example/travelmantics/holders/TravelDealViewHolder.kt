package com.example.travelmantics.holders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.travelmantics.R
import com.example.travelmantics.models.TravelDeal
import com.squareup.picasso.Picasso

class TravelDealViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private var tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
    private var tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
    private var tvPrice: TextView = itemView.findViewById(R.id.tvPrice)
    private var imageTradeDeal: ImageView = itemView.findViewById(R.id.imageTradeDeal)

    fun bind(travelDeal: TravelDeal) {
        tvTitle.text = travelDeal.title
        tvDescription.text = travelDeal.description
        tvPrice.text = travelDeal.price

        // show image
        travelDeal.imageUrl?.let { showImage(it) }
    }

    private fun showImage(url: String) {
        Picasso.get()
            .load(url)
//            .resize(160, 160)
            .fit()
            .centerCrop()
            .into(imageTradeDeal)
    }
}