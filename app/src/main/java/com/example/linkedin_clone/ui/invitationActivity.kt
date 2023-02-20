package com.example.linkedin_clone.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.linkedin_clone.Adapter.NetworkAdapter
import com.example.linkedin_clone.DataClasses.ConnectionRequestUser
import com.example.linkedin_clone.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class invitationActivity : AppCompatActivity() {
    private lateinit var dbref : DatabaseReference
    private lateinit var userRecyclerview : RecyclerView
    private lateinit var networkAdapter: NetworkAdapter
    private lateinit var userArrayList : ArrayList<ConnectionRequestUser>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invitation)
        userRecyclerview = findViewById(R.id.connectionRecyclerView)
        userRecyclerview.layoutManager = LinearLayoutManager(this)
        userRecyclerview.setHasFixedSize(true)

        userArrayList = arrayListOf<ConnectionRequestUser>()
        getUserData()
    }
    private fun getUserData() {
        val currentUid = FirebaseAuth.getInstance().currentUser!!.uid
        dbref = FirebaseDatabase.getInstance().getReference("Follow").child(currentUid).child("Requests")

        dbref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()){
                    for(userSnapshot in snapshot.children) {
                        val user = ConnectionRequestUser(userSnapshot.key.toString(),
                            userSnapshot.value.toString())
                        userArrayList.add(user)
                    }

                    networkAdapter = NetworkAdapter()
                    userRecyclerview.adapter = networkAdapter
                    networkAdapter.submitList(userArrayList)

                }

            }

            override fun onCancelled(error: DatabaseError) {
            }


        })

    }
}