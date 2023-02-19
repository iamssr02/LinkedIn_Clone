package com.example.linkedin_clone

import android.app.Application
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class LinkedInOffline : Application() {

    override fun onCreate() {
        super.onCreate()
        Firebase.database.setPersistenceEnabled(true)
        val userRef = Firebase.database.getReference("Users")
        userRef.keepSynced(true)
        val userConnection = Firebase.database.getReference("Follow")
        userConnection.keepSynced(true)
    }
}