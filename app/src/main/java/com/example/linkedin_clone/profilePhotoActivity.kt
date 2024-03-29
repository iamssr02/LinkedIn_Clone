package com.example.linkedin_clone

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.storage.FirebaseStorage
import com.yalantis.ucrop.UCrop
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.File
import java.util.*

class profilePhotoActivity : AppCompatActivity() {
    private lateinit var mProgressBar: ProgressBar
    private var flag: String = ""
    private var postFlag: String = "0"
    private var finalUri: Uri? = null
    private val REQUEST_GALLERY = 1001
    private val getContent = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val requestCode = result.data?.extras?.getInt(REQUEST_GALLERY.toString())
        Log.d("tag", "RequestCode : $requestCode")
        if (result.resultCode == Activity.RESULT_OK) {
            val intent: Intent? = result.data
            if (intent != null) {
                intent.data?.let { launchImageCrop(it) }
            }
        }
    }
    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent: Intent? = result.data
                Log.d("tag", "Intent: $intent")
                if (intent != null) {
                    postFlag = "1"
                    finalUri = UCrop.getOutput(intent)!!
                    Log.d("tag", "Intent finalUri : $finalUri")
                    Glide.with(applicationContext)
                        .load(finalUri)
                        .into(findViewById(R.id.profile_img))
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_photo)

        if (intent.hasExtra("FLAG")) {
            this.flag = intent.getStringExtra("FLAG").toString()
        }
        if (intent.hasExtra("postFlag")) {
            this.postFlag = intent.getStringExtra("postFlag").toString()
        }

        mProgressBar = findViewById(R.id.phoneProgressBar)
        mProgressBar.visibility = View.INVISIBLE
        FirebaseDatabase.getInstance().getReference("Users").
        child(FirebaseAuth.getInstance().currentUser!!.uid).get().addOnSuccessListener {
            val imageURL = it.child("profileImageURL").value.toString()
            Glide.with(applicationContext).load(imageURL).into(findViewById(R.id.profile_img))
        }
        findViewById<ImageView>(R.id.profile_img).setOnClickListener {
            if (checkSelfPermission()) {
                pickImageFromGallery()
            } else {
                Toast.makeText(applicationContext, "Allow all permissions", Toast.LENGTH_SHORT)
                    .show()
                requestPermission()
            }
        }

        findViewById<TextView>(R.id.btn_post).setOnClickListener {
            if (postFlag=="0"){
                Toast.makeText(this,"Please pick a picture first",Toast.LENGTH_SHORT).show()
            }
            else{
                mProgressBar.visibility = View.VISIBLE
                savePostDetailsToDatabase()
            }
        }

        findViewById<TextView>(R.id.btn_skip).setOnClickListener {
            if(flag == ""){
                val intent = Intent(this, coverPhotoActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
            }
            else{
                finish()
            }
        }
    }

    private fun savePostDetailsToDatabase() {
        var imageURL: String? = null
        val uploadedBy = FirebaseAuth.getInstance().currentUser!!.uid
        val databaseRef = FirebaseDatabase.getInstance()
            .getReference("Users")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
        val newPostID = databaseRef.push().key
        CoroutineScope(Dispatchers.IO).launch {
            imageURL = getDownloadImageURL(finalUri, newPostID!!, uploadedBy)
            databaseRef.child("profileImageURL").setValue(imageURL).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // handle successful after events
                    mProgressBar.visibility = View.INVISIBLE
                    if(flag == "") {
                        val intent = Intent(this@profilePhotoActivity, coverPhotoActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                        startActivity(intent)
                    }
                    else{
                        val intent = Intent(this@profilePhotoActivity, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                        startActivity(intent)
                    }
                } else {
                    Log.d("TAG", "savePostDetailsToDatabase: ${task.exception}")
                }
            }
        }
    }


    private suspend fun getDownloadImageURL(
        imageUri: Uri?,
        postID: String,
        userID: String
    ): String? {
        var downloadURL: String? = null
        if (imageUri != null) {
            val filePath = FirebaseStorage.getInstance().reference
                .child("ProfileImages")
                .child("user_$userID")
                .child("img_$postID.jpg")
            filePath.putFile(imageUri).addOnProgressListener {

            }.addOnPausedListener {
                Log.d("tag", "Upload is paused ${it.error?.message}")
            }.await()
            downloadURL = filePath.downloadUrl.await().toString()
        }
        Log.d("TAG", "getDownloadImageURL: $downloadURL")
        return downloadURL
    }

    private fun requestPermission() {
        Log.d("tag", "requestPermission: ")
        requestPermissions(
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            ), 100
        )
    }

    private fun checkSelfPermission(): Boolean {
        Log.d("tag", "onCreateView: checkSelfPermission ")
        return ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun pickImageFromGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        getContent.launch(intent)
        Log.d("tag", "pickImageFromGallery: ")
    }

    private fun launchImageCrop(imageUri: Uri) {
        Log.d("tag", "launchImageCrop: ")
        var destinationFileName: String = StringBuilder(UUID.randomUUID().toString()).toString()
        destinationFileName += ".jpg"
        val uCrop = UCrop.of(imageUri, Uri.fromFile(File(cacheDir, destinationFileName)))
        uCrop.withAspectRatio(10F, 10F);
        getCropOptions().let { uCrop.withOptions(it) }
        val uCropIntent = uCrop.getIntent(applicationContext)
        resultLauncher.launch(uCropIntent)
    }

    private fun getCropOptions(): UCrop.Options {
        val options = UCrop.Options()
        options.setCompressionQuality(100)

        //CompressingType
        options.setCompressionFormat(Bitmap.CompressFormat.PNG)
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG)

        //UI
        options.setHideBottomControls(false)
        options.setFreeStyleCropEnabled(false)

        options.setToolbarTitle("Crop Image")
        Log.d("tag", "getCropOptions: ")
        return options
    }
}