package com.example.linkedin_clone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.cardview.widget.CardView

class card_network_request : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.card_network_request)
        findViewById<CardView>(R.id.connect_cancel).setOnClickListener {
            Toast.makeText(this,"Clicked",Toast.LENGTH_SHORT).show()
        }
        findViewById<CardView>(R.id.connect_ok).setOnClickListener {
            Toast.makeText(this,"Clicked",Toast.LENGTH_SHORT).show()
        }
    }
}