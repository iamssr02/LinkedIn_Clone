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
    private var firebaseUser : FirebaseUser? = FirebaseAuth.getInstance().currentUser
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

//        Picasso.get().load(user.getImage()).placeholder(R.drawable.user).into(holder.profileImage)
//        checkFollowingStatus(user.getUID(),holder.followBtn)

        holder.itemView.setOnClickListener {
            val pref = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit()
            pref.putString("profileId",user.getUID())
            pref.apply()
            (mContext as FragmentActivity).startActivity(Intent(this@SearchAdapter.mContext,Profile::class.java))
        }

//        holder.followBtn.setOnClickListener {
//            if(holder.followBtn.text.toString()=="Follow"){
//                firebaseUser?.uid.let { it1 ->
//                    FirebaseDatabase.getInstance().reference
//                        .child("Follow").child(it1.toString())
//                        .child("Following").child(user.getUID())
//                        .setValue(true).addOnCompleteListener { task ->
//                            if(task.isSuccessful){
//                                firebaseUser?.uid.let { it1 ->
//                                    FirebaseDatabase.getInstance().reference
//                                        .child("Follow").child(user.getUID())
//                                        .child("Followers").child((it1.toString()))
//                                        .setValue(true).addOnCompleteListener { task ->
//                                            if (task.isSuccessful){
//
//                                            }
//                                        }
//                                }
//                            }
//                        }
//                }
//            }
//            else{
//                firebaseUser?.uid.let { it1 ->
//                    FirebaseDatabase.getInstance().reference
//                        .child("Follow").child(it1.toString())
//                        .child("Following").child(user.getUID())
//                        .removeValue().addOnCompleteListener { task ->
//                            if(task.isSuccessful){
//                                firebaseUser?.uid.let { it1 ->
//                                    FirebaseDatabase.getInstance().reference
//                                        .child("Follow").child(user.getUID())
//                                        .child("Followers").child((it1.toString()))
//                                        .removeValue().addOnCompleteListener { task ->
//                                            if (task.isSuccessful){
//
//                                            }
//                                        }
//                                }
//                            }
//                        }
//                }
//            }
//        }
    }

    private fun checkFollowingStatus(uid: String, followBtn: Button) {
        val followingRef = firebaseUser?.uid.let { it1 ->
            FirebaseDatabase.getInstance().reference
                .child("Follow").child(it1.toString())
                .child("Following")
        }
        followingRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.child(uid).exists()){
                    followBtn.text = "Following"
                }
                else{
                    followBtn.text = "Follow"
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    class ViewHolder(@NonNull itemView : View): RecyclerView.ViewHolder(itemView){
        val name : TextView = itemView.findViewById(R.id.username)
        val profileImage : CircleImageView = itemView.findViewById(R.id.profile_image)
        val followBtn : Button = itemView.findViewById(R.id.followBtn)
    }
}