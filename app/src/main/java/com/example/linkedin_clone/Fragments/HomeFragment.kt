package com.example.linkedin_clone.Fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.linkedin_clone.Adapter.ImageAdapter
import com.example.linkedin_clone.DataClasses.imageUsers
import com.example.linkedin_clone.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment : Fragment() {

    private lateinit var dbref : DatabaseReference
    private lateinit var userRecyclerview : RecyclerView
    private lateinit var imageAdapter: ImageAdapter
    private lateinit var con: Context

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_home, container, false)
        con = requireContext()
        userRecyclerview = view.findViewById(R.id.posts)
        userRecyclerview.layoutManager = LinearLayoutManager(context)
        userRecyclerview.setHasFixedSize(true)
        getUserData()

        view.findViewById<SwipeRefreshLayout>(R.id.swipeToRefresh).setOnRefreshListener {
            getUserData()
            view.findViewById<SwipeRefreshLayout>(R.id.swipeToRefresh).isRefreshing = false
        }
        return view
        }
    private fun getUserData() {
        val currentUid = FirebaseAuth.getInstance().currentUser!!.uid
        var userArrayList : ArrayList<imageUsers> = arrayListOf<imageUsers>()
        dbref = FirebaseDatabase.getInstance().getReference("Images")
        dbref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()){
                    for(userSnapshot in snapshot.children) {
                        val user = imageUsers(
                            userSnapshot.child("id").value.toString(),
                            userSnapshot.child("caption").value.toString(),
                            userSnapshot.child("imageURL").value.toString(),
                            userSnapshot.child("uploadedBy").value.toString(),
                            userSnapshot.child("name").value.toString(),
                            userSnapshot.child("headline").value.toString(),
                            userSnapshot.child("timeStamp").value.toString(),
                        )
                        userArrayList.add(user)

                    }
                    Log.d("TAG", "image: $userArrayList")
                    imageAdapter = ImageAdapter(userArrayList,con)
                    userRecyclerview.adapter = imageAdapter

                }

            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}