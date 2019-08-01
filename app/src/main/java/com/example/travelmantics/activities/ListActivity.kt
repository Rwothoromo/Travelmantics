package com.example.travelmantics.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.travelmantics.R
import com.example.travelmantics.models.TravelDeal
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_list.*

class ListActivity : AppCompatActivity() {

    var travelDeals = ArrayList<TravelDeal>()

    private var mFirebaseDatabase: FirebaseDatabase? = null
    private lateinit var mDatabaseReference: DatabaseReference
    private var mChildEventListener: ChildEventListener? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        mFirebaseDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mFirebaseDatabase!!.reference.child("traveldeals")

        mChildEventListener = object : ChildEventListener {

            // the DataSnapshot is an immutable copy of data from a Firebase database location

            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
//                Log.d("onChildAdded", "onChildAdded:" + dataSnapshot.key!!)

                // serialize the data using the getValue and put it in the TravelDeal class
                val travelDeal = dataSnapshot.getValue(TravelDeal::class.java)

                // A new travel deal has been added, add it to the displayed list
                textViewDeals.text = applicationContext.getString(
                    R.string.on_child_added,
                    textViewDeals.text.toString(),
                    travelDeal!!.title
                )
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
//                Log.d("onChildChanged", "onChildChanged: ${dataSnapshot.key}")

                // A travel deal has changed, use the key to determine if we are displaying this
                // travel deal and if so displayed the changed travel deal.
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
//                Log.d("onChildRemoved", "onChildRemoved:" + dataSnapshot.key!!)

                // A travel deal has changed, use the key to determine if we are displaying this
                // travel deal and if so remove it.
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {
//                Log.d("onChildMoved", "onChildMoved:" + dataSnapshot.key!!)

                // A travel deal has changed position, use the key to determine if we are
                // displaying this travel deal and if so move it.
            }

            override fun onCancelled(databaseError: DatabaseError) {
//                Log.w("onCancelled", "onCancelled", databaseError.toException())
                Toast.makeText(applicationContext, "Failed to load travel deals.", Toast.LENGTH_SHORT).show()
            }
        }

        mDatabaseReference.addChildEventListener(mChildEventListener as ChildEventListener)
    }
}
