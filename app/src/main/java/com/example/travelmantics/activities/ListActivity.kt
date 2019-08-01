package com.example.travelmantics.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.travelmantics.R
import com.example.travelmantics.adapters.TravelDealAdapter
import kotlinx.android.synthetic.main.activity_list.*

class ListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        val travelDealAdapter = TravelDealAdapter()
        rvTravelDeals.adapter = travelDealAdapter

        // set a LinearLayoutManager on the RecyclerView
        val travelDealsLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rvTravelDeals.layoutManager = travelDealsLayoutManager
    }
}
