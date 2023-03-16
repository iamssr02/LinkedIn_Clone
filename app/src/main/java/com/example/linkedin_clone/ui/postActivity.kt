package com.example.linkedin_clone.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.linkedin_clone.DataClasses.imageUsers
import com.example.linkedin_clone.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class postActivity : AppCompatActivity() {

    private lateinit var postId: String
    private lateinit var dbref: DatabaseReference
    private lateinit var dbref2: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)
        val pref = this.getSharedPreferences("PREFS", Context.MODE_PRIVATE)
        if (pref != null) {
            this.postId = pref.getString("postId", "none").toString()
        }

        dbref = FirebaseDatabase.getInstance().getReference("Images")
        dbref2 = FirebaseDatabase.getInstance().getReference("Users")

        CoroutineScope(Dispatchers.Main).launch {
            getUserData()
            checkLikeStatus()
        }

    }

    private suspend fun getUserData() {
        val uploadedBy = dbref.child(postId).child("uploadedBy").get().await().value.toString()
        val timeStamp = dbref.child(postId).child("timeStamp").get().await().value.toString()
        val caption = dbref.child(postId).child("caption").get().await().value.toString()
        val imageURL = dbref.child(postId).child("imageURL").get().await().value.toString()
        val name = dbref2.child(uploadedBy).child("name").get().await().value.toString()
        val headline = dbref2.child(uploadedBy).child("headline").get().await().value.toString()
        val profileImage = dbref2.child(uploadedBy).child("profileImageURL").get().await().value.toString()
        findViewById<TextView>(R.id.username).text = name
        findViewById<TextView>(R.id.user_headline).text = headline
        findViewById<TextView>(R.id.time_stamp).text = timeStamp
        findViewById<TextView>(R.id.caption).text = caption
        Glide.with(applicationContext).load(imageURL).into(findViewById(R.id.post_img))
        Glide.with(applicationContext).load(profileImage).into(findViewById(R.id.profileImage))
    }

    private fun checkLikeStatus() {
        val currentUid = FirebaseAuth.getInstance().currentUser!!.uid
        dbref = FirebaseDatabase.getInstance().getReference("Images").child(postId).child("likes")
        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    findViewById<RelativeLayout>(R.id.ll2).visibility = View.VISIBLE
                    findViewById<TextView>(R.id.likesTxt).text = snapshot.childrenCount.toString()
                }
                if (snapshot.child(currentUid).exists()) {
                    findViewById<ImageView>(R.id.ic_like)
                        .setImageResource(R.drawable.ic_liked)
                    findViewById<TextView>(R.id.like_clr).text = "Liked"
                } else {
                    findViewById<ImageView>(R.id.ic_like)
                        .setImageResource(R.drawable.ic_like)
                    findViewById<TextView>(R.id.like_clr).text = "Like"
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}