package com.example.linkedin_clone.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.bumptech.glide.Glide
import com.example.linkedin_clone.DataClasses.User
import com.example.linkedin_clone.R
import com.example.linkedin_clone.coverPhotoActivity
import com.example.linkedin_clone.profilePhotoActivity
import com.example.linkedin_clone.searchActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import org.w3c.dom.Text

class Profile : AppCompatActivity() {
    private lateinit var profileId: String
    private lateinit var firebaseUser: FirebaseUser
    private lateinit var pname: String
    private lateinit var cname: String

    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        findViewById<TextView>(R.id.item_search_input)?.setOnClickListener {
            val intent = Intent(this, searchActivity::class.java)
            startActivity(intent)
        }

        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        val pref = this.getSharedPreferences("PREFS", Context.MODE_PRIVATE)
        if (pref != null) {
            this.profileId = pref.getString("profileId", "none").toString()
        }
        FirebaseDatabase.getInstance().getReference("Users").child(profileId).get().addOnSuccessListener {
            this.pname = it.child("name").value.toString()
        }
        FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.uid).get().addOnSuccessListener {
            this.cname = it.child("name").value.toString()
        }
        if (profileId == firebaseUser.uid) {
            findViewById<TextView>(R.id.connectBtn).text = "Open to"
            findViewById<TextView>(R.id.messageBtn).text = "Add section"
        } else if (profileId != firebaseUser.uid) {
            checkConnection()
            findViewById<TextView>(R.id.messageBtn).text = "Message"
        }


            getConnections()
            userInfo()

        findViewById<ImageView>(R.id.profileImg).setOnClickListener{
            startActivity(Intent(this, profilePhotoActivity::class.java))
        }

        findViewById<ImageView>(R.id.background_img).setOnClickListener{
            startActivity(Intent(this, coverPhotoActivity::class.java))
        }

        findViewById<CardView>(R.id.Btn1).setOnClickListener {
            val getButtonText = findViewById<TextView>(R.id.connectBtn).text.toString()
            when {
                getButtonText == "Connect" -> {
                    firebaseUser.uid.let { it1 ->
                        FirebaseDatabase.getInstance().reference
                            .child("Follow").child(it1)
                            .child("Following").child(profileId).setValue(pname)
                    }

                    firebaseUser.uid.let { it1 ->
                        FirebaseDatabase.getInstance().reference
                            .child("Follow").child(profileId)
                            .child("Connections").child(it1.toString()).setValue(cname)
                    }

                    firebaseUser.uid.let { it1 ->
                        FirebaseDatabase.getInstance().reference
                            .child("Follow").child(profileId)
                            .child("Requests").child(it1.toString()).setValue(cname)
                    }

                }

                getButtonText == "Connected" -> {
                    firebaseUser?.uid.let { it1 ->
                        FirebaseDatabase.getInstance().reference
                            .child("Follow").child(it1.toString())
                            .child("Following").child(profileId).removeValue()
                    }

                    firebaseUser.uid.let { it1 ->
                        FirebaseDatabase.getInstance().reference
                            .child("Follow").child(profileId)
                            .child("Connections").child(it1.toString()).removeValue()
                    }
                }
            }
        }
    }

    private fun getConnections() {
        val followersRef = FirebaseDatabase.getInstance().reference
            .child("Follow").child(profileId)
            .child("Connections")
        followersRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    findViewById<TextView>(R.id.connectionsNumber)?.text =
                       "${ snapshot.childrenCount.toString() } connections"
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    //Getting user info of a Profile Id
    private fun userInfo() {
            val database = FirebaseDatabase.getInstance().getReference("Users")
            database.child(profileId).get().addOnSuccessListener {
                val name = it.child("name").value.toString()
                val headline = it.child("headline").value.toString()
                val coverImage = it.child("coverImageURL").value.toString()
                val profileImage = it.child("profileImageURL").value.toString()
                findViewById<TextView>(R.id.txt_name).text = name
                findViewById<TextView>(R.id.txt_headline).text = headline
                Glide.with(applicationContext).load(coverImage).into(findViewById(R.id.background_img))
                Glide.with(applicationContext).load(profileImage).into(findViewById(R.id.profileImg))
            }
    }

    override fun onStop() {
        super.onStop()

        val pref = this.getSharedPreferences("PREFS", Context.MODE_PRIVATE)?.edit()
        pref?.putString("profileId", firebaseUser.uid)
        pref?.apply()
    }

    override fun onPause() {
        super.onPause()

        val pref = this.getSharedPreferences("PREFS", Context.MODE_PRIVATE)?.edit()
        pref?.putString("profileId", firebaseUser.uid)
        pref?.apply()
    }

    override fun onDestroy() {
        super.onDestroy()

        val pref = this.getSharedPreferences("PREFS", Context.MODE_PRIVATE)?.edit()
        pref?.putString("profileId", firebaseUser.uid)
        pref?.apply()
    }
    private fun checkConnection() {
        val followingRef = firebaseUser?.uid.let { it1 ->
            FirebaseDatabase.getInstance().reference
                .child("Follow").child(it1.toString())
                .child("Connections")}
        if (followingRef != null){
            followingRef.addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    if(snapshot.child(profileId).exists()){
                        findViewById<TextView>(R.id.connectBtn)?.text = "Connected"
                    }
                    else{
                        findViewById<TextView>(R.id.connectBtn)?.text = "Connect"
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
        }
    }
}