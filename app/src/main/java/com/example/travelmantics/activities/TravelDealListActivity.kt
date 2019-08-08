package com.example.travelmantics.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.travelmantics.R
import com.example.travelmantics.adapters.TravelDealAdapter
import com.example.travelmantics.utils.FirebaseUtil
import com.firebase.ui.auth.AuthUI
import kotlinx.android.synthetic.main.activity_list.*

class TravelDealListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.list_activity_menu, menu)

        menu?.findItem(R.id.insert_menu)?.isVisible = FirebaseUtil.isAdmin

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.insert_menu -> {
                intent = Intent(this, TravelDealActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.logout_menu -> {
                AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener {
                        FirebaseUtil.attachListener()
                    }
                FirebaseUtil.detachListener()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPause() {
        super.onPause()
        FirebaseUtil.detachListener()
    }

    override fun onResume() {
        super.onResume()

        FirebaseUtil.openFirebaseReference("traveldeals", this)

        val travelDealAdapter = TravelDealAdapter()
        rvTravelDeals.adapter = travelDealAdapter

        // set a LinearLayoutManager on the RecyclerView
        val travelDealsLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rvTravelDeals.layoutManager = travelDealsLayoutManager

        FirebaseUtil.attachListener()
    }

    fun showMenu() {
        // tell android that menu contents have changed so it redraws the menu
        invalidateOptionsMenu()
    }
}
