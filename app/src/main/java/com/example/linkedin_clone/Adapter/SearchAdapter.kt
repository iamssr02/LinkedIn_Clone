package com.example.linkedin_clone.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.linkedin_clone.DataClasses.User
import com.example.linkedin_clone.R
import com.example.linkedin_clone.ui.Profile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class SearchAdapter (private var mContext: Context,
                     private var mUser: List<User>,
                     private var isFragment:Boolean = false):
    RecyclerView.Adapter<SearchAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchAdapter.ViewHolder {
        val view: View = LayoutInflater.from(mContext).inflate(R.layout.search_item_layout, parent, false)
        return SearchAdapter.ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mUser.size
    }

    override fun onBindViewHolder(holder:SearchAdapter.ViewHolder, position: Int){
        val user = mUser[position]
        holder.name.text = user.getName()
        holder.headline.text = user.getHeadline()
        Glide.with(mContext).load(user.getProfileImageURL()).into(holder.profileImage)

        holder.itemView.setOnClickListener {
            val intent = Intent(mContext, Profile::class.java)
            intent.putExtra("ID", user.getUID())
            (mContext as FragmentActivity).startActivity(intent)
        }
    }

    class ViewHolder(@NonNull itemView : View): RecyclerView.ViewHolder(itemView){
        val name : TextView = itemView.findViewById(R.id.username)
        val profileImage : CircleImageView = itemView.findViewById(R.id.profile_image)
        val headline: TextView = itemView.findViewById(R.id.headline)
    }
}