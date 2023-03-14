package com.example.linkedin_clone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.linkedin_clone.DataClasses.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class detailsActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    var email: String? = ""
    var name: String? = ""
    var additionalName: String? = ""
    var pronouns: String? = ""
    var education: String? = ""
    var industry: String? = ""
    var headline: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        if (intent.hasExtra("Name")) {
            this.name = intent.getStringExtra("Name").toString()
            val arr = name!!.split(" ")
            val firstName = arr[0]
            var lastName = ""
            for (i in 1 until (arr.size)) {
                lastName = lastName + arr[i] + " "
            }
            findViewById<EditText>(R.id.firstName).setText(firstName)
            findViewById<EditText>(R.id.lastName).setText(lastName)
        }
        if (intent.hasExtra("Email")) {
            email = intent.getStringExtra("Email").toString()
        }
        findViewById<Button>(R.id.saveBtn).setOnClickListener {
            val firstName = findViewById<EditText>(R.id.firstName).text.toString()
            val lastName = findViewById<EditText>(R.id.lastName).text.toString()
            this.name = "$firstName $lastName"
            this.additionalName = findViewById<EditText>(R.id.additionalName).text.toString()
            this.pronouns = findViewById<EditText>(R.id.pronouns).text.toString()
            this.education = findViewById<EditText>(R.id.education).text.toString()
            this.industry = findViewById<EditText>(R.id.industry).text.toString()
            this.headline = findViewById<EditText>(R.id.headline).text.toString()
            if (firstName.isEmpty()) {
                Toast.makeText(this, "Enter your first name", Toast.LENGTH_SHORT).show()
            } else if (lastName.isEmpty()) {
                Toast.makeText(this, "Enter your last name", Toast.LENGTH_SHORT).show()
            } else if (headline!!.isEmpty()) {
                Toast.makeText(this, "Enter your headline", Toast.LENGTH_SHORT).show()
            } else if (pronouns!!.isEmpty()) {
                Toast.makeText(this, "Enter your pronoun", Toast.LENGTH_SHORT).show()
            } else if (education!!.isEmpty()) {
                Toast.makeText(this, "Enter your education", Toast.LENGTH_SHORT).show()
            } else if (industry!!.isEmpty()) {
                Toast.makeText(this, "Enter your industry", Toast.LENGTH_SHORT).show()
            } else {
                updateDatabase()
                startActivity(Intent(this, profilePhotoActivity::class.java))
            }
        }
    }

    private fun updateDatabase() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val currentUserId = currentUser!!.uid
        database = FirebaseDatabase.getInstance().getReference("Users")
        val User = User(
            "$name",
            "$email",
            "$pronouns",
            "$headline",
            "$industry",
            "$education",
            "${currentUserId}",
            "",
            ""
        )

        //Realtime database upload
        database.child(currentUserId).setValue(User)

        //Firestore database upload
        val db = Firebase.firestore.collection("Users").document(currentUserId)
        db.set(User)
    }
}