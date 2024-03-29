package com.example.travelmantics.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.travelmantics.R
import com.example.travelmantics.activities.TravelDealActivity
import com.example.travelmantics.holders.TravelDealViewHolder
import com.example.travelmantics.models.TravelDeal
import com.example.travelmantics.utils.FirebaseUtil
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference


class TravelDealAdapter : RecyclerView.Adapter<TravelDealViewHolder>() {

    var travelDeals = ArrayList<TravelDeal>()

    private var mDatabaseReference: DatabaseReference = FirebaseUtil.mDatabaseReference!!
    private var mChildEventListener: ChildEventListener

    init {
        travelDeals = FirebaseUtil.mTravelDeals!!

        mChildEventListener = object : ChildEventListener {

            // the DataSnapshot is an immutable copy of data from a Firebase database location

            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {

                // serialize the data using the getValue and put it in the TravelDeal class
                val travelDeal = dataSnapshot.getValue(TravelDeal::class.java)

                // set the snapshot key
                travelDeal!!.id = dataSnapshot.key

                // update the deals array
                travelDeals.add(travelDeal)

                // alert the observers so that UI is updated
                notifyItemInserted(travelDeals.size - 1)
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
                //
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                //
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {
                //
            }

            override fun onCancelled(databaseError: DatabaseError) {
                //
            }
        }

        mDatabaseReference.addChildEventListener(mChildEventListener)
    }

    /**
     * Called when the RecyclerView needs a new ViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TravelDealViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.rv_row, parent, false)
        return TravelDealViewHolder(itemView)
    }

    /**
     * Called to display the data
     */
    override fun onBindViewHolder(holder: TravelDealViewHolder, position: Int) {
        // get the travel deal at current position and bind to the holder
        val travelDeal = travelDeals[position]
        holder.bind(travelDeal)

        holder.itemView.setOnClickListener { view ->
            val intent = Intent(view!!.context, TravelDealActivity::class.java)
            intent.putExtra("Deal", travelDeal)
            view.context.startActivity(intent)
        }
    }

    /**
     * Get the travel deals size/count
     */
    override fun getItemCount() = travelDeals.size
}