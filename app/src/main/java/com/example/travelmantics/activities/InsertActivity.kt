package com.example.travelmantics.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.travelmantics.R
import com.example.travelmantics.models.TravelDeal
import com.example.travelmantics.utils.FirebaseUtil
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_insert.*

class InsertActivity : AppCompatActivity() {

    // entry point for the db
    private var mFirebaseDatabase: FirebaseDatabase? = null

    // location in the db where we read/write data
    private var mDatabaseReference: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insert)

        FirebaseUtil.openFirebaseReference("traveldeals")
        mFirebaseDatabase = FirebaseUtil.mFirebaseDatabase
        mDatabaseReference = FirebaseUtil.mDatabaseReference!!
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.save_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save_menu -> {
                saveDeal()
                Toast.makeText(this, "Deal saved", Toast.LENGTH_LONG).show()

                // reset contents of the edit text fields
                clean()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveDeal() {
        val title = txtTitle.text.toString()
        val description = txtDescription.text.toString()
        val price = txtPrice.text.toString()

        val travelDeal = TravelDeal(title, description, price)

        // insert object to db
        mDatabaseReference?.push()?.setValue(travelDeal)
    }

    private fun clean() {
        txtTitle.text = null
        txtDescription.text = null
        txtPrice.text = null

        // give focus to the title field
        txtTitle.requestFocus()
    }
}
