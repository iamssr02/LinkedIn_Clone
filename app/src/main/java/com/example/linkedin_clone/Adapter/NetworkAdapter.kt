package com.example.linkedin_clone.Adapter

import android.content.Context
import android.content.Intent
import android.util.Log
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
import com.google.firebase.database.FirebaseDatabase

class NetworkAdapter() : ListAdapter<ConnectionRequestUser, NetworkAdapter.ConnectionsViewHolder>(
    ConnectionDiffUtilCallback()
) {
    private lateinit var firebaseUser: FirebaseUser
    private lateinit var context: Context
    private lateinit var pname: String
    private lateinit var cname: String
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConnectionsViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_network_request, parent, false)
        context = parent.context
        return ConnectionsViewHolder(view)
    }

    override fun onBindViewHolder(holder: ConnectionsViewHolder, position: Int) {
        val item = getItem(position)
        holder.bindView(item, context)

        firebaseUser = FirebaseAuth.getInstance().currentUser!!

        FirebaseDatabase.getInstance().getReference("Users").child(item.id).get().addOnSuccessListener {
            this.pname = it.child("name").value.toString()
        }
        FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.uid).get().addOnSuccessListener {
            this.cname = it.child("name").value.toString()
        }

        holder.itemView.findViewById<CardView>(R.id.connect_ok).setOnClickListener {
            firebaseUser.uid.let { it1 ->
                FirebaseDatabase.getInstance().reference
                    .child("Follow").child(it1.toString())
                    .child("Requests").child(item.id).removeValue()
            }
            firebaseUser.uid.let { it1 ->
                FirebaseDatabase.getInstance().reference
                    .child("Follow").child(item.id)
                    .child("Connections").child(it1.toString()).setValue(cname)
            }
        }
        holder.itemView.findViewById<CardView>(R.id.connect_cancel).setOnClickListener {
            firebaseUser.uid.let { it1 ->
                FirebaseDatabase.getInstance().reference
                    .child("Follow").child(it1.toString())
                    .child("Connections").child(item.id).removeValue()
            }
            firebaseUser.uid.let { it1 ->
                FirebaseDatabase.getInstance().reference
                    .child("Follow").child(it1.toString())
                    .child("Requests").child(item.id).removeValue()
            }
        }
        holder.itemView.findViewById<CardView>(R.id.item_parent).setOnClickListener {
            Log.d("TAG", "onBindViewHolder: Clicked")
            Toast.makeText(context,"Clicked ${item.id}",Toast.LENGTH_SHORT).show()
//            val pref = context.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit()
//            pref.putString("profileId",item.id)
//            pref.apply()
//            (context as FragmentActivity).startActivity(
//                Intent(this@NetworkAdapter.context,
//                    Profile::class.java)
//            )
        }
    }

    class ConnectionsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindView(item: ConnectionRequestUser, context: Context) {
            val user: TextView = itemView.findViewById(R.id.item_text)
            user.text = item.firstName
        }
    }

}

class ConnectionDiffUtilCallback() : DiffUtil.ItemCallback<ConnectionRequestUser>() {
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