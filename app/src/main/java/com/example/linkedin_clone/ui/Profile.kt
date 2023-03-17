package com.example.linkedin_clone.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.bumptech.glide.Glide
import com.example.linkedin_clone.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class Profile : AppCompatActivity() {
    private lateinit var firebaseUser: FirebaseUser

    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        firebaseUser = FirebaseAuth.getInstance().currentUser!!

        CoroutineScope(Dispatchers.Main).launch {
            var profileId = FirebaseAuth.getInstance().currentUser!!.uid
            var pname: String = ""
            var cname: String = ""

            val pref = getSharedPreferences("PREFS", Context.MODE_PRIVATE)
            if (pref != null) {
                profileId = pref.getString("profileId", "none").toString()
            }
            userInfo(profileId)
            getConnections(profileId)

            pname =
                FirebaseDatabase.getInstance().getReference("Users").child(profileId).get().await()
                    .child("name").value.toString()
            cname =
                FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.uid).get()
                    .await().child("name").value.toString()
            if (profileId == firebaseUser.uid) {
                findViewById<TextView>(R.id.connectBtn).text = "Open to"
                findViewById<TextView>(R.id.messageBtn).text = "Add section"
            } else if (profileId != firebaseUser.uid) {
                checkConnection(profileId)
                findViewById<TextView>(R.id.messageBtn).text = "Message"
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

        //On Click Listeners
        findViewById<ImageView>(R.id.btn_back).setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }

        findViewById<TextView>(R.id.item_search_input)?.setOnClickListener {
            val intent = Intent(this, searchActivity::class.java)
            startActivity(intent)
        }

        findViewById<ImageView>(R.id.profileImg).setOnClickListener {
            startActivity(Intent(this, profilePhotoActivity::class.java))
        }

        findViewById<ImageView>(R.id.background_img).setOnClickListener {
            startActivity(Intent(this, coverPhotoActivity::class.java))
        }
    }

    private fun getConnections(profileId: String) {
        val followersRef = FirebaseDatabase.getInstance().reference
            .child("Follow").child(profileId)
            .child("Connections")
        followersRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    findViewById<TextView>(R.id.connectionsNumber)?.text =
                        "${snapshot.childrenCount.toString()} connections"
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    //Getting user info of a Profile Id
    private suspend fun userInfo(profileId: String) {
//        val database = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().currentUser!!.uid)
        val database = FirebaseDatabase.getInstance().getReference("Users").child(profileId)
        val name = database.get().await().child("name").value.toString()
        val headline = database.get().await().child("headline").value.toString()
        val coverImage = database.get().await().child("coverImageURL").value.toString()
        val profileImage = database.get().await().child("profileImageURL").value.toString()
        findViewById<TextView>(R.id.txt_name).text = name
        findViewById<TextView>(R.id.txt_headline).text = headline
        Glide.with(applicationContext).load(coverImage).into(findViewById(R.id.background_img))
        Glide.with(applicationContext).load(profileImage).into(findViewById(R.id.profileImg))
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

    private fun checkConnection(profileId: String) {
        val followingRef = firebaseUser?.uid.let { it1 ->
            FirebaseDatabase.getInstance().reference
                .child("Follow").child(it1.toString())
                .child("Connections")
        }
        if (followingRef != null) {
            followingRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    if (snapshot.child(profileId).exists()) {
                        findViewById<TextView>(R.id.connectBtn)?.text = "Connected"
                    } else {
                        findViewById<TextView>(R.id.connectBtn)?.text = "Connect"
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
        }
    }
}