package com.example.travelmantics.utils

import android.util.Log
import android.widget.Toast
import com.example.travelmantics.activities.TravelDealListActivity
import com.example.travelmantics.constants.Constants.RC_SIGN_IN
import com.example.travelmantics.models.TravelDeal
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*

object FirebaseUtil {

    var mFirebaseDatabase: FirebaseDatabase? = null
    var mDatabaseReference: DatabaseReference? = null
    private var firebaseUtil: FirebaseUtil = FirebaseUtil
    var mTravelDeals: ArrayList<TravelDeal>? = null
    private var mFirebaseAuth: FirebaseAuth? = null
    var mFirebaseStorage: FirebaseStorage? = null
    var mStorageReference: StorageReference? = null

    private var mAuthStateListener: FirebaseAuth.AuthStateListener? = null
    var isAdmin: Boolean = false
    var userId: String? = null
    private var caller = TravelDealListActivity()

    fun openFirebaseReference(ref: String, callerActivity: TravelDealListActivity) {
        firebaseUtil = FirebaseUtil
        mFirebaseDatabase = FirebaseDatabase.getInstance()
        mFirebaseAuth = FirebaseAuth.getInstance()

        caller = callerActivity

        mAuthStateListener = FirebaseAuth.AuthStateListener {
            if (mFirebaseAuth!!.currentUser == null) {
                signIn()
            } else {
                userId = mFirebaseAuth!!.uid
                checkAdmin(userId)
            }
            Toast.makeText(callerActivity.baseContext, "Welcome back!", Toast.LENGTH_LONG).show()
        }

        // grant access to upload files to Firebase
        connectStorage()

        // called each time the class is called
        // Take note that <TravelDeal> must be there!
        mTravelDeals = ArrayList<TravelDeal>()

        // the path we want to reach
        mDatabaseReference = mFirebaseDatabase!!.reference.child(ref)
    }

    private fun checkAdmin(uid: String?) {
        val databaseReference = uid?.let { mFirebaseDatabase?.reference?.child("administrators")?.child(it) }

        val childEventListener = object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.d("databaseError", p0.toString())
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                //
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                //
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                isAdmin = true
                caller.showMenu()
            }

            override fun onChildRemoved(p0: DataSnapshot) {
                //
            }
        }

        // set the listener to the db reference
        databaseReference?.addChildEventListener(childEventListener)
    }

    fun attachListener() {
        mAuthStateListener?.let { mFirebaseAuth?.addAuthStateListener(it) }
    }

    fun detachListener() {
        mAuthStateListener?.let { mFirebaseAuth?.removeAuthStateListener(it) }
    }

    private fun signIn() {
        // Choose authentication providers
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )

        // Create and launch sign-in intent
        caller.startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),
            RC_SIGN_IN
        )
    }

    private fun connectStorage() {
        mFirebaseStorage = FirebaseStorage.getInstance()
        mStorageReference = mFirebaseStorage!!.reference.child("deals_pictures")
    }
}
