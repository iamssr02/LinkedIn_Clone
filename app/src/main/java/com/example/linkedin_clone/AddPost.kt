package com.example.linkedin_clone

import android.Manifest.permission.CAMERA
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.storage.FirebaseStorage
import com.yalantis.ucrop.UCrop
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.File
import java.util.*

class AddPost : AppCompatActivity() {

    private var finalUri: Uri? = null
    private val REQUEST_GALLERY = 1001
    private val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        val requestCode = result.data?.extras?.getInt(REQUEST_GALLERY.toString())
        Log.d("tag", "RequestCode : $requestCode")
        if (result.resultCode == Activity.RESULT_OK) {
            val intent: Intent? = result.data
            if (intent != null) {
                intent.data?.let { launchImageCrop(it) }
            }
        }
    }
    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intent: Intent? = result.data
            Log.d("tag", "Intent: $intent")
            if (intent != null) {
                finalUri = UCrop.getOutput(intent)!!
                Log.d("tag", "Intent finalUri : $finalUri")
                Glide.with(applicationContext)
                    .load(finalUri)
                    .into(findViewById(R.id.post_img))
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post)
        findViewById<ImageView>(R.id.img1).setOnClickListener{
            if (checkSelfPermission()){
                pickImageFromGallery()
            }
            else {
                Toast.makeText(applicationContext, "Allow all permissions", Toast.LENGTH_SHORT).show()
                requestPermission()
            }
        }
        findViewById<TextView>(R.id.btn_post).setOnClickListener {
            savePostDetailsToDatabase()
        }
    }

    private fun savePostDetailsToDatabase() {
        var imageURL: String? = null
        val caption = findViewById<EditText>(R.id.caption).text.toString()
        val uploadedBy = FirebaseAuth.getInstance().currentUser!!.uid
        val databaseRef = FirebaseDatabase.getInstance()
            .reference
            .child("Post")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
        val newPostID = databaseRef.push().key
        CoroutineScope(IO).launch {
            imageURL = getDownloadImageURL(finalUri, newPostID!!, uploadedBy)
            val postMap: HashMap<String, Any?> = hashMapOf(
                "id" to newPostID,
                "caption" to caption,
                "imageURL" to imageURL,
                "uploadedBy" to uploadedBy,
                "timeStamp" to ServerValue.TIMESTAMP,
            )
            Log.d("TAG", "savePostDetailsToDatabase: $postMap")
            databaseRef.child(newPostID).setValue(postMap).addOnCompleteListener {task ->
                if (task.isSuccessful){
                    // handle successful after events
                    finish()
                }
                else {
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
        if (imageUri != null){
            val filePath = FirebaseStorage.getInstance().reference
                .child("Posts")
                .child("user_$userID")
                .child("post_$postID")
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
        requestPermissions(arrayOf(READ_EXTERNAL_STORAGE, CAMERA), 100)
    }
    private fun checkSelfPermission(): Boolean {
        Log.d("tag", "onCreateView: checkSelfPermission ")
        return ContextCompat.checkSelfPermission(
            applicationContext,
            READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
            applicationContext,
            CAMERA
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
        uCrop.withAspectRatio(25F, 10F);
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
        options.setFreeStyleCropEnabled(true)

        //Colors
//        options.setStatusBarColor(resources.getColor(R.color.md_theme_background))
//        options.setToolbarColor(resources.getColor(R.color.md_theme_surface))
//        options.setToolbarWidgetColor(resources.getColor(R.color.md_theme_primary))
        options.setToolbarTitle("Crop Image")
        Log.d("tag", "getCropOptions: ")
        return options
    }
}