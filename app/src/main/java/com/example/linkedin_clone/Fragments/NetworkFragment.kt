package com.example.linkedin_clone.Fragments

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
import com.example.linkedin_clone.Adapter.Network2Adapter
import com.example.linkedin_clone.Adapter.NetworkAdapter
import com.example.linkedin_clone.DataClasses.ConnectionRequestUser
import com.example.linkedin_clone.R
import com.example.linkedin_clone.ui.manageNetwork
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class NetworkFragment : Fragment() {
    private lateinit var dbref : DatabaseReference
    private lateinit var dbref2 : DatabaseReference
    private lateinit var userRecyclerview : RecyclerView
    private lateinit var userRecyclerview2 : RecyclerView
    private lateinit var networkAdapter: NetworkAdapter
    private lateinit var networkAdapter2: Network2Adapter
    private lateinit var userArrayList : ArrayList<ConnectionRequestUser>
    private lateinit var userArrayList2 : ArrayList<ConnectionRequestUser>
    private lateinit var array: ArrayList<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_network, container, false)
        userRecyclerview = view.findViewById(R.id.request_recyclerView)
        userRecyclerview2 = view.findViewById(R.id.recycler_network)
        userRecyclerview.layoutManager = LinearLayoutManager(context)
        userRecyclerview2.layoutManager = LinearLayoutManager(context)
        userRecyclerview.setHasFixedSize(true)
        userRecyclerview2.setHasFixedSize(true)

        userArrayList = arrayListOf<ConnectionRequestUser>()
        userArrayList2 = arrayListOf<ConnectionRequestUser>()
        array = arrayListOf<String>()
        getUserData(view)
        getUserData2()

        view.findViewById<TextView>(R.id.manageNetwork)?.setOnClickListener {
            val intent = Intent (activity, manageNetwork::class.java)
            activity?.startActivity(intent)
        }

        return view

    }

    private fun getUserData(view: View) {
        val currentUid = FirebaseAuth.getInstance().currentUser!!.uid
        dbref = FirebaseDatabase.getInstance().getReference("Follow").child(currentUid).child("Requests")

        dbref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()){
                    view.findViewById<LinearLayout>(R.id.showMore).visibility = View.VISIBLE
                    for(userSnapshot in snapshot.children) {
                        val user = ConnectionRequestUser(userSnapshot.key.toString(),
                            userSnapshot.value.toString())
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
    private fun getUserData2() {
        val currentUid = FirebaseAuth.getInstance().currentUser!!.uid
        dbref = FirebaseDatabase.getInstance().getReference("Follow").child(currentUid).child("Connections")

        dbref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()){

                    for(userSnapshot in snapshot.children) {

//                        array.add(userSnapshot.key.toString())
                    }
                    array.add(currentUid)

                }
            }

            override fun onCancelled(error: DatabaseError) {
            }


        })

        dbref2 = FirebaseDatabase.getInstance().getReference("Users")

        dbref2.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()){
                    for(userSnapshot in snapshot.children) {
                        if(array.contains(userSnapshot.key.toString())){
                        }
                        else{
                            val user = ConnectionRequestUser(userSnapshot.key.toString(),
                                userSnapshot.child("name").value.toString())
                            userArrayList2.add(user)
                        }
                    }


                    networkAdapter2 = Network2Adapter()
                    userRecyclerview2.adapter = networkAdapter2
                    Log.d("TAG", "onDataChange: $userArrayList2")
                    networkAdapter2.submitList(userArrayList2)

                }

            }

            override fun onCancelled(error: DatabaseError) {
            }


        })

    }
}