package com.example.travelmantics.activities

import android.content.Intent
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

class TravelDealActivity : AppCompatActivity() {

    // entry point for the db
    private var mFirebaseDatabase: FirebaseDatabase? = null

    // location in the db where we read/write data
    private var mDatabaseReference: DatabaseReference? = null

    private var travelDeal: TravelDeal = TravelDeal()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insert)

        mFirebaseDatabase = FirebaseUtil.mFirebaseDatabase
        mDatabaseReference = FirebaseUtil.mDatabaseReference

        val deal = intent.getSerializableExtra("Deal")
        if (deal != null) {
            travelDeal = deal as TravelDeal
        }

        // for EditText fields/views, use setText
        txtTitle.setText(travelDeal.title)
        txtDescription.setText(travelDeal.description)
        txtPrice.setText(travelDeal.price)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.save_menu, menu)

        if (FirebaseUtil.isAdmin) {
            menu?.findItem(R.id.delete_menu)?.isVisible = true
            menu?.findItem(R.id.save_menu)?.isVisible = true
            enableEditTexts(true)
        } else {
            menu?.findItem(R.id.delete_menu)?.isVisible = false
            menu?.findItem(R.id.save_menu)?.isVisible = false
            enableEditTexts(false)
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save_menu -> {
                saveDeal()
                Toast.makeText(this, "Deal saved", Toast.LENGTH_LONG).show()

                // reset contents of the edit text fields
                clean()

                // go back to the list
                backToList()
                return true
            }

            R.id.delete_menu -> {
                deleteDeal()
                Toast.makeText(this, "Deal deleted!", Toast.LENGTH_LONG).show()

                // go back to the list
                backToList()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveDeal() {

        travelDeal.title = txtTitle.text.toString()
        travelDeal.description = txtDescription.text.toString()
        travelDeal.price = txtPrice.text.toString()

        if (travelDeal.id == null) {
            // insert object to db
            mDatabaseReference?.push()?.setValue(travelDeal)
        } else {
            // edit the db value with the matching id
            mDatabaseReference?.child(travelDeal.id.toString())?.setValue(travelDeal)
        }
    }

    private fun deleteDeal() {

        // delete the db value with the matching id
        mDatabaseReference?.child(travelDeal.id.toString())?.removeValue()
    }

    private fun clean() {
        txtTitle.text = null
        txtDescription.text = null
        txtPrice.text = null

        // give focus to the title field
        txtTitle.requestFocus()
    }

    private fun backToList() {
        intent = Intent(this, TravelDealListActivity::class.java)
        startActivity(intent)
    }

    private fun enableEditTexts(isEnabled: Boolean) {
        txtTitle.isEnabled = isEnabled
        txtDescription.isEnabled = isEnabled
        txtPrice.isEnabled = isEnabled
    }
}
