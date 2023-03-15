package com.example.linkedin_clone.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.linkedin_clone.MainActivity
import com.example.linkedin_clone.R
import com.google.firebase.auth.FirebaseAuth

class splashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        Handler(Looper.getMainLooper()).postDelayed({
            val user = FirebaseAuth.getInstance().currentUser
            if (user != null) {
                // User is signed in
                // Start home activity
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                // No user is signed in
                // start login activity
                startActivity(Intent(this, JoinNow::class.java))
            }
            // close splash activity
            // close splash activity
            finish()
        }, 2000)
    }
}