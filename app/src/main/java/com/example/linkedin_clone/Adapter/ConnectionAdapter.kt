package com.example.linkedin_clone.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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

class ConnectionAdapter() : ListAdapter<ConnectionRequestUser, ConnectionAdapter.ConnectionsViewHolder>(
    ConnectionsDiffUtilCallback()
) {
    private lateinit var firebaseUser: FirebaseUser
    private lateinit var context: Context
    private lateinit var pname: String
    private lateinit var cname: String

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConnectionsViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_connection, parent, false)
        context = parent.context
        return ConnectionsViewHolder(view)
    }

    override fun onBindViewHolder(holder: ConnectionsViewHolder, position: Int) {
        val item = getItem(position)
        holder.bindView(item, context)
        holder.itemView.setOnClickListener {
            val pref = context.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit()
            pref.putString("profileId",item.id)
            pref.apply()
            (context as FragmentActivity).startActivity(
                Intent(this@ConnectionAdapter.context,
                    Profile::class.java)
            )
        }
    }

    class ConnectionsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindView(item: ConnectionRequestUser, context: Context) {
            val user: TextView = itemView.findViewById(R.id.username)
            user.text = item.firstName
        }
    }

}

class ConnectionsDiffUtilCallback() : DiffUtil.ItemCallback<ConnectionRequestUser>() {
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