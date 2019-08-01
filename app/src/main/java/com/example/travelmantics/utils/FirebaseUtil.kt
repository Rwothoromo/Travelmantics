package com.example.travelmantics.utils

import com.example.travelmantics.models.TravelDeal
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*

object FirebaseUtil {

    var mFirebaseDatabase: FirebaseDatabase? = null
    var mDatabaseReference: DatabaseReference? = null
    private var firebaseUtil: FirebaseUtil? = null
    var mTravelDeals: ArrayList<TravelDeal>? = null

    fun openFirebaseReference(ref: String) {
        if (firebaseUtil == null) {
            firebaseUtil = FirebaseUtil
            mFirebaseDatabase = FirebaseDatabase.getInstance()
        }

        mTravelDeals = ArrayList()

        // the path we want to reach
        mDatabaseReference = mFirebaseDatabase!!.reference.child(ref)
    }
}