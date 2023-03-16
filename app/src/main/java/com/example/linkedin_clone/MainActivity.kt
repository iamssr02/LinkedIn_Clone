package com.example.linkedin_clone

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.example.linkedin_clone.Fragments.HomeFragment
import com.example.linkedin_clone.Fragments.JobsFragment
import com.example.linkedin_clone.Fragments.NetworkFragment
import com.example.linkedin_clone.Fragments.NotificationFragment
import com.example.linkedin_clone.databinding.ActivityMainBinding
import com.example.linkedin_clone.Fragments.*
import com.example.linkedin_clone.ui.Profile
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.nav_view)
        val navController = findNavController(R.id.fragmentContainerView2)
        bottomNavigationView.setupWithNavController(navController)

        findViewById<TextView>(R.id.item_search_input).setOnClickListener {
            startActivity(Intent(this, searchActivity::class.java))
        }


        val menuIcon = findViewById<ImageView>(R.id.profileImg)
        menuIcon.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        drawerLayout = findViewById(R.id.container)
        val navView1: NavigationView = findViewById(R.id.nav_view_menu)
        val headerView = navView1.getHeaderView(0)
        headerView.findViewById<LinearLayout>(R.id.viewProfile).setOnClickListener {
            startActivity(Intent(this, Profile::class.java))
        }
        navView1.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_account -> {
                    Toast.makeText(this@MainActivity, "Not yet implemented", Toast.LENGTH_SHORT)
                        .show()
                }
                R.id.nav_logout -> {
                    Toast.makeText(this@MainActivity, "Not yet implemented", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            true
        }
        val dbref = FirebaseDatabase.getInstance().getReference("Users")
        dbref.child(FirebaseAuth.getInstance().currentUser!!.uid).get().addOnSuccessListener {
            val profileImage = it.child("profileImageURL").value.toString()
            val name = it.child("name").value.toString()
            headerView.findViewById<TextView>(R.id.user_name).text = name
            Glide.with(applicationContext).load(profileImage).into(findViewById(R.id.profileImg))
            Glide.with(applicationContext).load(profileImage).into(headerView.findViewById<ImageView>(R.id.img))
        }
    }

    override fun onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }
}