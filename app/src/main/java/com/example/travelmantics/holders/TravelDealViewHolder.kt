package com.example.travelmantics.holders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.travelmantics.R
import com.example.travelmantics.models.TravelDeal

class TravelDealViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private var tvTitle: TextView = itemView.findViewById(R.id.tvTitle)

    fun bind(travelDeal: TravelDeal) {
        tvTitle.text = travelDeal.title
    }
}