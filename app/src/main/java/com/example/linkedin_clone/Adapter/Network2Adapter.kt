package com.example.linkedin_clone.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.linkedin_clone.DataClasses.ConnectionRequestUser
import com.example.linkedin_clone.R
import com.example.linkedin_clone.ui.Profile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Network2Adapter() : ListAdapter<ConnectionRequestUser, Network2Adapter.ConnectionsViewHolder>(
    ConnectionDiffUtilCallback2()
) {
    private lateinit var firebaseUser: FirebaseUser
    private lateinit var context: Context
    private lateinit var pname: String
    private lateinit var cname: String
    private lateinit var profileId: String

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConnectionsViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_network, parent, false)
        context = parent.context
        return ConnectionsViewHolder(view)
    }

    override fun onBindViewHolder(holder: ConnectionsViewHolder, position: Int) {
        val item = getItem(position)
        holder.bindView(item, context)
        this.profileId = item.id
        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        holder.itemView.setOnClickListener {
            val pref = context.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit()
            pref.putString("profileId",item.id)
            pref.apply()
            (context as FragmentActivity).startActivity(
                Intent(this@Network2Adapter.context,
                    Profile::class.java)
            )
        }
        val followingRef = firebaseUser?.uid.let { it1 ->
            FirebaseDatabase.getInstance().reference
                .child("Follow").child(it1.toString())
                .child("Connections")}
        if (followingRef != null){
            followingRef.addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    //chechking whether we follow the user or not
                    if(snapshot.child(profileId).exists()){
                        holder.itemView.findViewById<TextView>(R.id.txt1)?.text = "Connected"
                    }
                    else{
                        holder.itemView.findViewById<TextView>(R.id.txt1)?.text = "Connect"
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
        }
    }


    class ConnectionsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindView(item: ConnectionRequestUser, context: Context) {
            val user: TextView = itemView.findViewById(R.id.txt_name)
            user.text = item.firstName

        }
    }


}

class ConnectionDiffUtilCallback2() : DiffUtil.ItemCallback<ConnectionRequestUser>() {
    override fun areItemsTheSame(
        oldItem: ConnectionRequestUser,
        newItem: ConnectionRequestUser
    ): Boolean {
        return oldItem.firstName == newItem.firstName
    }

    override fun areContentsTheSame(
        oldItem: ConnectionRequestUser,
        newItem: ConnectionRequestUser
    ): Boolean {
        return oldItem == newItem
    }

}