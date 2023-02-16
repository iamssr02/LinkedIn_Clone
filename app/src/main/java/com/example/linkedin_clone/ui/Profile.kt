package com.example.linkedin_clone.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.linkedin_clone.R
import com.example.linkedin_clone.searchActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class Profile : AppCompatActivity() {
    private lateinit var profileId : String
    private lateinit var firebaseUser: FirebaseUser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        findViewById<TextView>(R.id.item_search_input)?.setOnClickListener {
            val intent = Intent (this, searchActivity::class.java)
            startActivity(intent)
        }

        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        val pref = this.getSharedPreferences("PREFS", Context.MODE_PRIVATE)
        if (pref != null){
            this.profileId = pref.getString("profileId", "none").toString()
        }
        if (profileId == firebaseUser.uid){
            findViewById<TextView>(R.id.connectBtn).text = "Open to"
            findViewById<TextView>(R.id.messageBtn).text = "Add section"
        }
        else if (profileId != firebaseUser.uid){
            findViewById<TextView>(R.id.connectBtn).text = "Connect"
            findViewById<TextView>(R.id.messageBtn).text = "Message"
        }

    }
}