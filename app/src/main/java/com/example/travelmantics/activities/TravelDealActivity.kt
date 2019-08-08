package com.example.travelmantics.activities

import android.app.Activity
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.travelmantics.R
import com.example.travelmantics.constants.Constants.PICTURE_RESULT
import com.example.travelmantics.models.TravelDeal
import com.example.travelmantics.utils.FirebaseUtil
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_deal.*


class TravelDealActivity : AppCompatActivity() {

    // entry point for the db
    private var mFirebaseDatabase: FirebaseDatabase? = null

    // location in the db where we read/write data
    private var mDatabaseReference: DatabaseReference? = null

    private var travelDeal: TravelDeal = TravelDeal()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deal)

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

        // show image
        travelDeal.imageUrl?.let { showImage(it) }

        btnImage.setOnClickListener {
            intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/jpg"
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
            startActivityForResult(
                Intent.createChooser(intent, "Insert Picture"),
                PICTURE_RESULT
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICTURE_RESULT && resultCode == Activity.RESULT_OK) {
            // locate the image file to be uploaded and put the location in a Uri object
            val imageUri = data!!.data!!

            // get the reference to the storage, where the data should be uploaded
            val storageReference = FirebaseUtil.mStorageReference!!.child(imageUri.lastPathSegment!!)

            // putFile will return an asynchronous upload task task
            // where we can listen to the result; success or failure
            val uploadTask = storageReference.putFile(imageUri)

            val urlTask = uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                val pictureName = task.result?.storage?.path
                travelDeal.imageName = pictureName

                return@Continuation storageReference.downloadUrl
            })
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val downloadUri = task.result
                        val url = downloadUri.toString()
                        travelDeal.imageUrl = url
                        showImage(url)
                    } else {
                        // Handle failures
                        // ...
                    }
                }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        if (FirebaseUtil.isAdmin) {
            menu?.findItem(R.id.delete_menu)?.isVisible = true
            menu?.findItem(R.id.save_menu)?.isVisible = true
            enableEditTexts(true)
            menu?.findItem(R.id.btnImage)?.isVisible = true
        } else {
            menu?.findItem(R.id.delete_menu)?.isVisible = false
            menu?.findItem(R.id.save_menu)?.isVisible = false
            enableEditTexts(false)
            menu?.findItem(R.id.btnImage)?.isVisible = false
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
        travelDeal.imageUrl?.let { Log.d("url", it) }

        if (travelDeal.id == null) {
            // insert object to db
            mDatabaseReference?.push()?.setValue(travelDeal)
        } else {
            // edit the db value with the matching id
            mDatabaseReference?.child(travelDeal.id.toString())?.setValue(travelDeal)
        }
    }

    private fun deleteDeal() {

        // delete image from firebase storage
        if (travelDeal.imageName != null) {
            FirebaseUtil.mFirebaseStorage!!.reference.child(travelDeal.imageName!!).delete()
        }

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

    private fun showImage(url: String) {
        val width = Resources.getSystem().displayMetrics.widthPixels

        Picasso.get()
            .load(url)
            .resize(width, width * 2 / 3)
            .centerCrop()
            .into(image)
    }
}
