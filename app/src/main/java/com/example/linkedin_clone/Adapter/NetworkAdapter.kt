package com.example.linkedin_clone.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.linkedin_clone.DataClasses.ConnectionRequestUser
import com.example.linkedin_clone.R

class NetworkAdapter() : ListAdapter<ConnectionRequestUser, NetworkAdapter.ConnectionsViewHolder>(ConnectionDiffUtilCallback()){

    private lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConnectionsViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.card_network_request, parent, false)
        context = parent.context
        return ConnectionsViewHolder(view)
    }

    override fun onBindViewHolder(holder: ConnectionsViewHolder, position: Int) {
        val item = getItem(position)
        holder.bindView(item, context)
    }

    class ConnectionsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindView(item: ConnectionRequestUser, context: Context) {
            val user : TextView = itemView.findViewById(R.id.item_text)
            user.text = item.firstName
        }
    }

}

class ConnectionDiffUtilCallback(): DiffUtil.ItemCallback<ConnectionRequestUser>(){
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