package com.example.linkedin_clone.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.linkedin_clone.Adapter.NetworkAdapter
import com.example.linkedin_clone.DataClasses.ConnectionRequestUser
import com.example.linkedin_clone.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue

class NetworkFragment : Fragment() {
    private lateinit var dbref : DatabaseReference
    private lateinit var userRecyclerview : RecyclerView
    private lateinit var networkAdapter: NetworkAdapter
    private lateinit var userArrayList : ArrayList<ConnectionRequestUser>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_network, container, false)
        userRecyclerview = view.findViewById(R.id.request_recyclerView)
        userRecyclerview.layoutManager = LinearLayoutManager(context)
        userRecyclerview.setHasFixedSize(true)

        userArrayList = arrayListOf<ConnectionRequestUser>()
        getUserData()
        return view

    }

    private fun getUserData() {
        val currentUid = FirebaseAuth.getInstance().currentUser!!.uid
        dbref = FirebaseDatabase.getInstance().getReference("Follow").child(currentUid).child("Requests")

        dbref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()){
                    for(userSnapshot in snapshot.children) {
                        val user = ConnectionRequestUser(
                            userSnapshot.value.toString()
                        )
                        Log.d("TAG", "Value: $user")
                        userArrayList.add(user)

                    }


                    networkAdapter = NetworkAdapter()
                    userRecyclerview.adapter = networkAdapter
                    Log.d("TAG", "onDataChange: $userArrayList")
                    networkAdapter.submitList(userArrayList)

                }

            }

            override fun onCancelled(error: DatabaseError) {
            }


        })

    }
}