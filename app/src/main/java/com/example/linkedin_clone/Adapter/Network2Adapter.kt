package com.example.linkedin_clone.Adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConnectionsViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_network, parent, false)
        context = parent.context
        return ConnectionsViewHolder(view)
    }

    override fun onBindViewHolder(holder: ConnectionsViewHolder, position: Int) {
        val item = getItem(position)
        holder.bindView(item, context)

        firebaseUser = FirebaseAuth.getInstance().currentUser!!

        holder.itemView.setOnClickListener {
            val intent = Intent(context, Profile::class.java)
            intent.putExtra("ID", item.id)
            (context as FragmentActivity).startActivity(intent)
        }
    }


    class ConnectionsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val user: TextView = itemView.findViewById(R.id.txt_name)
        val headline: TextView = itemView.findViewById(R.id.text_headline)
        val profileImage: ImageView = itemView.findViewById(R.id.profileImg)
        val coverImage: ImageView = itemView.findViewById(R.id.coverImg)
        fun bindView(item: ConnectionRequestUser, context: Context) {
            user.text = item.firstName
            headline.text = item.headline
            Glide.with(context).load(item.coverImage).into(coverImage)
            Glide.with(context).load(item.profileImage).into(profileImage)
        }
    }


}

class ConnectionDiffUtilCallback2() : DiffUtil.ItemCallback<ConnectionRequestUser>() {
    override fun areItemsTheSame(
        oldItem: ConnectionRequestUser,
        newItem: ConnectionRequestUser
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: ConnectionRequestUser,
        newItem: ConnectionRequestUser
    ): Boolean {
        return oldItem == newItem
    }

}