package com.example.linkedin_clone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.linkedin_clone.Adapter.SearchAdapter
import com.example.linkedin_clone.DataClasses.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class searchActivity : AppCompatActivity() {
    private var recyclerView : RecyclerView? = null
    private var userAdapter : SearchAdapter? = null
    private var mUser : MutableList<User>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        recyclerView = findViewById(R.id.recycler_view_search)
        recyclerView?.setHasFixedSize(true)
        recyclerView?.layoutManager = LinearLayoutManager(this)
        mUser = ArrayList()
        userAdapter = SearchAdapter(this, mUser as ArrayList<User>, true)
        recyclerView?.adapter = userAdapter
        findViewById<EditText>(R.id.search_layout).addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(findViewById<EditText>(R.id.search_layout).text.toString()==""){
                }
                else{
                    recyclerView?.visibility = View.VISIBLE
                    retrieveUsers()
                    searchUser(s.toString())
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
    }
    private fun searchUser(input: String) {
        val query = FirebaseDatabase.getInstance().getReference("Users")
            .orderByChild("name").startAt(input).endAt(input + "\uf8ff")
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                mUser?.clear()
                for(snapshot in dataSnapshot.children){
                    val user = snapshot.getValue(User::class.java)
                    if (user!=null){
                        mUser?.add(user)
                    }
                }
                Log.d("TAG", "searchResult: $mUser")
                userAdapter?.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

    }

    private fun retrieveUsers() {
        val userRef = FirebaseDatabase.getInstance().getReference("Users")
        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(findViewById<EditText>(R.id.search_layout)?.text.toString()!=""){
                    mUser?.clear()
                    for(snapshot in dataSnapshot.children){
                        val user = snapshot.getValue(User::class.java)
                        if (user!=null){
                            mUser?.add(user)
                        }
                    }
                    userAdapter?.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}