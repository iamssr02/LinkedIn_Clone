package com.example.linkedin_clone.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.example.linkedin_clone.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class manageNetwork : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_network)
        val cUid = FirebaseAuth.getInstance().currentUser!!.uid
        val followersRef = FirebaseDatabase.getInstance().reference

        findViewById<LinearLayout>(R.id.connectionLayout).setOnClickListener {
            startActivity(Intent(this, connectionActivity::class.java))
        }
        findViewById<LinearLayout>(R.id.followLayout).setOnClickListener {
            Toast.makeText(this, "Not yet implemented", Toast.LENGTH_SHORT).show()
        }
        findViewById<LinearLayout>(R.id.groupsLayout).setOnClickListener {
            Toast.makeText(this, "Not yet implemented",Toast.LENGTH_SHORT).show()
        }
        findViewById<LinearLayout>(R.id.pagesLayout).setOnClickListener {
            Toast.makeText(this, "Not yet implemented",Toast.LENGTH_SHORT).show()
        }
        findViewById<LinearLayout>(R.id.eventsLayout).setOnClickListener {
            Toast.makeText(this, "Not yet implemented",Toast.LENGTH_SHORT).show()
        }

        followersRef.child("Follow").child(cUid)
            .child("Connections").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    findViewById<TextView>(R.id.connectionsNumber)?.text = snapshot.childrenCount.toString()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        followersRef.child("Follow").child(cUid)
            .child("Following").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        findViewById<TextView>(R.id.followNumber)?.text = snapshot.childrenCount.toString()
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }
}