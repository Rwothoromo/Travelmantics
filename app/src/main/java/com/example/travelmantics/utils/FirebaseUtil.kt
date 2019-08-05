package com.example.travelmantics.utils

import android.app.Activity
import android.widget.Toast
import com.example.travelmantics.models.TravelDeal
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*

object FirebaseUtil {

    var mFirebaseDatabase: FirebaseDatabase? = null
    var mDatabaseReference: DatabaseReference? = null
    private var firebaseUtil: FirebaseUtil = FirebaseUtil
    var mTravelDeals: ArrayList<TravelDeal>? = null
    private var mFirebaseAuth: FirebaseAuth? = null
    var mAuthStateListener: FirebaseAuth.AuthStateListener? = null
    private const val RC_SIGN_IN = 793 // request code assigned for starting the new activity, it can be any number

    fun openFirebaseReference(ref: String, callerActivity: Activity) {
        firebaseUtil = FirebaseUtil
        mFirebaseDatabase = FirebaseDatabase.getInstance()
        mFirebaseAuth = FirebaseAuth.getInstance()
        mAuthStateListener = FirebaseAuth.AuthStateListener {
            if (mFirebaseAuth!!.currentUser == null) {
                signIn(callerActivity)
            }
            Toast.makeText(callerActivity.baseContext, "Welcome back!", Toast.LENGTH_LONG).show()
        }

        // called each time the class is called
        // Take note that <TravelDeal> must be there!
        mTravelDeals = ArrayList<TravelDeal>()

        // the path we want to reach
        mDatabaseReference = mFirebaseDatabase!!.reference.child(ref)
    }

    fun attachListener() {
        mAuthStateListener?.let { mFirebaseAuth?.addAuthStateListener(it) }
    }

    fun detachListener() {
        mAuthStateListener?.let { mFirebaseAuth?.removeAuthStateListener(it) }
    }

    fun signIn(callerActivity: Activity) {
        // Choose authentication providers
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )

        // Create and launch sign-in intent
        callerActivity.startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),
            RC_SIGN_IN
        )
    }
}