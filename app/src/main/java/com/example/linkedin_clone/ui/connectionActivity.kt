package com.example.linkedin_clone.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.linkedin_clone.Adapter.ConnectionAdapter
import com.example.linkedin_clone.DataClasses.ConnectionRequestUser
import com.example.linkedin_clone.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class connectionActivity : AppCompatActivity() {
    private lateinit var dbref: DatabaseReference
    private lateinit var userRecyclerview: RecyclerView
    private lateinit var networkAdapter: ConnectionAdapter
    private lateinit var userArrayList: ArrayList<ConnectionRequestUser>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_connection)
        userRecyclerview = findViewById(R.id.connectionRecyclerView)
        userRecyclerview.layoutManager = LinearLayoutManager(this)
        userRecyclerview.setHasFixedSize(true)

        userArrayList = arrayListOf<ConnectionRequestUser>()

        findViewById<ImageView>(R.id.btn_back).setOnClickListener {
            finish()
        }

        val followersRef = FirebaseDatabase.getInstance().reference
        val cUid = FirebaseAuth.getInstance().currentUser!!.uid
        followersRef.child("Follow").child(cUid)
            .child("Connections").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        findViewById<TextView>(R.id.textView)?.text =
                            "${snapshot.childrenCount.toString()} connections"
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })


        getUserData()
    }

    private fun getUserData() {
        val currentUid = FirebaseAuth.getInstance().currentUser!!.uid
        dbref = FirebaseDatabase.getInstance().getReference("Follow").child(currentUid).child("Connections")

        dbref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                CoroutineScope(IO).launch {

                    if (snapshot.exists()) {
                        for (userSnapshot in snapshot.children) {

                            val database = FirebaseDatabase.getInstance().getReference("Users")
                                .child(userSnapshot.key.toString())
                            val user = ConnectionRequestUser(
                                userSnapshot.key.toString(),
                                database.child("name").get().await().value.toString(),
                                database.child("headline").get().await().value.toString(),
                                database.child("profileImageURL").get().await().value.toString(),
                                database.child("coverImageURL").get().await().value.toString()
                            )
                            Log.d("TAG", "Value: $user")
                            userArrayList.add(user)

                        }


                        networkAdapter = ConnectionAdapter()
                        userRecyclerview.adapter = networkAdapter
                        Log.d("TAG", "onDataChange: $userArrayList")
                        networkAdapter.submitList(userArrayList)

                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }


        })
    }
}

