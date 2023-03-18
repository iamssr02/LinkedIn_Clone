package com.example.linkedin_clone.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.linkedin_clone.Adapter.ImageAdapter
import com.example.linkedin_clone.DataClasses.imageUsers
import com.example.linkedin_clone.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class myPostsActivity : AppCompatActivity() {
    private lateinit var dbref: DatabaseReference
    private lateinit var dbref2: DatabaseReference
    private lateinit var userRecyclerview: RecyclerView
    private lateinit var imageAdapter: ImageAdapter
    private lateinit var userArrayList: ArrayList<imageUsers>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_posts)
        userRecyclerview = findViewById(R.id.posts)
        userArrayList = arrayListOf<imageUsers>()
        imageAdapter = ImageAdapter()
        userRecyclerview.layoutManager = LinearLayoutManager(applicationContext)
        userRecyclerview.setHasFixedSize(true)
        userRecyclerview.adapter = imageAdapter
        var profileId = FirebaseAuth.getInstance().currentUser!!.uid

        if (intent.hasExtra("ID")) {
            profileId = intent.getStringExtra("ID").toString()
        }

        getUserData(profileId)

        findViewById<ImageView>(R.id.btn_back)?.setOnClickListener {
            finish()
        }

    }

    private fun getUserData(profileId: String) {
        val currentUid = FirebaseAuth.getInstance().currentUser!!.uid
        dbref = FirebaseDatabase.getInstance().getReference("Post").child(profileId)
        dbref2 = FirebaseDatabase.getInstance().getReference("Users")
        dbref.addValueEventListener(object : ValueEventListener {


            override fun onDataChange(snapshot: DataSnapshot) {
                CoroutineScope(Dispatchers.IO).launch {
                    var name: String = ""
                    var headline: String = ""
                    var profileImage: String = ""

                    if (snapshot.exists()) {
                        for (userSnapshot in snapshot.children) {
                            val uploadedBy = userSnapshot.child("uploadedBy").value.toString()

                            name = dbref2.child(uploadedBy).child("name").get()
                                .await().value.toString()
                            headline = dbref2.child(uploadedBy).child("headline").get()
                                .await().value.toString()
                            profileImage = dbref2.child(uploadedBy).child("profileImageURL").get()
                                .await().value.toString()


                            Log.d("TAG", "NAMECheck2: $name")

                            val user = imageUsers(
//                            userSnapshot.child("likes").childrenCount.toInt(),
                                userSnapshot.child("id").value.toString(),
                                userSnapshot.child("caption").value.toString(),
                                userSnapshot.child("imageURL").value.toString(),
                                profileImage,
                                userSnapshot.child("uploadedBy").value.toString(),
                                name,
                                headline,
                                userSnapshot.child("timeStamp").value.toString(),
                            )
                            userArrayList.add(user)

                        }
                        Log.d("TAG", "image: $userArrayList")
                        imageAdapter.submitList(userArrayList)

                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}