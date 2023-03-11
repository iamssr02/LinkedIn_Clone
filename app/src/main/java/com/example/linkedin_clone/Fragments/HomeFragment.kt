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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.linkedin_clone.Adapter.ImageAdapter
import com.example.linkedin_clone.DataClasses.PostClassUser
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
    private lateinit var userArrayList : ArrayList<imageUsers>
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
        userArrayList = arrayListOf<imageUsers>()
        getUserData()
        return view
        }
    private fun getUserData() {
        val currentUid = FirebaseAuth.getInstance().currentUser!!.uid
        dbref = FirebaseDatabase.getInstance().getReference("Images")
        dbref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()){
                    for(userSnapshot in snapshot.children) {
                        val user = imageUsers(userSnapshot.child("caption").value.toString(),userSnapshot.child("imageURL").value.toString())
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